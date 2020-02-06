var myArr = null;
function getUrlParams() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}
var id = getUrlParams()["x"];   //id della campagna di cui stiamo visualizzando la mappa
var provenienza = getUrlParams()["provenienza"];    //se la chiamata arriva dal lavoratore o dal manager
var xmlhttp = new XMLHttpRequest();
xmlhttp.open('GET', 'GetPeaks' + '?' + 'id=' + id + '&provenienza=' + provenienza, false);
xmlhttp.send(null);
while (xmlhttp.status != 200) {
    setTimeout(function(){}, 100);
}

if (xmlhttp.status == 200) myArr = JSON.parse(xmlhttp.responseText);


var greenDot = 'http://maps.google.com/mapfiles/ms/icons/green-dot.png';
var yellowDot = 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png';
var redDot = 'http://maps.google.com/mapfiles/ms/icons/red-dot.png';
var orangeDot = 'http://maps.google.com/mapfiles/ms/icons/orange-dot.png';

var map;
function initMap() {
    var bound = new google.maps.LatLngBounds();
    for (var z = 0; z < Object.keys(myArr).length; z++) {   //aggiungo i picchi al bound parsando il json
        bound.extend(new google.maps.LatLng(myArr[z].Latitudine, myArr[z].Longitudine));
    }

    map = new google.maps.Map(document.getElementById('map'), { //crea la mappa centrandola in automatico con la funzione getCenter()
        center: {lat: bound.getCenter().lat(), lng: bound.getCenter().lng()},
        zoom: 5
    });

    for (var i = 0; i < Object.keys(myArr).length; i++) {   //parso il json array associando ai vari marker il colore, il popup e le varie informazioni
        var dot;
        if (myArr[i].colour == "verde") dot = greenDot;
        if (myArr[i].colour == "rosso") dot = redDot;
        if (myArr[i].colour == "giallo") dot = yellowDot;
        if (myArr[i].colour == "arancione") dot = orangeDot;
        var marker = new google.maps.Marker({   //creo nuovo marker
            map: map,
            position: new google.maps.LatLng(myArr[i].Latitudine, myArr[i].Longitudine),
            icon: dot
        });
        var uri = window.location.href;
        var adm = "viewMapAdmin";
        var isAdmin = false;
        var contentString = '<p>Nome: ' + myArr[i].Nome + '</p><p> Elevazione: ' + myArr[i].Elevazione + 'm</p>';

        if (uri.includes(adm)) {
            isAdmin = true;
        }

       var infowindow = new google.maps.InfoWindow({    //popup sul marker
            content: contentString
        });

        google.maps.event.addListener(marker, 'mouseover', (function (marker, contentString, infoWindow) {
            return function () {
                infoWindow.setContent(contentString);
                infoWindow.open(map, marker)
            };
        })(marker, contentString, infowindow));

        google.maps.event.addListener(marker, 'mouseout', (function (marker, infoWindow) {
            return function () {
                infoWindow.close(map, marker);
            };
        })(marker, infowindow));

        google.maps.event.addListener(marker, 'click', (function (marker, myArr, i) {
            return function () {
                if (isAdmin) {showAnnotation(myArr[i]);}    //se admin sul click mostra le annotazioni
                else if (!isAdmin) {window.location.href = "../userAnnotation.jsp?piccoId=" + myArr[i].ID +"&piccoNome="+myArr[i].Nome+"&piccoNomiLocalizzati="+myArr[i].Nomi_localizzati+"&piccoElevazione="+myArr[i].Elevazione+"&idBackCampaign="+id;}   //se user apre pagina per aggiungere annotazione
            };
        })(marker, myArr, i));
    }

    map.fitBounds(bound); // zoooma per farci stare tutti i picchi
}

function backToCampaigns(){
    self.location.href = "../home.jsp";
}