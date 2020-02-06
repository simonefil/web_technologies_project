<%--
  Created by IntelliJ IDEA.
  User: Stefano
  Date: 16/11/2018
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dettagli Profilo</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
</head>
<body>
        <%

            Boolean isAdminUser = (Boolean) request.getSession().getAttribute("isAdmin");
            String user = (String) request.getSession().getAttribute("user");
            if(isAdminUser==null || user==null) //controllo sulla session
                throw new IllegalArgumentException("Devi effettuare il login");

            String username = request.getParameter("username");
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String email = request.getParameter("email");
            String isAdmin = request.getParameter("isAdmin");
        %>

        <table width="100%">
            <tr>
                <td width="50%" align="center">&nbsp;</td>
                <td width="50%" align="center">&nbsp;</td>
            </tr>

            <tr bgcolor="#e0e0eb">
                <td align="center" style="font-size: 4vw; font-weight:700;">Username:</td>
                <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="username" placeholder="Username" value="<%=username%>" disabled></td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr bgcolor="#a2a2c3">
                <td align="center" style="font-size: 4vw; font-weight:700;">Nome:</td>
                <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="nome" placeholder="Nome" value="<%=nome%>"></td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr bgcolor="#e0e0eb">
                <td align="center" style="font-size: 4vw; font-weight:700;">Cognome:</td>
                <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="cognome" placeholder="Cognome" value="<%=cognome%>"></td>
            </tr >
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr bgcolor="#a2a2c3">
                <td align="center" style="font-size: 4vw; font-weight:700;">Email:</td>
                <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="email" placeholder="Email" value="<%=email%>"></td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>

                <tr><td colspan="2">&nbsp;</td></tr>
                <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td align="center"><img style="cursor: pointer;" border="0"  src="/img/save.png" width="70px" height="70px" onclick="updateProfile()"></td>
                <td align="center"><img style="cursor: pointer;" border="0"  src="/img/exit.png" width="70px" height="70px" onclick="closeWindow()"></td>
            </tr>
        </table>

</body>
<script src="js/changeProfile.js" type="text/javascript"></script>
</html>
