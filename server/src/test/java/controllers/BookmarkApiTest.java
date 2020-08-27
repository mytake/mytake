/*
 * MyTake.org website and tooling.
 * Copyright (C) 2020 MyTake.org, Inc.
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
package controllers;

import common.JoobyDevRule;
import common.Time;
import io.restassured.http.ContentType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java2ts.Routes;
import javax.ws.rs.core.Response.Status;
import org.assertj.core.api.Assertions;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookmarkApiTest {
	@ClassRule
	public static JoobyDevRule dev = JoobyDevRule.initialData();

	//	@Test
	//	public void notAuth() {
	//		RestAssured.given().get(Routes.API_BOOKMARKS).then()
	//				.statusCode(Status.FORBIDDEN.getStatusCode());
	//	}

	@Test
	public void _01_empty() {
		dev.givenUser("samples").get(Routes.API_BOOKMARKS).then()
				.statusCode(Status.OK.getStatusCode())
				.body(Matchers.equalTo("[]"))
				.header("Last-Modified", Matchers.nullValue());
	}

	@Test
	public void _02_save() throws ParseException {
		dev.time().setCurrentMs(ts("1980"));
		dev.givenUser("samples")
				.contentType(ContentType.JSON)
				.body("["
						+ "{\"savedAt\":\"1970-01-01T00:00:00.000Z\",\"fact\":\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"start\":2,\"end\":718},"
						+ "{\"savedAt\":\"1970-01-01T00:00:00.000Z\",\"fact\":\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",\"start\":3,\"end\":14159}"
						+ "]")
				.put(Routes.API_BOOKMARKS).then()
				.statusCode(Status.CREATED.getStatusCode())
				.header("Last-Modified", Matchers.equalTo("Mon, 31 Dec 1979 16:00:00 GMT"))
				.body(Matchers.equalTo(""));
	}

	@Test
	public void _03_getAfterSave() throws ParseException {
		dev.givenUser("samples").get(Routes.API_BOOKMARKS).then()
				.statusCode(Status.OK.getStatusCode())
				.contentType(ContentType.JSON)
				.body(hasToString("["
						+ "{\"savedAt\":\"1980-01-01T00:00:00Z\",\"fact\":\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"start\":2,\"end\":718},"
						+ "{\"savedAt\":\"1980-01-01T00:00:00Z\",\"fact\":\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",\"start\":3,\"end\":14159}"
						+ "]"))
				.header("Last-Modified", Matchers.equalTo("Mon, 31 Dec 1979 16:00:00 GMT"));
	}

	@Test
	public void _04_saveIsIdempotent() throws ParseException {
		// move forward by a year
		dev.time().setCurrentMs(ts("1981"));
		dev.givenUser("samples")
				.contentType(ContentType.JSON)
				.body("["
						+ "{\"savedAt\":\"1970-01-01T00:00:00Z\",\"fact\":\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"start\":2,\"end\":718},"
						+ "{\"savedAt\":\"1970-01-01T00:00:00Z\",\"fact\":\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",\"start\":3,\"end\":14159}"
						+ "]")
				.put(Routes.API_BOOKMARKS).then()
				.statusCode(Status.CREATED.getStatusCode())
				// so the last modified does too
				.header("Last-Modified", Matchers.equalTo("Wed, 31 Dec 1980 16:00:00 GMT"))
				.body(Matchers.equalTo(""));
		// and so does the get
		dev.givenUser("samples").get(Routes.API_BOOKMARKS).then()
				.statusCode(Status.OK.getStatusCode())
				.contentType(ContentType.JSON)
				.body(hasToString("["
						+ "{\"savedAt\":\"1981-01-01T00:00:00Z\",\"fact\":\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"start\":2,\"end\":718},"
						+ "{\"savedAt\":\"1981-01-01T00:00:00Z\",\"fact\":\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",\"start\":3,\"end\":14159}"
						+ "]"))
				.header("Last-Modified", Matchers.equalTo("Wed, 31 Dec 1980 16:00:00 GMT"));
	}

	@Test
	public void _05_delete() throws ParseException {
		// move forward by another year
		dev.time().setCurrentMs(ts("1982"));

		dev.givenUser("samples")
				.contentType(ContentType.JSON)
				.body("[{\"savedAt\":\"1970-01-01T00:00:00Z\",\"fact\":\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\",\"start\":2,\"end\":718}]")
				.delete(Routes.API_BOOKMARKS).then()
				.statusCode(Status.OK.getStatusCode())
				// so the last modified does too
				.header("Last-Modified", Matchers.equalTo("Thu, 31 Dec 1981 16:00:00 GMT"))
				.body(Matchers.equalTo(""));
	}

	@Test
	public void _06_getAfterDelete() throws ParseException {
		dev.givenUser("samples").get(Routes.API_BOOKMARKS).then()
				.statusCode(Status.OK.getStatusCode())
				.contentType(ContentType.JSON)
				.body(hasToString("[{\"savedAt\":\"1981-01-01T00:00:00Z\",\"fact\":\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\",\"start\":3,\"end\":14159}]"))
				.header("Last-Modified", Matchers.equalTo("Thu, 31 Dec 1981 16:00:00 GMT"));
	}

	private static long ts(String year) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Time.setTZ(format);
		return format.parse(year).getTime();
	}

	private static Matcher<String> hasToString(String input) {
		return new BaseMatcher<String>() {
			@Override
			public boolean matches(Object actual) {
				Assertions.assertThat(actual.toString()).isEqualTo(input);
				return true;
			}

			@Override
			public void describeTo(Description description) {}
		};
	}
}