function showAnnotation(picco) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = callback;
    xmlhttp.open("GET", "GetAnnotation?id=" + picco.ID, true);
    xmlhttp.send(null);
    function callback() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var annArr = JSON.parse(this.responseText);
            writeAnnotations(annArr,picco.Nome,picco.Longitudine,picco.Latitudine,picco.Elevazione,picco.Provenienza);
        }
    }
}

function writeAnnotations(annArr, nome, longitudine, latitudine, elevazione, provenienza) {
    var tableString = "";
    var statoCampagna = getUrlParams()["stato"];

    //parsa il json in ingresso e mostra le annotazioni in una tabella
    document.getElementById("annotations").innerHTML = "";
        tableString += "<div class=\"FontMedioGrassetto\" align=\"center\">"+nome+" ("+longitudine+" "+latitudine+") - "+elevazione+"m - "+provenienza+"</div><br>" +   //titolo sopra la tabella
        "<table id=\"annotationTable\" class=\"display\" cellspacing=\"2px\" width=\"100%\" align=\"center\">" +
        "<thead>" +
        "<tr bgcolor=\"#b3b3ff\" class=\"FontPiccoloGrassetto\">" +
        "<td width=\"20%\" align=\"center\">USERAME</td>" +
        "<td width=\"20%\" align=\"center\">DATA CREAZIONE</td>" +
        "<td width=\"7%\" align=\"center\">ELEVAZIONE</td>" +
        "<td width=\"15%\" align=\"center\">NOME</td>" +
        "<td width=\"22%\" align=\"center\">NOMI LOCALIZZATI</td>" +
        "<td width=\"10%\" align=\"center\">STATO</td>" +
        "<td width=\"5%\" align=\"center\">RIFIUTA</td>" +
        "</tr>" +
        "</thead>" +
        "<tbody>";
    for (var i = 0; i<annArr.length; i++) {     //inizio parsing array json

        var elevazione="";
        var stato = null;
        var nome="";
        var nomiLocalizzati="";

        if(annArr[i].Elevazione == null)
            elevazione="";
        else
            elevazione=annArr[i].Elevazione;

        if(annArr[i].Nome == null)
            nome="";
        else
            nome=annArr[i].Nome;

        if(annArr[i].Nomi_localizzati == null)
            nomiLocalizzati="";
        else
            nomiLocalizzati=annArr[i].Nomi_localizzati;

        if (annArr[i].Stato == false)
            stato = "RESPINTA";
        else if (annArr[i].Stato == true)
            stato = "NON RIFIUTATA";
            tableString +=
            "<tr>" +
            "<td align=\"center\">"+annArr[i].User+"</td>" +
            "<td align=\"center\">"+annArr[i].Data_creazione+"</td>" +
            "<td align=\"center\">"+elevazione+"</td>" +
            "<td align=\"center\">"+nome+"</td>" +
            "<td align=\"center\">"+nomiLocalizzati+"</td>" +
            "<td align=\"center\">"+stato+"</td>";
                if(annArr[i].Stato == true && statoCampagna=="1"){  //se l'annotazione è valida e lo stato della campagna è avviata mostra il pulsante per rifiutare l'annotazione
                    tableString +="<td align=\"center\"><img style=\"cursor: pointer;\" border=\"0\"  src=\"/img/redCross.jpg\" width=\"25px\" height=\"25px\" onclick=\"rifiuta("+annArr[i].ID+")\"></td>";
                } else {
                    tableString += "<td align=\"center\">&nbsp</td>";
                }
                tableString += "</tr>";
    }
    tableString +=
        "</tbody>" +
        "</table>";
    document.getElementById("annotations").innerHTML = tableString;
    $('#annotationTable').DataTable();  //jquery è l'equivalente di fare document.getElementById("annotationTable").DataTable();
}

function rifiuta(idAnn) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = callback;
    xmlhttp.open("GET", "RefuseAnnotation?id=" + idAnn, true);
    xmlhttp.send(null);
    function callback() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var temp = this.responseText;
            location.reload();  //refresh pagina
        }
    }
}



function getUrlParams() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function backToDetails(){
    self.location.href = "../campaignInfoAdmin.jsp";
}