package guestbook;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * Manages login, signup, and accountchooser status as well as a few helper
 * functions dealing with users.
 */
@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		HttpSession session = req.getSession(true);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		String form = req.getParameter("form");
		String email = req.getParameter("email");

		// There are 3 possible ways to post to this servlet:
		// 1 the login form
		// 2 the signoup form
		// 3 accountchooser.com status request

		// 1 login
		if ("login".equals(form)) {
			// Tiny bit of error handling on email...
			if (!email.isEmpty()) {
				loginRegisteredUser(session, datastore, email, resp);
			} else {
				resp.sendRedirect("/account-login.jsp");
			}

			// 2 signup
		} else if ("signup".equals(form)) {
			// Tiny bit of error handling on email...
			if (!email.isEmpty()) {
				Entity user = new Entity("User");
				user.setProperty("email", email);
				user.setProperty("displayName", req.getParameter("displayName"));
				user.setProperty("photoUrl", req.getParameter("photoUrl"));
				setUserSession(user, session);
				datastore.put(user);
				resp.sendRedirect("/accountchooser-store.jsp");
			} else {
				resp.sendRedirect("/account-create.jsp");
			}

			// 3 accountchooser status
		} else if (!email.isEmpty()) {
			// looks like we got a user status request from accountchooser.com
			resp.setContentType("application/json");
			Entity user = getUser(datastore, email);
			String json;
			if (user != null) {
				json = "{\"registered\": true}";
			} else {
				json = "{\"registered\": false}";
				// TODO json = "{\"authUri": "http://primco.org"}";
			}
			PrintWriter out = resp.getWriter();
			out.print(json);
			out.flush();
		}
	}

	private void loginRegisteredUser(HttpSession session,
			DatastoreService datastore, String email, HttpServletResponse resp)
			throws IOException {
		Entity user = getUser(datastore, email);
		if (user != null) {
			setUserSession(user, session);
			// The user wasn't registered and we don't have fancy form error
			// handling.
		} else {
			resp.sendRedirect("/account-create.jsp");
		}
		resp.sendRedirect("/accountchooser-store.jsp");
	}

	private Entity getUser(DatastoreService datastore, String email) {
		Query query = new Query("User");
		query.setFilter(new FilterPredicate("email",
				Query.FilterOperator.EQUAL, email));
		return datastore.prepare(query).asSingleEntity();
	}

	private void setUserSession(Entity user, HttpSession session) {
		session.setAttribute("email", user.getProperty("email"));
		session.setAttribute("displayName", user.getProperty("displayName"));
		session.setAttribute("photoUrl", user.getProperty("photoUrl"));
	}

	public static String getUserIdentifier(HttpSession session) {
		String email = (String) session.getAttribute("email");
		String displayName = (String) session.getAttribute("displayName");
		// Prefer the displayName over id if available, but both could be null.
		return (null == displayName || "".equals(displayName)) ? email
				: displayName;
	}

	public static String getPhotoTag(String url) {
		return "".equals(url) ? "" : "<img src=" + url
				+ " style=padding-right:10>";
	}
}