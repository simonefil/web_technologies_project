<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
</head>
<body>
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    String idBackCampaign = request.getParameter("idBackCampaign");
    String piccoId = request.getParameter("piccoId")==null?"0":request.getParameter("piccoId");
    String piccoNome = request.getParameter("piccoNome")==null?"":request.getParameter("piccoNome");
    String piccoNomiLocalizzati = request.getParameter("piccoNomiLocalizzati")==null?"":request.getParameter("piccoNomiLocalizzati");
    String piccoElevazione = request.getParameter("piccoElevazione")==null?"0":request.getParameter("piccoElevazione");



    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");
%>
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
        <td colspan="4" align="center"><p class="FontGrandeGrassetto"><%=piccoNome%>&nbsp;<%=piccoElevazione%>m&nbsp;-&nbsp;<%=piccoNomiLocalizzati%></p></td>
        <td align="center"></td>
    </tr>
    <tr>
        <td align="center"></td>
        <td colspan="4" align="center"><p class="FontGrandeGrassetto">Il picco contrassegnato e' valido?</p></td>
        <td rowspan="2" class="FontMedioGrassetto"><img style="cursor: pointer;" border="0"  src="/img/back.png" width="50px" height="50px" onclick="backToMap('<%=idBackCampaign%>');"><div style="align: center">BACK</div></td>
    </tr>
    <tr><td colspan="6">&nbsp;</td></tr>
    <tr><td colspan="6">&nbsp;</td></tr>
    <tr>
        <td align="center">&nbsp;</td>
        <td align="center" colspan="2" class="FontMedioGrassetto">
            <label for="valido">
                <input style="transform: scale(1.5);" type="checkbox" id="valido" name="valido" title="valido" onchange="toogleValido(this)"> Valido </input>
            </label>
        </td>
        <td align="center" colspan="2" class="FontMedioGrassetto">
            <label for="nonvalido">
                <input style="transform: scale(1.5);" type="checkbox" id="nonvalido" name="nonvalido" title="nonvalido" onchange="toogleNonValido(this)"> Non Valido </input>
            </label>
        </td>
    </tr>

    <tr><td colspan="6">&nbsp;</td></tr>

    <tr>
        <td align="center" colspan="6">
            <div id="form" style="display: none;">
                <form action="/AddAnnotation" method ="post">
                    <table width="100%" id="tableInput">
                        <tr>
                            <td width="5%" align="center">&nbsp;</td>
                            <td width="17.5%" align="center">&nbsp;</td>
                            <td width="17.5%" align="center">&nbsp;</td>
                            <td width="17.5%" align="center">&nbsp;</td>
                            <td width="17.5%" align="center">&nbsp;</td>
                            <td width="10%" align="center">&nbsp;</td>
                            <td width="20%" align="center">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center">&nbsp;</td>
                            <td align="center" class="FontMedioGrassetto" colspan="2">Elevazione:</td>
                            <td align="center" colspan="2"><input class="inputText" type="text" name="elevazione" id="elevazione"></td>
                            <td align="left" class="FontPiccoloGrassetto">(metri)</td>
                            <td align="center">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center">&nbsp;</td>
                            <td align="center" class="FontMedioGrassetto" colspan="2">Nome:</td>
                            <td align="center" colspan="2"><input class="inputText" type="text" name="nome" id="nome"></td>
                            <td align="center" colspan="2">&nbsp;</td>
                        </tr>
                        <tr id="rowInput">
                            <td align="center">&nbsp;</td>
                            <td align="center" class="FontMedioGrassetto" colspan="2">Nome localizzato:</td>
                            <td align="center" colspan="2"><input class="inputText" type="text" name="nomi_localizzati" id="nomi_localizzati"></td>
                            <td align="left">
                                <select class="Button" name="selectLang">
                                    <option value="DE">DE</option>
                                    <option value="EN">EN</option>
                                    <option value="ES">ES</option>
                                    <option value="FR">FR</option>
                                    <option value="IT">IT</option>
                                    <option value="RU">RU</option>
                                    <option value="ZH">ZH</option>
                                </select>
                            </td>
                            <td align="center" name="lastCell">&nbsp;</td>
                        </tr>
                        <tr id="rowPlus">
                            <td align="center">&nbsp;</td>
                            <td align="center" class="FontMedioGrassetto" colspan="2"><img style="cursor: pointer;" border="0"  src="/img/plus.png" width="25px" height="25px" onclick="addRow()"></td>
                            <td align="center" colspan="4">&nbsp;</td>
                            <input type="hidden" name="piccoId" id="piccoId" value="<%=piccoId%>">
                            <input type="hidden" name="piccoNome" id="piccoNome" value="<%=piccoNome%>">
                            <input type="hidden" name="piccoNomiLocalizzati" id="piccoNomiLocalizzati" value="<%=piccoNomiLocalizzati%>">
                            <input type="hidden" name="piccoElevazione" id="piccoElevazione" value="<%=piccoElevazione%>">
                        </tr>
                    </table>
                </form>
            </div>
        </td>
    </tr>

    <tr><td colspan="6">&nbsp;</td></tr>
    <tr><td colspan="6">&nbsp;</td></tr>
    <tr><td colspan="6">&nbsp;</td></tr>

    <tr>
        <td align="center" colspan="2"></td>
        <td align="center" colspan="2"><input style="font-size: 2vw; font-weight:700;" class="DetailsButton" type="button" value="Submit" onclick="submit()"></td>
        <td align="center" colspan="2"></td>

    </tr>

</table>
<script src="js/usrAnnotation.js" type="text/javascript"></script>
</body>
</html>