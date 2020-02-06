var myArr = null;
function getUrlParams() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

var id = getUrlParams()["x"];
var provenienza = getUrlParams()["provenienza"];
var xmlhttp = new XMLHttpRequest();
xmlhttp.open('GET', 'GetPeaks' + '?' + 'id=' + id + '&provenienza=' + provenienza, false);
xmlhttp.send(null);
while (xmlhttp.status != 200) {setTimeout(function(){}, 100);}
if (xmlhttp.status == 200) myArr = JSON.parse(xmlhttp.responseText);

// <-- calcola il punto medio su cui centrare la mappa
var x = 0.0;
var y = 0.0;
var z = 0.0;
for (var i = 0; i < Object.keys(myArr).length; i++){
    var latitude = myArr[i].Latitudine * Math.PI / 180;
    var longitude = myArr[i].Longitudine * Math.PI / 180;
    x += Math.cos(latitude) * Math.cos(longitude);
    y += Math.cos(latitude) * Math.sin(longitude);
    z += Math.sin(latitude);
}
var total = Object.keys(myArr).length;
x = x / total;
y = y / total;
z = z / total;
var centralLongitude = Math.atan2(y, x);
var centralSquareRoot = Math.sqrt(x * x + y * y);
var centralLatitude = Math.atan2(z, centralSquareRoot);

var mapCenterLat = centralLatitude * 180 / Math.PI;
var mapCenterLng = centralLongitude * 180 / Math.PI;
// --> calcola il punto medio su cui centrare la mappa

Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI4NTg0YjgwZC01MWE5LTQ4M2UtYTZkZC03NWUwMGM4YTAxY2UiLCJpZCI6NDQ2Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MDkxMzA1NX0.LnyM2wr--qsAQwCDX2wkF6wz7ZK5hzdyrOpBEnltgA8';
var viewer = new Cesium.Viewer('3d',{   //creo il viewer di cesium
    terrainProvider: Cesium.createWorldTerrain()
});

var iframe = document.getElementsByClassName('cesium-infoBox-iframe')[0];
iframe.setAttribute('sandbox', 'allow-same-origin allow-scripts allow-popups allow-forms');  //per sbloccare il javascript e permettere il link interno al popup

var initialPosition = new Cesium.Cartesian3.fromDegrees(mapCenterLng, mapCenterLat, 15666); //lat-long-altezza
var initialOrientation = new Cesium.HeadingPitchRoll.fromDegrees(7.1077496389876024807, -31.987223091598949054, 0.025883251314954971306);   //inclinazione telecamera
var homeCameraView = {
    destination : initialPosition,
    orientation : {
        heading : initialOrientation.heading,
        pitch : initialOrientation.pitch,
        roll : initialOrientation.roll
    }
};
// Set the initial view
viewer.scene.camera.setView(homeCameraView);
var uri = window.location.href;
var adm = "viewMapAdmin";
var usr = "campaignInfo";
var red = Cesium.Color.RED;
var orange = Cesium.Color.ORANGE;
var yellow = Cesium.Color.YELLOW;
var green = Cesium.Color.GREEN;

for (var w = 0; w < Object.keys(myArr).length; w++) {   //parso il JSON e associo al marker il colore di Cesium
    var dot;
    if (myArr[w].colour == "verde") dot = green;
    if (myArr[w].colour == "rosso") dot = red;
    if (myArr[w].colour == "giallo") dot = yellow;
    if (myArr[w].colour == "arancione") dot = orange;
    var entity = viewer.entities.add({  // entity = marker
        name : myArr[w].Nome,
        position : Cesium.Cartesian3.fromDegrees(myArr[w].Longitudine, myArr[w].Latitudine),
        point : {
            pixelSize : 15,
            color : dot,
            outlineColor : Cesium.Color.WHITE,
            outlineWidth : 2,
            heightReference : 2
        },
        label : {   //la scritta sopra al marker nella mappa
            text : myArr[w].Nome,
            font : '14pt monospace',
            style: Cesium.LabelStyle.FILL_AND_OUTLINE,
            outlineWidth : 2,
            verticalOrigin : Cesium.VerticalOrigin.BOTTOM,
            pixelOffset : new Cesium.Cartesian2(0, -30)
        }
    });
    if (uri.includes(usr)) {    //popup per lo user con link per aggiungere l'annotazione
        entity.description = '<p> Elevazione: ' + myArr[w].Elevazione + 'm</p>' + '<a href= /userAnnotation.jsp?piccoId=' + myArr[w].ID +"&piccoNome="+encodeURIComponent(myArr[w].Nome)+"&piccoNomiLocalizzati="+ encodeURIComponent(myArr[w].Nomi_localizzati) +"&piccoElevazione="+myArr[w].Elevazione+"&idBackCampaign="+id +' target=\"mainFrame\">Annotazioni</a>';
    }
    if (uri.includes(adm)) {    //popup per l'admin
        entity.description = '<p> Elevazione: ' + myArr[w].Elevazione + 'm</p>' + '<button class="click-test-button" id=\"' + w + '\"> Annotazioni </button>';  //il bottone mostra le annotazioni del picco
    }
}
viewer.infoBox.frame.addEventListener('load', function() {
    viewer.infoBox.frame.contentDocument.body.addEventListener('click', function(e) {
        if (e.target && e.target.className === 'click-test-button') {
            showAnnotation(myArr[e.target.id]);
        }
    });
})
viewer.zoomTo(viewer.entities);

