/*
 * MyTake.org website and tooling.
 * Copyright (C) 2017 MyTake.org, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact us at team@mytake.org
 */
package common;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;

public class NotFound implements Jooby.Module {
	public static Result result() {
		return Results.with(views.error404.template(), Status.NOT_FOUND);
	}

	public static RuntimeException exception() {
		return new NotFoundException();
	}

	static class NotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1830308574935746133L;
	}

	@Override
	public void configure(Env env, Config conf, Binder binder) throws Throwable {
		env.router().err(NotFoundException.class, (req, rsp, err) -> {
			rsp.send(result());
		});
	}
}
