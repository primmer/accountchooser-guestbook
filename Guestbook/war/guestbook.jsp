<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="guestbook.UserServlet" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<% 
// This page has no direct accountchooser integration. 
// It's simply the guestbook application itself.
// see /account-login.jsp for Account Chooser info.
%>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

  <body>

<%
	String guestbookName = request.getParameter("guestbookName");
    if (guestbookName == null) {
        guestbookName = "default";
    }

	if (session.getAttribute("user_id") != null) { 
%>
<p>Hello, <%= UserServlet.getUserIdentifier(session) %>! (You can <a href="/signout">sign out</a>.)</p>
<br/>
<%
    } else {
%>
<p>Hello. This is the AccountChooser Guestbook java sample application. 
</p>
<p>Once you <a href="/account-login.jsp">sign in</a> 
   you can include your name and photo with greetings you post.</p>
<%
    }
%>
<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    Query query = new Query("Greeting", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
    List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    if (greetings.isEmpty()) {
        %>
        <p>Guestbook '<%= guestbookName %>' has no messages.</p>

        <%
    } else {
        %>
        <p>Messages in Guestbook '<%= guestbookName %>'.</p>
        <table border="1"><tr><td>
        
        <%
        for (Entity greeting : greetings) {
            if (greeting.getProperty("user") == null) {
                %>
                <p>An anonymous person wrote:</p>
                <%
            } else {
                %>
                <p><%= UserServlet.getPhotoTag((String) greeting.getProperty("photo")) %>
                <b><%= greeting.getProperty("user") %></b> wrote:</p>
                <%
            }
            %>
            <blockquote><%= greeting.getProperty("content") %></blockquote>
            <%
        }
    }
%>
</td></tr>
</table>
    <form action="/sign" method="post">
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="Post Greeting" /></div>
      <input type="hidden" name="guestbookName" value="<%= guestbookName %>"/>
    </form>

    <form action="/guestbook.jsp" method="get">
      <div><input type="text" name="guestbookName" value="<%= guestbookName %>"/></div>
      <div><input type="submit" value="Switch Guestbook" /></div>
    </form>

  </body>
</html>