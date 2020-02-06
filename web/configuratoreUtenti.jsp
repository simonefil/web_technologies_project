<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Progetto.User" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Stefano
  Date: 15/11/2018
  Time: 21:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Configuratori Utenti</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
    <link rel="stylesheet" type="text/css" href="css/jqueryDataTables.css">
</head>
<body>
<%
    Boolean isAdminUser = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    if(isAdminUser==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
<br>
<table width="100%">
    <tr>
        <td width="5%"></td>
        <td width="70%"><p class="FontGrandeGrassetto">CONFIGURATORE UTENTI</p></td>
        <td width="25%"></td>
    </tr>

    <tr><td colspan="3">&nbsp;</td></tr>
    <tr><td colspan="3">&nbsp;</td></tr>


    <tr>
        <td></td>
        <td>
            <table id="usersTable" class="display" cellspacing="2px" width="100%" align="center">
                <thead>
                <tr bgcolor="#b3b3ff" class="FontMedioGrassetto">
                    <td width="20%" align="center">USERNAME</td>
                    <td width="20%" align="center">NOME</td>
                    <td width="20" align="center">COGNOME</td>
                    <td width="25%" align="center">EMAIL</td>
                    <td width="10%" align="center">ADMIN</td>
                    <td width="5%" align="center"></td>
                </tr>
                </thead>
                <tbody>
                <%
                    ArrayList<User> array = (ArrayList<User>) session.getAttribute("usersSet");
                    int i=0;
                    for(User temp : array){
                        String username = temp.getUsername();
                        String nome = temp.getNome();
                        String cognome = temp.getCognome();
                        String email = temp.getEmail();
                        String isAdmin = temp.isAdmin();
                %>
                    <tr>
                        <td align="center" id="username<%=i%>"><%=username%></td>
                        <td align="center" id="nome<%=i%>"><%=nome%></td>
                        <td align="center" id="cognome<%=i%>"><%=cognome%></td>
                        <td align="center" id="email<%=i%>"><%=email%></td>
                        <td align="center" id="isAdmin<%=i%>"><%=isAdmin%></td>
                        <td align="center">
                            <img style="cursor: pointer;" border="0"  src="/img/modifica.png" width="25px" height="25px" onclick="modificaProfilo(<%=i%>)">
                        </td>
                    </tr>
                <%
                        i++;
                    }
                %>
                </tbody>
            </table>
        </td>
        <td></td>
    </tr>
</table>
</body>
<script src="js/changeProfile.js" type="text/javascript"></script>
<script src="js/jquery-3.1.1.js" type="text/javascript"></script>
<script src="js/jquerydataTables.js" type="text/javascript"></script>
<script>
    $(document).ready(function() {
        $('#usersTable').DataTable();
    } );
</script>
</html>
