package guestbook;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class SignGuestbookServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		HttpSession session = req.getSession(true);

		String guestbookName = req.getParameter("guestbookName");
		Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
		String content = req.getParameter("content");
		Date date = new Date();
		Entity greeting = new Entity("Greeting", guestbookKey);
		greeting.setProperty("user", UserServlet.getUserIdentifier(session));
		greeting.setProperty("photo", session.getAttribute("photo"));
		greeting.setProperty("date", date);
		greeting.setProperty("content", content);

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(greeting);

		resp.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
	}
}
