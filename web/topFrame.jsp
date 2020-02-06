<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Top Frame</title>
</head>
<body bgcolor="#d8d8ff" >
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
        <table>
            <tr>
                <td height="20%" align="center" width="25%" style="font-weight:700; color: grey;">
                    <%
                        if(isAdmin) {
                    %>
                        <img style="cursor: pointer;" border="0"  src="/img/home.png" width="17.5%" height="10%"  onclick="adminForwardHome()">
                    <%
                    } else {
                    %>
                        <img style="cursor: pointer;" border="0"  src="/img/home.png" width="17.5%" height="10%" onclick="forwardHome()">
                    <%
                        }
                    %>
                    <div style="vertical-align: center">&nbsp;HOME</div>
                </td>

                <td  height="20%" width="25%" align="center" style="font-weight:700; color: grey;">
                    <img style="cursor: pointer;" border="0"  src="/img/profile.png" width="17.5%" height="10%" onclick="profilePopUp()">
                    <div style="vertical-align: center">&nbsp;PROFILO</div>

                </td>


                <td  height="20%" width="25%" align="center" style="font-weight:700; color: grey;">
                    <a href=/Logout target="mainFrame">
                        <img border="0"  src="/img/logout.png" width="17.5%" height="10%">
                    </a>
                    <div style="vertical-align: center">&nbsp;LOGOUT</div>
                </td>

                <td  height="20%" width="25%" align="center" style="font-weight:700; color: grey;">
                    <a href=../docs/help.pdf target="mainFrame">
                        <img border="0"  src="/img/help.png" width="17.5%" height="10%">
                    </a>
                    <div style="vertical-align: center">&nbsp;INFO</div>
                </td>
            </tr>
            <tr>
                <td colspan="4"></td>
                <td colspan="4"></td>
                <td colspan="4"></td>
                <td colspan="4"></td>
            </tr>
        </table>
</body>
<script src="js/login.js" type="text/javascript"></script>
</html>
