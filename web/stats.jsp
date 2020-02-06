<%@ page import="Progetto.Campagna" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Progetto.Picco" %>
<%@ page import="Progetto.Annotazione" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="css/utils.css">
    <link rel="stylesheet" type="text/css" href="css/jqueryDataTables.css">
</head>
<script type="text/javascript">
    function toggle_by_class(cls, nomePicco, tabellaPadre, tabellaFiglia) {
        var tableFigliaDiv = document.getElementById(tabellaFiglia+'Div');
        var tablePadreDiv = document.getElementById(tabellaPadre+'Div');
        var tableFiglia = document.getElementById(tabellaFiglia);
        var rows = tableFiglia.getElementsByTagName("tr");
        tablePadreDiv.style.visibility = 'hidden';
        tablePadreDiv.style.position = 'absolute';

        tableFigliaDiv.style.visibility = '';
        tableFigliaDiv.style.position = '';


        for(var z = 1; z < rows.length; z++) {
            if(rows[z].className == cls+' odd' || rows[z].className == cls+' even'){    //gestione datatable jquery
                rows[z].style.visibility = '';
                rows[z].style.position = '';
            } else {
                rows[z].style.visibility = 'hidden';
                rows[z].style.position = 'absolute';
            }
        }

        var divTitolo = document.getElementById("titoloImg");
        divTitolo.style.visibility = '';
        divTitolo.style.position = '';
        var titolo = document.getElementById("Titolo");
        titolo.innerText=nomePicco;

        var backImg = document.getElementById("backImg");
        backImg.style.visibility = '';
        backImg.style.position = '';

        //nasconde la scritta "showing 1 of X entities" nella datatable perchè alcune sono nascoste e il numero non sarebbe quindi corretto
        var annotazioniDataTable = document.getElementById(tabellaFiglia+'_info');
        annotazioniDataTable.style.visibility = 'hidden';
        annotazioniDataTable.style.position = 'absolute';
    }

</script>
<body>
<%
    Boolean isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
    String user = (String) request.getSession().getAttribute("user");
    if(isAdmin==null || user==null) //controllo sulla session
        throw new IllegalArgumentException("Devi effettuare il login");

    String campagnaDesc = request.getParameter("nome");
    String campagnaId = request.getParameter("campagna");
    Campagna campagna = (Campagna) session.getAttribute("CAMPAGNA");

