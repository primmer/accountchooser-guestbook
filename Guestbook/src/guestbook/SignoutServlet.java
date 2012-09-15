package guestbook;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class SignoutServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		HttpSession session = req.getSession(true);
		session.invalidate();
		String previous = req.getHeader("Referer");
		if (null == previous) {
			resp.sendRedirect("/guestbook.jsp");
		}
		resp.sendRedirect(previous);
	}
}