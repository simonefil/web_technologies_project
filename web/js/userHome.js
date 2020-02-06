function Sub(id) {  //parametro in ingresso l'id della campagna a cui l'utente lo vuole
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = xhr.responseText;
            window.location.href="../home.jsp"
        }
    }
    xhr.open('GET', 'Subscribe'+'?'+'id='+id, true);
    xhr.send(null);
}

function Details(id) {  //apre la mappa per lo user
   self.location.href="../campaignInfo.html?x=" + id +"&provenienza=lavoratore";
}
