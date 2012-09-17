<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="guestbook.UserServlet" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
// The page is loaded at the end of a sign-in or account creation.
// It loads the "save" feature of Account Chooser. The browser is
// redirected to accountchooser.com and has the option to store the
// currently signed in account. If the account is currently stored,
// the ac.js redirects to the homeurl location.
 
String user_id = (String) session.getAttribute("user_id");
String name = (String) session.getAttribute("name");
String photo = (String) session.getAttribute("photo");
// Don't bother trying to store the account if there's no user_id.
if (user_id == null || user_id.isEmpty()) {
  response.sendRedirect("/guestbook.jsp");
}
%>

<html>
<head>

<!-- Begin AC integration -->
<script type="text/javascript" src="https://www.accountchooser.com/ac.js">
storeAccount: {
  // After the storeAccount method, redirect to homeUrl"
  homeUrl: '/guestbook.jsp',
  email: '<%= user_id %>', 
  displayName: '<%= name %>', 
  photoUrl: '<%= photo %>'
  } 
</script>
<!-- End AC integration -->

</head>
<body>
</body>
</html>