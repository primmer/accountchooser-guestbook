<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="guestbook.UserServlet" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
</head>

<body>
	<%
	if (session.getAttribute("user_id") != null) {
	// The user is already signed in. let them sign out.
    %>
	<p>
		Hello, <%= UserServlet.getUserIdentifier(session) %>! (You can <a
			href="/signout">sign out</a>.)
	</p>
	<%
		} else {
	    // we didn't come here from a sign-in formpost, display the form
	%>
	<p>
	    Welcome to the Account Chooser Guestbook java sample.
	</p>
	<p>
		Sign in with an existing account.
	</p>
	<p>
	   Or, <a href="/account-create.jsp">Sign Up</a> if you don't have a username.
	</p>
	<form name="login" action="/user" method="post">
	    <input type="hidden" name="form" value="login">
		<table>
			<tr>
				<td>User Id:</td>
				<td><input type="text" id="user_id" name="user_id"></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" id="password"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="login"></td>
			</tr>
		</table>
	</form>
	<%
    }
%>

</body>
</html>