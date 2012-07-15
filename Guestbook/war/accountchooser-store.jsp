<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="guestbook.UserServlet" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
String email = (String) session.getAttribute("email");
String displayName = (String) session.getAttribute("displayName");
String photoUrl = (String) session.getAttribute("photoUrl");
// Don't bother trying to store the account if there's no email.
if (email == null || email.isEmpty()) {
  response.sendRedirect("/guestbook.jsp");
}
%>

<html>
<head>

<!-- Begin AC integration -->
<script type="text/javascript" src="https://www.accountchooser.com/ac.js">
storeAccount: {email: '<%= email %>', displayName: '<%= displayName %>', photoUrl: '<%= photoUrl %>'} 
</script>
<!-- End AC integration -->

</head>
<body>
</body>
</html>