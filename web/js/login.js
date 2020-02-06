function login() {
    var username = document.getElementsByName('loginUsername')[0].value;
    var password = document.getElementsByName('loginPassword')[0].value;
    var url =  '../Login?loginUsername=' + username + '&loginPassword=' + password;
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (xhttp.getResponseHeader("LoginFailure") == "true") error();
        if (xhttp.getResponseHeader("LoginFailure") == "false") {
            if (xhttp.getResponseHeader("Admin") == "true") adminForward();
            else if (xhttp.getResponseHeader("Admin") == "false") forward();
            else window.location.href = "../error.html";
        }
    }
    xhttp.open("GET", url, true);
    xhttp.send(null);
}

function error() {
    document.getElementById('error').innerHTML = "<br><b><big><font style=\"color:rgb(255, 0, 0);\">Username o password non corretti</font></big></b><br><br>";
    document.getElementById("loginPsw").value="";
}

function forward() {    //chiamata alla login per reindirizzare alla home dello user
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = xhr.responseText;
            window.location.href = "../main.jsp";
        }
    }
    xhr.open('GET', 'GetNotSubscribedCamp', true);
    xhr.send(null);
}

function forwardHome() {  //chiamata premendo sul bottone nel top frame dello user
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = xhr.responseText;
            top.frames['mainFrame'].location.href = 'home.jsp';
        }
    }
    xhr.open('GET', 'GetNotSubscribedCamp', true);
    xhr.send(null);
}

function adminForward() {   //chiamata alla login per reindirizzare alla home dell'admin
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = xhr.responseText;
            window.location.href = "../main.jsp"
        }
    }
    xhr.open('GET', 'GetCampaignInfo?operation=login', true);
    xhr.send(null);

}

function adminForwardHome() {   //chiamata premendo sul bottone nel top frame dell'admin
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = xhr.responseText;
            top.frames['mainFrame'].location.href = 'adminHome.jsp';
        }
    }
    xhr.open('GET', 'GetCampaignInfo?operation=login', true);
    xhr.send(null);

}

function profilePopUp(){    //chiamata premendo sul bottone nel top frame
    var url ='../profileInfo.jsp;';
    window.open( url, '', 'location=yes,height=600,width=600,scrollbars=yes,status=yes');
}