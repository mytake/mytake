/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytakedotorg/mytakedotorg/graphs/contributors
 */
package common;

import static db.Tables.ACCOUNT;

import auth.AuthModuleHarness;
import com.diffplug.common.base.Errors;
import com.diffplug.common.base.Throwing;
import com.icegreen.greenmail.util.GreenMail;
import db.tables.pojos.Account;
import io.restassured.specification.RequestSpecification;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jooby.Jooby;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.junit.rules.ExternalResource;

public class JoobyDevRule extends ExternalResource {
	public static JoobyDevRule empty() {
		return new JoobyDevRule(new Dev());
	}

	public static JoobyDevRule initialData() {
		return new JoobyDevRule(new Dev(), app -> {
			try (DSLContext dsl = app.dsl()) {
				InitialData.init(dsl, app.app().require(Time.class));
			}
		});
	}

	/** To prevent noise from moderator emails. */
	public static JoobyDevRule initialDataNoMods() {
		return new JoobyDevRule(new Dev(), app -> {
			try (DSLContext dsl = app.dsl()) {
				InitialData.init(dsl, app.app().require(Time.class));
				dsl.deleteFrom(db.Tables.MODERATOR).execute();
			}
		});
	}

	public JoobyDevRule(Jooby app) {
		this(app, unused -> {});
	}

	public JoobyDevRule(Jooby app, Throwing.Consumer<JoobyDevRule> init) {
		this.app = app;
		this.init = init;
	}

	private final Jooby app;
	private final Throwing.Consumer<JoobyDevRule> init;

	@Override
	public void before() throws Throwable {
		app.start("server.join=false");
		init.accept(this);
	}

	@Override
	public void after() {
		app.stop();
	}

	public DevTime time() {
		return (DevTime) app.require(Time.class);
	}

	public DSLContext dsl() {
		return app.require(DSLContext.class);
	}

	public <R extends TableRecord<R>> void insertRecord(R record) {
		try (DSLContext dsl = dsl()) {
			dsl.executeInsert(record);
		}
	}

	public <K, R extends TableRecord<R>> R fetchRecord(Table<R> table, TableField<R, K> keyField, K key) {
		try (DSLContext dsl = dsl()) {
			return dsl.selectFrom(table).where(keyField.eq(key)).fetchOne();
		}
	}

	private int emailsReceived = 0;

	public Map<String, EmailAssert> waitForEmails(int count) {
		GreenMail greenmail = app.require(GreenMail.class);
		MimeMessage[] messages;
		int numTries = 0;
		do {
			greenmail.waitForIncomingEmail(1);
			messages = greenmail.getReceivedMessages();
		} while (messages.length < emailsReceived + count && ++numTries < count);
		if (messages.length != emailsReceived + count) {
			String errorMsg = "Expected " + count + " new messages, but had " + (messages.length - emailsReceived);
			for (MimeMessage message : Arrays.asList(messages).subList(emailsReceived, messages.length)) {
				errorMsg += "\n    " + emailFor(message) + ": " + Errors.rethrow().get(() -> message.getSubject());
			}
			throw new AssertionError(errorMsg);
		}

		Map<String, EmailAssert> map = new HashMap<>();
		for (MimeMessage message : Arrays.asList(messages).subList(emailsReceived, emailsReceived + count)) {
			map.put(emailFor(message), new EmailAssert(message));
		}
		emailsReceived += count;
		return map;
	}

	public Map<String, EmailAssert> getOldEmails() {
		GreenMail greenmail = app.require(GreenMail.class);
		Map<String, EmailAssert> map = new HashMap<>();
		for (MimeMessage message : greenmail.getReceivedMessages()) {
			map.put(emailFor(message), new EmailAssert(message));
		}
		return map;
	}

	private static String emailFor(MimeMessage message) {
		try {
			InternetAddress addr = (InternetAddress) message.getRecipients(RecipientType.TO)[0];
			return addr.getAddress();
		} catch (MessagingException e) {
			throw Errors.asRuntime(e);
		}
	}

	public EmailAssert waitForEmail() {
		return waitForEmails(1).values().iterator().next();
	}

	public Jooby app() {
		return app;
	}

	/** Returns a request with cookies set for the given username. */
	public RequestSpecification givenUser(String username) {
		Account account = fetchRecord(ACCOUNT, ACCOUNT.USERNAME, username).into(Account.class);
		return AuthModuleHarness.givenUser(app, account);
	}
}
