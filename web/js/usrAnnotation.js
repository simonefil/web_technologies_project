

function toogleValido (element) { //checkbox dell utente quando deve inserire l'annotazione
    if (element.checked) {
        document.getElementById('nonvalido').checked = false;  //checkbox esclusivi
        document.getElementById('form').style.display = 'block'; //mostra la from per inserire i dati se è checkato il valido
    }
    if (!element.checked) document.getElementById('form').style.display = 'none';

}

function toogleNonValido (element) {
    if (element.checked) {
        document.getElementById('valido').checked = false;
        document.getElementById('form').style.display = 'none';
    }
}

function getUrlParams() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function submit() {
    var validita;
    var url;
    //var picco = getUrlParams()["picco"];
    debugger;
    var piccoId = document.getElementById("piccoId").value;
    var piccoNome = document.getElementById("piccoNome");
    var piccoNomiLocalizzati = document.getElementById("piccoNomiLocalizzati");
    var piccoElevazione = document.getElementById("piccoElevazione");
    if (document.getElementById('valido').checked == true){
        if(controllaCampiAnnotazione()){
            validita = true;
            var elevazione = document.getElementById('elevazione').value;
            var nome = document.getElementById('nome').value;
            var nomi_localizzati = formatNomiLocalizzati();
            url = 'AddAnnotation'+'?'+'validita='+validita+'&picco='+piccoId+'&elevazione='+elevazione+'&nome='+encodeURIComponent(nome)+'&nomi_localizzati='+encodeURIComponent(nomi_localizzati);   //gestione UTF/8
        } else {
            return false;
        }
    }
    else {  //se è checkato non valido
        validita = false;
        url = 'AddAnnotation'+'?'+'validita='+validita+'&picco='+piccoId;
    }

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = callback;
    xmlhttp.open('POST', url, true);
    xmlhttp.send(null);
    function callback() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var response =  xmlhttp.responseText;
            if (response == "1")
                window.location.href = "../home.jsp";
            if (response == "0")
                window.location.href = "../home.jsp?alreadyIn=true";    //se il campo è valorizzato da alert sulla jsp che era già presente un'annotazione
            if (response == "-1")
                window.location.href = "../home.jsp?daAnnotare=false";   //se il campo è valorizzato da alert sulla jsp che il picco non era da annotare
        }
    }
}

function controllaCampiAnnotazione(){
    var nomi=document.getElementsByName("nomi_localizzati");


    if(document.getElementById('elevazione').value == ""){
        alert("Elevazione non compilata");
        return false;
    }
    if ( /[^0-9./]/.test(document.getElementById('elevazione').value)) {     //elevazione solo con numeri e il carattere .
        alert("Elevazione puo' essere solo numerica (Usare il punto per i decimali)");
        return false;
    }

    if(document.getElementById('elevazione').value.length > 8 ){   // 5 numeri a sinistra del punto, il punto e due cifre decimali
        alert("Elevazione troppo grande");
        return false;
    }

    if(document.getElementById('nome').value == "" ){
        alert("Nome non compilato");
        return false;
    }

    if(document.getElementById('nome').value.length > 30 ){
        alert("Nome troppo lungo");
        return false;
    }

    for(var i=0; i <nomi.length;i++){
        if(nomi[i].value == ""){
            alert("Nomi Localizzati non compilati");
            return false;
        }

        if(nomi[i].value.length > 30){
            alert("Nome Localizzato troppo lungo");
            return false;
        }

    }

    return true;

}



function addRow() { //aggiugne una riga ai nomi localizzati

    var tableInput = document.getElementById("tableInput");
    var rowCount = tableInput.rows.length;
    var cloneInputRow = document.getElementById('rowInput').cloneNode(true);
    var clonePlusRow = document.getElementById('rowPlus').cloneNode(true);
    tableInput.deleteRow(rowCount -1);
    tableInput.appendChild(cloneInputRow);
    tableInput.appendChild(clonePlusRow);

    var lastCell = document.getElementsByName("lastCell");
    lastCell[lastCell.length-1].innerHTML="<img style=\"cursor: pointer;\" border=\"0\"  src=\"/img/redCross.jpg\" width=\"25px\" height=\"25px\" onclick=\"deleteRow(this.parentNode.parentNode.rowIndex)\">";

    var lastNome = document.getElementsByName("nomi_localizzati");
    lastNome[lastNome.length-1].value="";
}

function deleteRow(i){
    document.getElementById('tableInput').deleteRow(i)
}

function formatNomiLocalizzati() {
    var nomi_localizzati="";
    var nomi = document.getElementsByName("nomi_localizzati");
    var lingue = document.getElementsByName("selectLang");

    for(var i=0; i <nomi.length;i++){
        nomi_localizzati+=nomi[i].value;
        nomi_localizzati+="(";
        nomi_localizzati+=lingue[i].value;
        nomi_localizzati+=")";
        if(i!=nomi.length-1)
            nomi_localizzati+=", ";
    }

    return nomi_localizzati;
}

function backToMap(idCampaign) {
    self.location.href="../campaignInfo.html?x=" + idCampaign +"&provenienza=lavoratore";
}