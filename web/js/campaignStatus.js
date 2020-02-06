function status() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = callback;
    xmlhttp.open("GET", "GetCampaignStatus", true);
    xmlhttp.send(null);
    function callback() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
          document.getElementById('status').innerHTML = "Stato: "+ xmlhttp.responseText;
        }
    }
}

function mapForward(id) {   //quando premo su "Visualizza Mappa"    parametro in ingresso è l'id della campgna
    var stato = document.getElementById('campagnaStato').value;
    self.location.href="../viewMapAdmin.html?x=" + id + "&stato="+stato+ "&provenienza=manager";
}

function statForward(id) {  //quando premo su "Visualizza Statistiche" parametro in ingresso è l'id della campgna
    var nome = document.getElementById("campagnaDesc").value;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = callback;
    xmlhttp.open('GET', 'GetCampaignInfo' + '?' + 'id=' + id + '&' + 'operation=statistics', true);
    xmlhttp.send(null);
    function callback() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            self.location.href="../stats.jsp?campagna=" + id+"&nome="+nome;
        }
    }
}

function changeFileName() { // mette il nome del file selezionato sotto il bottone sfoglia file
   var fileName = document.getElementById("upload").value;
    fileName = fileName.replace(/.*[\/\\]/, '');
    document.getElementById("fileName").innerHTML=fileName;
}

function hideElement(idDesc){   //funzione che nasconde l'oggetto che ha come id il parametro in ingresso
    var element = document.getElementById(idDesc);
    element.style.visibility = 'hidden';
    element.style.position = 'absolute';
}

function showElement(idDesc) {   //funzione che mostra l'oggetto che ha come id il parametro in ingresso
    var element = document.getElementById(idDesc);
    element.style.visibility = '';
    element.style.position = '';
}

function showAttesa() {     //quando premo su upload mostro la gif di caricamento
    hideElement("uploadRow1");
    hideElement("uploadRow2");
    hideElement("uploadRow3");
    showElement("waitImg");
    document.getElementById("uploadForm").submit();
}