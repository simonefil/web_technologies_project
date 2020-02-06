<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="Progetto.Campagna" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Home</title>
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
        <td width="70%"><p class="FontGrandeGrassetto">LE TUE CAMPAGNE</p></td>
        <td width="25%"></td>
    </tr>
    <tr><td colspan="3">&nbsp;</td></tr>
    <tr><td colspan="3">&nbsp;</td></tr>

    <tr>
        <td width="5%">&nbsp;</td>
        <td width="70%" align="center">
                <a href=/newCampaign.html target="mainFrame">
                <img border="0"  src="/img/plus.png" width="50px" height="50px"> </a>
                <div class="FontMedioGrassetto"> <a style="align-content: center; color: black; text-decoration: none" href=/newCampaign.html target="mainFrame">Crea una nuova Campagna </a></div>

        </td>
        <td width="25%"></td>
    </tr>

    <tr><td colspan="3">&nbsp;</td></tr>

    <tr>
        <td></td>
        <td>
            <table id="campaignsTable" class="display" cellspacing="2px" width="100%" align="center">
                <thead>
                <tr bgcolor="#b3b3ff" class="FontMedioGrassetto">
                    <td width="25%" align="center">NOME</td>
                    <td width="25%" align="center">DATA INIZIO</td>
                    <td width="25%" align="center">DATA FINE</td>
                    <td width="15%" align="center">STATO</td>
                    <td width="10%" align="center"></td>
                </tr>
                </thead>
                <tbody>
                <%
                    ArrayList<Campagna> array =(ArrayList<Campagna>) session.getAttribute("resultSet");
                    int i=0;
                    if(array!=null) {
                        for(Campagna temp : array){
                            String nome = temp.getNome();
                            int stato = temp.getStato();
                            String inizio = temp.getDataInizio();
                            String fine = temp.getDataFine();
                            String ID = temp.getId();
                        %>
                <tr  <%//class="active" if(i%2==0){out.print("style=\"background-color: #e0e0eb\"");}else{out.print("style=\"background-color: #a2a2c3\"");}%>>
                    <td align="center"><%=nome%></td>
                    <td align="center"><%=inizio%></td>
                    <td align="center"><%=fine%></td>
                    <td align="center"><%if(stato==0){out.print("Creata");}else {if(stato==1){out.print("Avviata");}else {if(stato==2){out.print("Chiusa");}}}%></td>
                    <td align="center"><button type="button" class="DetailsButton" onclick=pageForward(<%=ID%>)>Dettagli</button></td>
                </tr>
                <%
                            i++;
                        }
                    }
                %>
                </tbody>
            </table>
        </td>
        <td></td>
    </tr>
</table>
</body>
<script src="js/campaignUpload.js" type="text/javascript"></script>
<script src="js/jquery-3.1.1.js" type="text/javascript"></script>
<script src="js/jquerydataTables.js" type="text/javascript"></script>
<script>
    $(document).ready(function() {
        $('#campaignsTable').DataTable();
    } );
</script>
</html>
