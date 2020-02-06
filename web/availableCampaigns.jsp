<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Progetto.Campagna" %>
<%@ page import="java.util.ArrayList" %>

<html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Campagne a cui ci si può iscrivere</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
    <link rel="stylesheet" type="text/css" href="css/jqueryDataTables.css">
</head>
<body>
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
<br>
<table width="100%">
    <tr>
        <td width="5%"></td>
        <td width="70%"><p class="FontGrandeGrassetto">Campagne a cui NON sei iscritto:</p></td>
        <td width="25%"></td>
    </tr>
    <tr><td colspan="3">&nbsp;</td></tr>
    <tr><td colspan="3">&nbsp;</td></tr>
    <tr><td colspan="3">&nbsp;</td></tr>
    <tr>
        <td></td>
        <td>
            <table id="availableCampaignsTable" class="display" cellspacing="2px" width="100%" align="center">
                <thead>
                <tr bgcolor="#b3b3ff" class="FontMedioGrassetto">
                    <td width="30%" align="center">NOME</td>
                    <td width="30%" align="center">DATA INIZIO</td>
                    <td width="30%" align="center">DATA FINE</td>
                    <td width="10%" align="center"></td>
                </tr>
                </thead>
                <tbody>
                <%
                    ArrayList<Campagna> array = (ArrayList<Campagna>) session.getAttribute("notSubbed");
                    int i=0;
                    for(Campagna temp : array){
                        String nome = temp.getNome();
                        String inizio = temp.getDataInizio();
                        String fine = temp.getDataFine();
                        String ID = temp.getId();
                %>
                <tr>
                    <td align="center"><%=nome%></td>
                    <td align="center"><%=inizio%></td>
                    <td align="center"><%=fine%></td>
                    <td align="center"><button type="button" class="DetailsButton" onclick=Sub(<%=ID%>)>Subscribe</button></td>
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
<script src="js/userHome.js" type="text/javascript"></script>
<script src="js/jquery-3.1.1.js" type="text/javascript"></script>
<script src="js/jquerydataTables.js" type="text/javascript"></script>
<script>
    $(document).ready(function() {
        $('#availableCampaignsTable').DataTable();
    } );
</script>
</html>
