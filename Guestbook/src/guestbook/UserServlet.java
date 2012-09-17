package guestbook;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;


@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		HttpSession session = req.getSession(true);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		String form = req.getParameter("form");
		String user_id = req.getParameter("user_id");

		if ("login".equals(form)) {
			// Tiny bit of error handling on user_id...
			if (!user_id.isEmpty()) {
				loginRegisteredUser(session, datastore, user_id, resp);
			} else {
				// no user_id provided and no fancy form error handling.
				resp.sendRedirect("/account-login.jsp");
			}
		} else if ("signup".equals(form)) {
			// Tiny bit of error handling on user_id...
			if (!user_id.isEmpty()) {
				Entity user = new Entity("User");
				user.setProperty("user_id", user_id);
				user.setProperty("name", req.getParameter("name"));
				user.setProperty("photo", req.getParameter("photo"));
				datastore.put(user);
				loginRegisteredUser(session, user, resp);
			} else {
				// no user_id provided and no fancy form error handling.
				resp.sendRedirect("/account-create.jsp");
			}
		}
	}

	private void loginRegisteredUser(HttpSession session,
			DatastoreService datastore, String user_id, HttpServletResponse resp)
			throws IOException {
		Entity user = getUser(datastore, user_id);
		loginRegisteredUser(session, user, resp);
	}

	private void loginRegisteredUser(HttpSession session,
			Entity user, HttpServletResponse resp) throws IOException {
		if (user != null) {
			setUserSession(user, session);
			// At the end of a login, always attempt to store the account in
			// accountchooser.
			resp.sendRedirect("/accountchooser-store.jsp");
		} else {
			// The user wasn't registered and we don't have fancy form error
			// handling. Don't store, try to create an account.
			resp.sendRedirect("/account-create.jsp");
		}
	}
	
	private Entity getUser(DatastoreService datastore, String user_id) {
		Query query = new Query("User");
		query.setFilter(new FilterPredicate("user_id",
				Query.FilterOperator.EQUAL, user_id));
		return datastore.prepare(query).asSingleEntity();
	}

	private void setUserSession(Entity user, HttpSession session) {
		session.setAttribute("user_id", user.getProperty("user_id"));
		session.setAttribute("name", user.getProperty("name"));
		session.setAttribute("photo", user.getProperty("photo"));
	}

	public static String getUserIdentifier(HttpSession session) {
		String user_id = (String) session.getAttribute("user_id");
		String name = (String) session.getAttribute("name");
		// Prefer the name over id if available, but both could be null.
		return (null == name || "".equals(name)) ? user_id : name;
	}

	public static String getPhotoTag(String url) {
		return "".equals(url) ? "" : "<img src=" + url
				+ " style=padding-right:10>";
	}
}