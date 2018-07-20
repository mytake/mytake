/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytakedotorg/mytakedotorg/graphs/contributors
 */
package common;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.typesafe.config.Config;
import java.util.Random;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.Renderer;
import org.jooby.whoops.Whoops;

/**
 * The app that we run in unit tests.  See {@link Prod} in the main
 * directory for the app that we run in production.
 */
public class Dev extends Jooby {
	public static Dev unitTest() {
		Dev dev = new Dev(new CleanPostgresModule());
		dev.use(new DevTime.Module());
		return dev;
	}

	public static Dev realtime(CleanPostgresModule postgres) {
		Dev dev = new Dev(postgres);
		Prod.realtime(dev);
		return dev;
	}

	public static Dev realtime() {
		return realtime(new CleanPostgresModule());
	}

	private Dev(CleanPostgresModule postgresModule) {
		// random and time-dependent results in tests will be repeatable
		use((env, conf, binder) -> {
			binder.bind(Random.class).toInstance(new Random(0));
		});
		use(new GreenMailModule());
		use(postgresModule);
		// exit has to come before the "/user" route
		get("/exit", (req, rsp) -> {
			stop();
		});
		Prod.common(this);
		Prod.controllers(this);
		use(new JooqDebugRenderer());
		use(new Whoops());
	}

	static class JooqDebugRenderer implements Jooby.Module {
		@Override
		public void configure(Env env, Config conf, Binder binder) throws Throwable {
			Multibinder.newSetBinder(binder, Renderer.class)
					.addBinding()
					.toInstance(new Renderer() {
						@Override
						public void render(Object value, Context ctx) throws Exception {
							if (value instanceof org.jooq.Result) {
								ctx.type(MediaType.html)
										.send("<!DOCTYPE html><html><body><div style=\"font-family:monospace\">" +
												value.toString().replace("\n", "<br>").replace(" ", "&nbsp;") +
												"</div></body></html>");
							}
						}
					});
		}
	}

	static class GreenMailModule implements Jooby.Module {
		@Override
		public void configure(Env env, Config conf, Binder binder) throws Throwable {
			ServerSetup setup = new ServerSetup(conf.getInt("mail.smtpPort"), conf.getString("mail.hostName"), "smtp");
			GreenMail greenMail = new GreenMail(setup);
			greenMail.start();
			env.onStop(greenMail::stop);
			binder.bind(GreenMail.class).toInstance(greenMail);

			env.router().get("/email", req -> {
				StringBuilder builder = new StringBuilder();
				builder.append("<ul>");
				MimeMessage[] messages = greenMail.getReceivedMessages();
				for (int i = 0; i < messages.length; ++i) {
					String index = Integer.toString(i + 1);
					builder.append("<li><a href=\"/email/" + index + "\">");
					builder.append(index + " &lt;");
					MimeMessage message = messages[i];
					for (Address addr : message.getRecipients(RecipientType.TO)) {
						builder.append(addr.toString());
					}
					builder.append("&gt; " + message.getSubject());
					builder.append("</a></li>");
				}
				builder.append("</ul>");
				return builder.toString();
			});
			env.router().get("/email/:idx", req -> {
				int idx = req.param("idx").intValue() - 1;
				MimeMessage[] messages = greenMail.getReceivedMessages();
				if (idx >= 0 && idx < messages.length) {
					MimeMessage message = messages[idx];
					MimeMultipart content = (MimeMultipart) message.getContent();
					StringBuilder buffer = new StringBuilder();
					for (int i = 0; i < content.getCount(); ++i) {
						buffer.append(content.getBodyPart(i).getContent().toString());
					}
					return buffer.toString();
				} else {
					return "No such message";
				}
			});
		}
	}

	public static void main(String[] args) {
		Dev dev = Dev.realtime();
		dev.use(new InitialData.Module());
		Jooby.run(() -> dev, args);
	}
}
