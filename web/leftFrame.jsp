<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Left Frame</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
</head>
<body bgcolor="#e8eef4">
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
<br><br><br><br>
    <table>
        <%
            if(isAdmin == true) {
        %>
            <tr><td style="font-size:7vw;cursor: pointer; font-weight:700; color: #8787ff;">-&nbsp;<a onclick="adminForwardHome()" TARGET="mainFrame" style="font-size:7vw;text-decoration: underline; font-weight:700; color: #8787ff;">Consulta Campagne</a></td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td style="font-size:7vw; font-weight:700; color: #8787ff;">-&nbsp;<a href="configuratoreUtenti.jsp" TARGET="mainFrame" style="font-size:7vw; font-weight:700; color: #8787ff;">Configuratore Utenti</a></td></tr>
        <%
        } else {
        %>
            <tr><td style="font-size:7vw; font-weight:700; color: #8787ff;">-&nbsp;<a href="availableCampaigns.jsp" TARGET="mainFrame"  style="font-size:7vw; font-weight:700; color: #8787ff;">Cerca Campagne Aperte</a></td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td style="font-size:7vw; font-weight:700; color: #8787ff;">-&nbsp;<a href="home.jsp" TARGET="mainFrame" style="font-size:7vw; font-weight:700; color: #8787ff;">Le Tue Campagne</a></td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td style="font-size:7vw; font-weight:700; color: #8787ff;">-&nbsp;<a href="closedCampaigns.jsp" TARGET="mainFrame" style="font-size:7vw; font-weight:700; color: #8787ff;">Campagne Chiuse</a></td></tr>

        <%
            }
        %>
    </table>
</body>
<script src="js/login.js" type="text/javascript"></script>
</html>
