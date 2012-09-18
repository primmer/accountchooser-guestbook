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
 %>
 
<html>
<head>

<!-- Begin AC integration -->
<script type="text/javascript" src="https://www.accountchooser.com/ac.js">
loginUrl: '/account-login.jsp',
signupUrl: '/account-create.jsp',
homeUrl: '/guestbook.jsp',
userStatusUrl: '/userstatus',
siteEmailId: 'user_id'
</script>
<!-- End AC integration -->

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
		This login page has the <a href="http://code.google.com/p/accountchooser/source/browse/accountchooser.com/ac.debug.js">
		accountchooser javascript</a>
		added to it. If you haven't used Account Chooser before, you will not
		see the account selector from accountchooser.com, when this page loads.
		You can use use <a
			href="https://ac-cribwars.appspot.com/">this link</a> to log into a sample site with a federated
		ldentity, which will populate the account store. Or, you can sign in 
		with an existing account.
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