<%--suppress ALL --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
</head>
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
<frameset class="backgroundImage" rows="15%,*" cols="*" border="0" framespacing="0" framespacing="0">
    <frame frameborder="YES" src="topFrame.jsp" name="topFrame" scrolling="NO" noresize>
    <frameset rows="*" cols="15%, 85%" border="0" framespacing="0">
        <frame src="leftFrame.jsp" name="leftFrame" noresize>
        <%
            if(isAdmin == true) {
        %>
            <frame src="adminHome.jsp" name="mainFrame">
        <%
            } else {
        %>
            <frame src="home.jsp" name="mainFrame">
        <%
            }
        %>
    </frameset>
</frameset>


<body>
</body>
<script src="js/userHome.js" type="text/javascript"></script>
</html>