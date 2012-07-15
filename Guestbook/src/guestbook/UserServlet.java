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
		String email = req.getParameter("email");

		if ("login".equalsIgnoreCase(form)) {
			// Tiny bit of error handling on email...
			if (email != null && !email.isEmpty()) {
				Entity user = getUser(datastore, email);
				setUserSession(user, session);
		    } else {
		    	resp.sendRedirect("/account-login.jsp");
		    }
		} else if ("signup".equalsIgnoreCase(form)) {
			// Tiny bit of error handling on email...
			if (email != null && !email.isEmpty()) {
				Entity user = new Entity("User");
				user.setProperty("email", email);
				user.setProperty("nickName", req.getParameter("nickName"));
				user.setProperty("photoUrl", req.getParameter("photoUrl"));
				setUserSession(user, session);
				datastore.put(user);
				setUserSession(user, session);
		    } else {
		    	resp.sendRedirect("/account-create.jsp");
		    }			
		} 
		
		resp.sendRedirect("/guestbook.jsp");
	}

	private Entity getUser(DatastoreService datastore, String email) {
		Query query = new Query("User");
		query.setFilter(new FilterPredicate("email",
				Query.FilterOperator.EQUAL, email));
		return datastore.prepare(query).asSingleEntity();
	}

	private void setUserSession(Entity user, HttpSession session) {
		session.setAttribute("email", user.getProperty("email"));
		session.setAttribute("nickName", user.getProperty("nickName"));
		session.setAttribute("photoUrl", user.getProperty("photoUrl"));
	}
	
	public static String getUserIdentifier(HttpSession session) {
		String email = (String) session.getAttribute("email");
		String nickName = (String) session.getAttribute("nickName");
		// Prefer the nickName over id if available, but both could be null.
		return "".equals(nickName) ? email: nickName;
	}
	
	public static String getPhotoTag(String url) {
		return "".equals(url) ? "" : "<img src=" + url + " style=padding-right:10>";
	}
}