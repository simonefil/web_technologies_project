<%@ page import="Progetto.Campagna" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<link rel="stylesheet" type="text/css" href="css/utils.css">
</head>
<body onload="status()">
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    boolean canBeStarted = false;
    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
<form action="UploadFile" id="uploadForm" method="post" enctype="multipart/form-data">
    <table width="100%">
        <tr>
            <td width="5%" align="center">&nbsp;</td>
            <td width="17.5%" align="center">&nbsp;</td>
            <td width="17.5%" align="center">&nbsp;</td>
            <td width="17.5%" align="center">&nbsp;</td>
            <td width="17.5%" align="center">&nbsp;</td>
            <td width="25%" align="center">&nbsp;</td>
        </tr>
        <tr>
            <td align="center"></td>
            <td colspan="4" align="center"><p class="FontGrandeGrassetto" >Dettagli Campagna</p></td>
            <td align="center"></td>
        </tr>
        <tr><td colspan="6">&nbsp;</td></tr>
        <tr><td colspan="6">&nbsp;</td></tr>

        <tr>
            <td></td>
            <td colspan="4" align="center" class="FontMedioGrassetto">Nome Campagna:
                <%
                    Date todayDate = new Date();
                    String id = session.getAttribute("idCampagna").toString();
                    ArrayList<Campagna> array =(ArrayList<Campagna>) session.getAttribute("resultSet");
                    int i =0;
                    String nome="";
                    String ID="";
                    int stato = -1;
                    for(Campagna temp : array){
                        ID = temp.getId();
                        if(ID.equalsIgnoreCase(id)) {
                            stato = temp.getStato();
                            nome = temp.getNome();
                            String inizio = temp.getDataInizio();
                            String fine = temp.getDataFine();
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = format.parse(inizio);
                            if(todayDate.after(date)){
                                canBeStarted = true;
                            }
                            break;
                        }
                    }
                %>
                <%="  "+nome%>
            </td>
            <td ></td>
        </tr>

        <tr>
            <td align="center"></td>
            <td colspan="4" align="center" class="FontMedioGrassetto"><div id="status"></div></td>
            <td align="center"></td>
        </tr>

        <tr><td colspan="6">&nbsp;</td></tr>
        <tr><td colspan="6">&nbsp;</td></tr>
        <tr><td colspan="6">&nbsp;</td></tr>
        <tr>
            <td>&nbsp;</td>
            <td colspan="4" align="center">
                <img style="visibility: hidden; position: absolute" border="0"  src="/img/loading.gif" width="121px" height="19px" id="waitImg">
            </td>
            <td>&nbsp;</td>
        </tr>

        <tr id="uploadRow1">
            <td align="center"></td>
            <td align="center"></td>
            <td align="center" class="FontMedioGrassetto">
                <label style="width: 70%" for="upload" class="<%if(stato == 2 || stato == 1){out.print("DisabledDetailsButton");} else {out.print("DetailsButton");}%>">Sfoglia File</label>
                <input onchange="javascript:changeFileName();" type="file" name="file" id="upload" <%if(stato == 2 || stato == 1){out.print("disabled");}%>>
            </td>
            <td align="center" class="FontMedioGrassetto">Da annotare: <input type="checkbox" name="daAnnotare" id="checkbox" value="true" <%if(stato == 2 || stato == 1){out.print("disabled");}%>></td>
            <td align="center"></td>
            <td align="center"></td>
        </tr>

        <tr id="uploadRow2">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td align="center" class="FontPiccoloGrassetto" id="fileName">

            </td>
            <td colspan="3">&nbsp;</td>
        </tr>
        <tr id="uploadRow3">
            <td align="center"></td>
            <td align="center"></td>
            <td colspan="2" align="center">
                <input style="text-align: center" class="<%if(stato == 2 || stato == 1){out.print("DisabledDetailsButton");} else {out.print("DetailsButton");}%>" onclick="showAttesa();" id="submitBtn" value="Upload" <%if(stato == 2 || stato == 1){out.print("disabled");}%>>
            </td>
            <td align="center"></td>
            <td align="center"></td>
        </tr>

        <tr><td colspan="3">&nbsp;</td></tr>
        <tr><td colspan="3">&nbsp;</td></tr>
        <tr><td colspan="3">&nbsp;</td></tr>
        <tr><td colspan="3">&nbsp;</td></tr>

    </table>
</form>

<form action="StartCampaign" method="get">
    <table width="100%">
        <tr>
            <td width="5%" align="center"></td>
            <td width="17.5%" align="center"></td>
            <td width="35%" align="center"><input class="<%if(stato == 1 || stato == 2 || canBeStarted==false){out.print("DisabledDetailsButton");} else {out.print("DetailsButton");}%>" type="submit" id="start" value="Avvia Campagna" <%if(stato == 1 || stato == 2 || canBeStarted==false){out.print("disabled");}%>></td>
            <td width="17.5%" align="center"></td>
            <td width="25%" align="center"></td>
        </tr>

        <tr><td colspan="3">&nbsp;</td></tr>

    </table>
</form>

<form action="CloseCampaign" method="get">
    <table width="100%">
        <tr>
            <td width="5%" align="center"></td>
            <td width="17.5%" align="center"></td>
            <td width="35%" align="center"><input class="<%if(stato == 0 || stato == 2){out.print("DisabledDetailsButton");} else {out.print("DetailsButton");}%>" type="submit" id="close" value="Chiudi Campagna" <%if(stato == 0 || stato == 2){out.print("disabled");}%>></td>
            <td width="17.5%" align="center"></td>
            <td width="25%" align="center"></td>
        </tr>

        <tr><td colspan="3">&nbsp;</td></tr>
    </table>
</form>

<tr><td colspan="3">&nbsp;</td></tr>
<tr><td colspan="3">&nbsp;</td></tr>

<table width="100%">
    <tr>
        <td width="5%" align="center"></td>
        <td width="17.5%" align="center"></td>
        <td width="35%" align="center">
            <input class="<%if(stato == 0){out.print("DisabledDetailsButton");} else {out.print("DetailsButton");}%>" type="submit" id="map" value="Visualizza Mappa" onclick="mapForward(<%=ID%>)" <%if(stato == 0){out.print("disabled");}%>>
            <input type="hidden" name="campagnaStato" id="campagnaStato" value="<%=stato%>">
        </td>
        <td width="17.5%" align="center"></td>
        <td width="25%" align="center"></td>
    </tr>
    <tr><td colspan="5">&nbsp;</td></tr>
    <tr><td colspan="5">&nbsp;</td></tr>
    <tr>
        <td align="center"></td>
        <td align="center"></td>
        <td align="center">
            <input class="<%if(stato == 0){out.print("DisabledDetailsButton");} else {out.print("DetailsButton");}%>" type="submit" id="stat" value="Visualizza Statistiche" onclick="statForward(<%=ID%>)" <%if(stato == 0){out.print("disabled");}%>>
            <input type="hidden" name="campagnaDesc" id="campagnaDesc" value="<%=nome%>">
            <input type="hidden" name="campagnaId" id="campagnaId" value="<%=id%>">
        </td>
        <td align="center"></td>
        <td align="center"></td>
    </tr>
</table>

</body>
<script src="js/campaignStatus.js" type="text/javascript"></script>
</html>