%>
<table width="100%">
    <tr>
        <td width="5%">&nbsp;</td>
        <td width="17.5%">&nbsp;</td>
        <td width="17.5%">&nbsp;</td>
        <td width="17.5%">&nbsp;</td>
        <td width="17.5%">&nbsp;</td>
        <td width="25%">&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td colspan="4"><p class="FontGrandeGrassetto" >Statistiche Campagna - <%=campagnaDesc%></p></td>
        <td class="FontMedioGrassetto"><img style="cursor: pointer;" border="0"  src="/img/back.png" width="50px" height="50px" onclick="backToDetails();"><div style="align: center">BACK</div></td>
    </tr>

    <tr><td colspan="6">&nbsp;</td></tr>
    <tr><td colspan="6">&nbsp;</td></tr>

    <tr>
        <td>&nbsp;</td>
        <td align="center"><button type="button" id="picchiAnnotatiButton" class="SelectedDetailsButton" onclick="javascript:PicchiAnnotati()">Picchi Annotati</button></td>
        <td align="center"><button type="button" id="picchiNonAnnotatiButton" class="DetailsButton" onclick="javascript:PicchiNonAnnotati()">Picchi non Annotati</button></td>
        <td align="center"><button type="button" id="annotazioniRifiutateButton" class="DetailsButton" onclick="javascript:AnnotazioniRifiutate()">Annotazioni Rifiutate</button></td>
        <td align="center"><button type="button" id="conflittiButton" class="DetailsButton" onclick="javascript:Conflitti()">Conflitti</button></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td align="center" class="FontMedioGrassetto" id="numeroPicchiAnnotati">&nbsp;</td>
        <td align="center" class="FontMedioGrassetto" style="color: #bfbfbf"  id="numeroPicchiNonAnnotati">&nbsp;</td>
        <td align="center" class="FontMedioGrassetto" style="color: #bfbfbf"  id="numeroPicchiRifiutati">&nbsp;</td>
        <td align="center" class="FontMedioGrassetto" style="color: #bfbfbf"  id="numeroConflitti">&nbsp;</td>
        <td>&nbsp;</td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td colspan="4" class="FontGrandeGrassetto"><img style="visibility: hidden; position: absolute" border="0"  src="/img/mountain.png" width="25px" height="25px" id="titoloImg"></td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td align="middle"><img style="cursor: pointer; visibility: hidden; position: absolute" border="0"  src="/img/back.png" width="25px" height="25px" id="backImg" onclick="javascript:returnBack()"></td>
        <td colspan="2" class="FontGrandeGrassetto" id="Titolo">Picchi con almeno un'Annotazione</td>
        <td colspan="2">&nbsp;</td>
    </tr>
    <tr><td colspan="6">&nbsp;</td></tr>

    <tr>
        <td>&nbsp;</td>
        <td colspan="4">
            <div align="center" name="picchiAnnotati" id="picchiAnnotati">
                <div id="picchiAnnotatiTableDiv">
                    <table id="picchiAnnotatiTable" class="display" cellspacing="2px" width="100%" align="center">
                     <thead>
                        <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                            <td width="20%" align="center">NOME</td>
                            <td width="20%" align="center">NOMI LOCALIZZATI</td>
                            <td width="15%" align="center">PROVENIENZA</td>
                            <td width="10%" align="center">ELEVAZIONE</td>
                            <td width="13%" align="center">LATITUDINE</td>
                            <td width="13%" align="center">LONGITUDINE</td>
                            <td width="9%" align="center">N.ANNOTAZIONI</td>
                        </tr>
                     </thead>
                        <tbody>
                        <%
                            ArrayList<Picco> picchi = campagna.getPicchi();
                            int numeroPicchiAnnotati = 0;
                            for(Picco piccoTmp : picchi){
                                int numeroAnnotazioni = piccoTmp.getAnnotazioni().size();
                                if(numeroAnnotazioni>0){
                                    numeroPicchiAnnotati++;
                        %>
                                    <tr style="cursor: pointer" onclick="javascript:toggle_by_class(<%=piccoTmp.getId()%>, '<%=piccoTmp.getNome()%>', 'picchiAnnotatiTable', 'annotationTable')">
                                        <td align="center"><%=piccoTmp.getNome()==null?"":piccoTmp.getNome()%></td>
                                        <td align="center"><%=piccoTmp.getNomi_localizzati()==null?"":piccoTmp.getNomi_localizzati()%></td>
                                        <td align="center"><%=piccoTmp.getProvenienza()==null?"":piccoTmp.getProvenienza()%></td>
                                        <td align="center"><%=piccoTmp.getElevazione()==0?"":piccoTmp.getElevazione()%></td>
                                        <td align="center"><%=piccoTmp.getLatitudine()==null?"":piccoTmp.getLatitudine()%></td>
                                        <td align="center"><%=piccoTmp.getLongitudine()==null?"":piccoTmp.getLongitudine()%></td>
                                        <td align="center"><%=numeroAnnotazioni%></td>
                                    </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                    </table>
                    <%
                        if(numeroPicchiAnnotati>0) {
                    %>
                    <br>
                    <div class="FontPiccoloGrassettoGrassetto" align="left">Selezionare il picco per vedere le annotazioni associate ad esso... </div>
                    <%
                        }
                    %>
                </div>
                <div id="annotationTableDiv" style="visibility: hidden; position: absolute">
                    <table id="annotationTable" class="display" cellspacing="2px" width="100%" align="center">
                        <thead>
                        <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                            <td width="20%" align="center">USER</td>
                            <td width="20%" align="center">NOME</td>
                            <td width="20%" align="center">NOMI LOCALIZZATI</td>
                            <td width="10%" align="center">ELEVAZIONE</td>
                            <td width="17%" align="center">DATA CREAZIONE</td>
                            <td width="13%" align="center">STATO</td>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for(Picco piccoTmp : picchi){
                                ArrayList<Annotazione> annotazioniArray =  piccoTmp.getAnnotazioni();
                                if(annotazioniArray.size()>0){
                                    for(Annotazione annotazioneTemp : annotazioniArray){
                        %>
                                        <tr class="<%=piccoTmp.getId()%>" style="visibility: hidden; position: absolute">
                                            <td align="center"><%=annotazioneTemp.getUser()%></td>
                                            <td align="center"><%=annotazioneTemp.getNome()==null?"":annotazioneTemp.getNome()%></td>
                                            <td align="center">
                                                <%
                                                    if(annotazioneTemp.getValidita()==0){
                                                        out.print("NON VALIDA");
                                                    } else {
                                                        out.print(annotazioneTemp.getNomi_localizzati()==null?"":annotazioneTemp.getNomi_localizzati());
                                                    }
                                                %>
                                            </td>
                                            <td align="center"><%=annotazioneTemp.getElevazione()==0?"":annotazioneTemp.getElevazione()%></td>
                                            <td align="center"><%=annotazioneTemp.getData_creazione()%></td>
                                            <td align="center"><%=annotazioneTemp.getStatoDesc()%></td>
                                        </tr>
                        <%          }
                                }
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
            <div align="center" name="picchiNonAnnotati" id="picchiNonAnnotati" style="visibility: hidden; position: absolute">
                <table id="picchiNonAnnotatiTable" class="display" cellspacing="2px" width="100%" align="center">
                    <thead>
                    <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                        <td width="20%" align="center">NOME</td>
                        <td width="20%" align="center">NOMI LOCALIZZATI</td>
                        <td width="16%" align="center">PROVENIENZA</td>
                        <td width="10%" align="center">ELEVAZIONE</td>
                        <td width="17%" align="center">LATITUDINE</td>
                        <td width="17%" align="center">LONGITUDINE</td>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int numeroPicchiNonAnnotati = 0;

                        for(Picco piccoTmp : picchi){
                            int numeroAnnotazioni = piccoTmp.getAnnotazioni().size();
                            if(numeroAnnotazioni==0){
                                numeroPicchiNonAnnotati++;
                    %>
                    <tr>
                        <td align="center"><%=piccoTmp.getNome()==null?"":piccoTmp.getNome()%></td>
                        <td align="center"><%=piccoTmp.getNomi_localizzati()==null?"":piccoTmp.getNomi_localizzati()%></td>
                        <td align="center"><%=piccoTmp.getProvenienza()==null?"":piccoTmp.getProvenienza()%></td>
                        <td align="center"><%=piccoTmp.getElevazione()==0?"":piccoTmp.getElevazione()%></td>
                        <td align="center"><%=piccoTmp.getLatitudine()==null?"":piccoTmp.getLatitudine()%></td>
                        <td align="center"><%=piccoTmp.getLongitudine()==null?"":piccoTmp.getLongitudine()%></td>
                    </tr>
                    <%
                            }
                        }
                    %>
                    </tbody>
                </table>
            </div>
            <div align="center" name="annotazioniRifiutate" id="annotazioniRifiutate" style="visibility: hidden; position: absolute">
                <div id="picchiRifiutatiTableDiv">
                    <table id="picchiRifiutatiTable" class="display" cellspacing="2px" width="100%" align="center">
                        <thead>
                        <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                            <td width="20%" align="center">NOME</td>
                            <td width="20%" align="center">NOMI LOCALIZZATI</td>
                            <td width="15%" align="center">PROVENIENZA</td>
                            <td width="10%" align="center">ELEVAZIONE</td>
                            <td width="13%" align="center">LATITUDINE</td>
                            <td width="13%" align="center">LONGITUDINE</td>
                            <td width="9%" align="center">N.RIFIUTATE</td>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            int numeroAnnotazioniRifiutate = 0;
                            int numeroPicchiRifiutati = 0;

                            for(Picco piccoTmp : picchi){
                                numeroAnnotazioniRifiutate = 0;
                                ArrayList<Annotazione> annotationArray =  piccoTmp.getAnnotazioni();
                                for(Annotazione annotazioneTemp : annotationArray){
                                    if(annotazioneTemp.getStato()==0){ //stato = 0 --> rifiutata
                                        numeroAnnotazioniRifiutate++;
                                       }
                                }

                                if(numeroAnnotazioniRifiutate>0) {
                                    numeroPicchiRifiutati++;
                        %>
                                        <tr style="cursor: pointer" onclick="javascript:toggle_by_class(<%=piccoTmp.getId()%>, '<%=piccoTmp.getNome()%>', 'picchiRifiutatiTable', 'annotazioniRifiutateTable')">
                                            <td align="center"><%=piccoTmp.getNome()==null?"":piccoTmp.getNome()%></td>
                                            <td align="center"><%=piccoTmp.getNomi_localizzati()==null?"":piccoTmp.getNomi_localizzati()%></td>
                                            <td align="center"><%=piccoTmp.getProvenienza()==null?"":piccoTmp.getProvenienza()%></td>
                                            <td align="center"><%=piccoTmp.getElevazione()==0?"":piccoTmp.getElevazione()%></td>
                                            <td align="center"><%=piccoTmp.getLatitudine()==null?"":piccoTmp.getLatitudine()%></td>
                                            <td align="center"><%=piccoTmp.getLongitudine()==null?"":piccoTmp.getLongitudine()%></td>
                                            <td align="center"><%=numeroAnnotazioniRifiutate%></td>
                                        </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                    </table>
                    <%
                        if(numeroPicchiRifiutati>0) {
                    %>
                    <br>
                    <div class="FontPiccoloGrassettoGrassetto" align="left">Selezionare il picco per vedere le annotazioni associate ad esso... </div>
                    <%
                        }
                    %>
                </div>
                <div id="annotazioniRifiutateTableDiv" style="visibility: hidden; position: absolute">
                    <table id="annotazioniRifiutateTable" class="display" cellspacing="2px" width="100%" align="center">
                        <thead>
                        <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                            <td width="20%" align="center">USER</td>
                            <td width="20%" align="center">NOME</td>
                            <td width="20%" align="center">NOMI LOCALIZZATI</td>
                            <td width="10%" align="center">ELEVAZIONE</td>
                            <td width="17%" align="center">DATA CREAZIONE</td>
                            <td width="13%" align="center">STATO</td>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for(Picco piccoTmp : picchi){
                                ArrayList<Annotazione> annotazioniArray =  piccoTmp.getAnnotazioni();
                                if(annotazioniArray.size()>0){
                                    for(Annotazione annotazioneTemp : annotazioniArray){
                        %>
                        <tr class="<%=piccoTmp.getId()%>" style="visibility: hidden; position: absolute">
                            <td align="center"><%=annotazioneTemp.getUser()%></td>
                            <td align="center"><%=annotazioneTemp.getNome()==null?"":annotazioneTemp.getNome()%></td>
                            <td align="center"><%=annotazioneTemp.getNomi_localizzati()==null?"":annotazioneTemp.getNomi_localizzati()%></td>
                            <td align="center"><%=annotazioneTemp.getElevazione()==0?"":annotazioneTemp.getElevazione()%></td>
                            <td align="center"><%=annotazioneTemp.getData_creazione()%></td>
                            <td align="center"><%=annotazioneTemp.getStatoDesc()%></td>
                        </tr>
                        <%          }
                                }
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
            <div align="center" name="conflitti" id="conflitti" style="visibility: hidden; position: absolute">
                <div id="conflittiPicchiTableDiv">
                    <table id="conflittiPicchiTable" class="display" cellspacing="2px" width="100%" align="center">
                        <thead>
                        <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                            <td width="19%" align="center">NOME</td>
                            <td width="19%" align="center">NOMI LOCALIZZATI</td>
                            <td width="15%" align="center">PROVENIENZA</td>
                            <td width="7%" align="center">ELEVAZIONE</td>
                            <td width="11%" align="center">LATITUDINE</td>
                            <td width="11%" align="center">LONGITUDINE</td>
                            <td width="9%" align="center">ANNOTAZIONI POSITIVE</td>
                            <td width="9%" align="center">ANNOTAZIONI NEGATIVE</td>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            int numeroAnnotazioniValide = 0;
                            int numeroAnnotazioniNonValide = 0;
                            int numeroConflitti=0;
                            for(Picco piccoTmp : picchi){
                                numeroAnnotazioniValide = 0;
                                numeroAnnotazioniNonValide = 0;
                                ArrayList<Annotazione> annotationArray =  piccoTmp.getAnnotazioni();

                                for(Annotazione annotazioneTemp : annotationArray){

                                    if(annotazioneTemp.getValidita()==0)
                                        numeroAnnotazioniNonValide++;

                                    if(annotazioneTemp.getValidita()==1)
                                        numeroAnnotazioniValide++;
                                }

                                if(numeroAnnotazioniNonValide>0 && numeroAnnotazioniValide>0) { //se c'è conflitto sulle validità allora stampa il picco
                                    numeroConflitti++;
                        %>
                        <tr style="cursor: pointer" onclick="javascript:toggle_by_class(<%=piccoTmp.getId()%>, '<%=piccoTmp.getNome()%>', 'conflittiPicchiTable', 'annotazioniConflittiTable')">
                            <td align="center"><%=piccoTmp.getNome()==null?"":piccoTmp.getNome()%></td>
                            <td align="center"><%=piccoTmp.getNomi_localizzati()==null?"":piccoTmp.getNomi_localizzati()%></td>
                            <td align="center"><%=piccoTmp.getProvenienza()==null?"":piccoTmp.getProvenienza()%></td>
                            <td align="center"><%=piccoTmp.getElevazione()==0?"":piccoTmp.getElevazione()%></td>
                            <td align="center"><%=piccoTmp.getLatitudine()==null?"":piccoTmp.getLatitudine()%></td>
                            <td align="center"><%=piccoTmp.getLongitudine()==null?"":piccoTmp.getLongitudine()%></td>
                            <td align="center"><%=numeroAnnotazioniValide%></td>
                            <td align="center"><%=numeroAnnotazioniNonValide%></td>
                        </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                    </table>
                    <%
                        if(numeroConflitti>0) {
                    %>
                    <br>
                    <div class="FontPiccoloGrassettoGrassetto" align="left">Selezionare il picco per vedere le annotazioni associate ad esso... </div>
                    <%
                        }
                    %>
                </div>
                <div id="annotazioniConflittiTableDiv" style="visibility: hidden; position: absolute">
                    <table id="annotazioniConflittiTable" class="display" cellspacing="2px" width="100%" align="center">
                        <thead>
                        <tr bgcolor="#b3b3ff" class="FontPiccoloGrassetto">
                            <td width="20%" align="center">USER</td>
                            <td width="20%" align="center">NOME</td>
                            <td width="20%" align="center">NOMI LOCALIZZATI</td>
                            <td width="10%" align="center">ELEVAZIONE</td>
                            <td width="17%" align="center">DATA CREAZIONE</td>
                            <td width="13%" align="center">VALIDITA'</td>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for(Picco piccoTmp : picchi){
                                ArrayList<Annotazione> annotazioniArray3 =  piccoTmp.getAnnotazioni();
                                if(annotazioniArray3.size()>0){
                                    for(Annotazione annotazioneTemp : annotazioniArray3){
                        %>
                        <tr class="<%=piccoTmp.getId()%>" style="visibility: hidden; position: absolute">
                            <td align="center"><%=annotazioneTemp.getUser()%></td>
                            <td align="center"><%=annotazioneTemp.getNome()==null?"":annotazioneTemp.getNome()%></td>
                            <td align="center"><%=annotazioneTemp.getNomi_localizzati()==null?"":annotazioneTemp.getNomi_localizzati()%></td>
                            <td align="center"><%=annotazioneTemp.getElevazione()==0?"":annotazioneTemp.getElevazione()%></td>
                            <td align="center"><%=annotazioneTemp.getData_creazione()%></td>
                            <td align="center"><%=annotazioneTemp.getValiditaDesc()%></td>
                        </tr>
                        <%
                                    }
                                }
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </td>
        <td>&nbsp;</td>
    </tr>

</table>

<input type="hidden" id="selezione" value="1">
<input type="hidden" id="numeroPicchiAnnotatiHidden" value="<%=numeroPicchiAnnotati%>">
<input type="hidden" id="numeroPicchiNonAnnotatiHidden" value="<%=numeroPicchiNonAnnotati%>">
<input type="hidden" id="numeroPicchiRifiutatiHidden" value="<%=numeroPicchiRifiutati%>">
<input type="hidden" id="numeroConflittiHidden" value="<%=numeroConflitti%>">
<script src="js/statistics.js" type="text/javascript"></script>
<script src="js/jquery-3.1.1.js" type="text/javascript"></script>
<script src="js/jquerydataTables.js" type="text/javascript"></script>
<script>
    $(document).ready(function() {
        $('#picchiAnnotatiTable').DataTable();
        $('#annotationTable').DataTable();
        $('#picchiNonAnnotatiTable').DataTable();
        $('#picchiRifiutatiTable').DataTable();
        $('#annotazioniRifiutateTable').DataTable();
        $('#conflittiPicchiTable').DataTable();
        $('#annotazioniConflittiTable').DataTable();

        document.getElementById("numeroPicchiAnnotati").innerText = document.getElementById("numeroPicchiAnnotatiHidden").value;
        document.getElementById("numeroPicchiNonAnnotati").innerText = document.getElementById("numeroPicchiNonAnnotatiHidden").value;
        document.getElementById("numeroPicchiRifiutati").innerText = document.getElementById("numeroPicchiRifiutatiHidden").value;
        document.getElementById("numeroConflitti").innerText = document.getElementById("numeroConflittiHidden").value;

    } );

</script>
</body>
</html>