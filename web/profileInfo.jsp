<%@ page import="Progetto.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Modifica Profilo</title>
</head>
<body>
<%
    User userFull = (User) request.getSession().getAttribute("UserFull");
%>
<table width="100%">
    <tr>
        <td width="50%" align="center">&nbsp;</td>
        <td width="50%" align="center">&nbsp;</td>
    </tr>

    <tr bgcolor="#e0e0eb">
        <td align="center" style="font-size: 4vw; font-weight:700;">Nome:</td>
        <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="nome" placeholder="Nome" value="<%=userFull.getNome()%>"></td>
    </tr >
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr bgcolor="#a2a2c3">
        <td align="center" style="font-size: 4vw; font-weight:700;">Cognome:</td>
        <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="cognome" placeholder="Cognome" value="<%=userFull.getCognome()%>"></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr bgcolor="#e0e0eb">
        <td align="center" style="font-size: 4vw; font-weight:700;">Password:</td>
        <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="password" id="password" placeholder="Password"></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr bgcolor="#a2a2c3">
        <td align="center" style="font-size: 4vw; font-weight:700;">Ripeti Password:</td>
        <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="password" id="rePassword" placeholder="Ripeti Password"></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr bgcolor="#a2a2c3">
        <td align="center" style="font-size: 4vw; font-weight:700;">Email:</td>
        <td><input style="text-align: center; font-size: 4vw; width: 70%;" class="inputText" type="text" id="email" placeholder="Email" value="<%=userFull.getEmail()%>"></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr>
        <td align="center"><img style="cursor: pointer;" border="0"  src="/img/save.png" width="70px" height="70px" onclick="updateProfileAdmin()"></td>
        <td align="center"><img style="cursor: pointer;" border="0"  src="/img/exit.png" width="70px" height="70px" onclick="closeWindow()"></td>
    </tr>
</table>

</body>
<script src="js/changeProfile.js" type="text/javascript"></script>
</html>