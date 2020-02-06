function modificaProfilo(i) {   //passato il numero della riga dal configuratore utenti per prendere i dati dell'utente che si vogliono modificare
    var username = document.getElementById("username"+i).innerText;
    var nome = document.getElementById("nome"+i).innerText;
    var cognome = document.getElementById("cognome"+i).innerText;
    var email = document.getElementById("email"+i).innerText;
    var url ='../profileDetails.jsp?username='+ username +'&nome='+ nome + '&cognome=' + cognome + '&email=' + email;
    window.open( url, '', 'location=yes,height=600,width=600,scrollbars=yes,status=yes');   //per aprire una pagina in popup
}

function closeWindow() {
    window.close();
}

function updateProfile() {

    if(controllaCampi()){
        var username = document.getElementById("username").value;
        var nome = document.getElementById("nome").value;
        var cognome = document.getElementById("cognome").value;
        var email = document.getElementById("email").value;
        var url = '../UpdateProfile?username='+ username +'&nome='+ nome + '&cognome=' + cognome + '&email=' + email;
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", url, true);
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function() {
                    if (xhr.readyState == 4 && xhr.status == 200) {
                        var data = xhr.responseText;
                        opener.location.href = '/adminHome.jsp';
                        window.close();
                    }
                }
                xhr.open('GET', 'UpdateUsersList', true);
                xhr.send(null);
            }
        }
        xhttp.send(null);
    }

}

function controllaCampi() {
    var email = document.getElementById("email");
    var nome = document.getElementById("nome");
    var cognome = document.getElementById("cognome");

    if (email.value === "") {
        email.style.border = "2px solid red";
        alert("Il campo email non può essere vuoto");
        email.focus();
        return false;
    }

    if(!(/^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email.value))) {    // /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/ è l'esperssione regolare che controlla se la stringa inserita è una mail valida
        email.style.border = "2px solid red";
        alert("Inserire una email valida");
        email.focus();
        return false;
    }

    if (nome.value === "") {
        nome.style.border = "2px solid red";
        alert("Il campo nome non può essere vuoto");
        nome.focus();
        return false;
    }

    if (nome.value.length > 20) {
        nome.style.border = "2px solid red";
        alert("Il nome può essere al massimo 20 caratteri");
        nome.focus();
        return false;
    }

    if (cognome.value === "") {
        cognome.style.border = "2px solid red";
        alert("Il campo cognome non può essere vuoto");
        cognome.focus();
        return false;
    }

    if (cognome.value.length > 20) {
        cognome.style.border = "2px solid red";
        alert("Il cognome può essere al massimo 20 caratteri");
        cognome.focus();
        return false;
    }

    return true;
}

function controllaCampiAdmin() {
    var email = document.getElementById("email");
    var nome = document.getElementById("nome");
    var cognome = document.getElementById("cognome");
    var password = document.getElementById("password");
    var rePassword = document.getElementById("rePassword");

    if (email.value === "") {
        email.style.border = "2px solid red";
        alert("Il campo email non può essere vuoto");
        email.focus();
        return false;
    }

    if (nome.value === "") {
        nome.style.border = "2px solid red";
        alert("Il campo nome non può essere vuoto");
        nome.focus();
        return false;
    }

    if (nome.value.length > 20) {
        nome.style.border = "2px solid red";
        alert("Il nome può essere al massimo 20 caratteri");
        nome.focus();
        return false;
    }

    if (cognome.value === "") {
        cognome.style.border = "2px solid red";
        alert("Il campo cognome non può essere vuoto");
        cognome.focus();
        return false;
    }

    if (cognome.value.length > 20) {
        cognome.style.border = "2px solid red";
        alert("Il cognome può essere al massimo 20 caratteri");
        cognome.focus();
        return false;
    }

    if (password.value === "") {
        password.style.border = "2px solid red";
        passwordError.textContent = "Inserire la password";
        password.focus();
        return false;
    }

    if ( /[^a-zA-Z0-9._\-\/]/.test(password.value)) {
        password.style.border = "2px solid red";
        passwordError.textContent = "La password può contenere solo numeri, lettere e . _ - ";
        password.focus();
        return false;
    }

    if(password.value.length<6) {
        password.style.border = "2px solid red";
        passwordError.textContent = "La password deve essere lunga almeno 6 caratteri";
        password.focus();
        return false;
    }

    if(password.value.length>20) {
        password.style.border = "2px solid red";
        passwordError.textContent = "La password può essere al massimo di 20 caratteri";
        password.focus();
        return false;
    }

    if (password.value != rePassword.value) {
        password.style.border = "2px solid red";
        password.value="";
        rePassword.style.border = "2px solid red";
        rePassword.value="";
        passwordError.textContent = "Le password non corrispondono";
        password.focus();
        return false;
    }

    if(!(/^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email.value))) {    // /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/ è l'esperssione regolare che controlla se la stringa inserita è una mail valida
        email.style.border = "2px solid red";
        alert("Inserire una email valida");
        email.focus();
        return false;
    }

    return true;
}

function updateProfileAdmin() {
    if(controllaCampiAdmin()){
        var password = document.getElementById("password").value;
        var nome = document.getElementById("nome").value;
        var cognome = document.getElementById("cognome").value;
        var email = document.getElementById("email").value;

        var url = '../ChangeProfileInfo?password='+ password +'&nome='+ nome + '&cognome=' + cognome + '&email=' + email;

        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", url, true);
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.close();
                var a = document.createElement('a');
                a.href='/Logout';
                a.target = 'mainFrame';
                opener.document.body.appendChild(a);
                a.click();

            }
        }
        xhttp.send(null);
    }
}