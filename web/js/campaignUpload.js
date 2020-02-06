function pageForward(id) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            var data = xhr.responseText;
            self.location.href = "../campaignInfoAdmin.jsp"
        }
    }
    xhr.open('GET', 'campaignInfoPage'+'?'+'id='+id, true);
    xhr.send(null);
}