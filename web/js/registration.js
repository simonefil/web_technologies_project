function removeLogin() {
    hideElement("loginDiv");
    showElement("registrationDiv");

    document.getElementsByName('username')[0].addEventListener('input', usernameVerify, true);
    document.getElementsByName('password')[0].addEventListener('input', passwordVerify, true);
    document.getElementsByName('rePassword')[0].addEventListener('input', rePasswordVerify, true);
    document.getElementsByName('email')[0].addEventListener('input', emailVerify, true);
    document.getElementsByName('nome')[0].addEventListener('input', nomeVerify, true);
    document.getElementsByName('cognome')[0].addEventListener('input', cognomeVerify, true);
}


function checkForm() {

    var username = document.getElementsByName('username')[0];
    var password = document.getElementsByName('password')[0];
    var rePassword = document.getElementsByName('rePassword')[0];
    var email = document.getElementsByName('email')[0];
    var nome = document.getElementsByName('nome')[0];
    var cognome = document.getElementsByName('cognome')[0];

    var usernameError = document.getElementById("usernameError");
    var passwordError = document.getElementById("passwordError");
    var emailError = document.getElementById("emailError");
    var nomeError = document.getElementById("nomeError");
    var cognomeError = document.getElementById("cognomeError");

    if (username.value === "") {
        username.style.border = "2px solid red";
        usernameError.textContent = "Inserire uno Username";
        username.focus();
        return false;
    }


    if ( /[^a-zA-Z0-9/]/.test(username.value)) {     //username solo alfanumerico
        username.style.border = "2px solid red";
        usernameError.textContent = "Lo username può contenere solo numeri o lettere";
        username.focus();
        return false;
    }

    if (username.value.length < 3) {
        username.style.border = "2px solid red";
        usernameError.textContent = "Lo username deve essere almeno di 3 caratteri";
        username.focus();
        return false;
    }

    if (username.value.length > 15) {
        username.style.border = "2px solid red";
        usernameError.textContent = "Lo username può essere massimo di 15 caratteri";
        username.focus();
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

    if (email.value === "") {
        email.style.border = "2px solid red";
        emailError.textContent = "Inserire l'email";
        email.focus();
        return false;
    }

    if(!(/^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email.value))) {    // /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/ è l'esperssione regolare che controlla se la stringa inserita è una mail valida
        email.style.border = "2px solid red";
        emailError.textContent = "Inserire una mail valida";
        email.focus();
        return false;
    }

    if (nome.value === "") {
        nome.style.border = "2px solid red";
        nomeError.textContent = "Inserire un Nome";
        nome.focus();
        return false;
    }

    if (nome.value.length > 20) {
        nome.style.border = "2px solid red";
        nomeError.textContent = "Il nome può essere al massimo 20 caratteri";
        nome.focus();
        return false;
    }

    if (cognome.value === "") {
        cognome.style.border = "2px solid red";
        cognomeError.textContent = "Inserire un Cognome";
        cognome.focus();
        return false;
    }

    if (cognome.value.length > 20) {
        cognome.style.border = "2px solid red";
        cognomeError.textContent = "Il cognome può essere al massimo 20 caratteri";
        cognome.focus();
        return false;
    }

    document.getElementById("registrationForm").submit();
}

function usernameVerify() {
    var username = document.getElementsByName('username')[0];
    var usernameError = document.getElementById("usernameError");
    if (username.value != "") {
        username.style.border = "1px solid #5e6e66";
        usernameError.innerHTML = "";
        return true;
    }
}

function rePasswordVerify() {
    var rePassword = document.getElementsByName('rePassword')[0];
    var password = document.getElementsByName('password')[0];
    if (password.value === rePassword.value){
        rePassword.style.border = "1px solid #5e6e66";
        passwordError.innerHTML = "";
        return true;
    }

}

function passwordVerify() {
    var password = document.getElementsByName('password')[0];
    var passwordError = document.getElementById("passwordError");
    if (password.value != "") {
        password.style.border = "1px solid #5e6e66";
        passwordError.innerHTML = "";
        return true;
    }
}

function emailVerify() {
    var email = document.getElementsByName('email')[0];
    var emailError = document.getElementById("emailError");
    if (email.value != "") {
        email.style.border = "1px solid #5e6e66";
        emailError.innerHTML = "";
        return true;
    }
}

function nomeVerify() {
    var nome = document.getElementsByName('nome')[0];
    var nomeError = document.getElementById("nomeError");
    if (nome.value != "") {
        nome.style.border = "1px solid #5e6e66";
        nomeError.innerHTML = "";
        return true;
    }
}

function cognomeVerify() {
    var cognome = document.getElementsByName('cognome')[0];
    var cognomeError = document.getElementById("cognomeError");
    if (cognome.value != "") {
        cognome.style.border = "1px solid #5e6e66";
        cognomeError.innerHTML = "";
        return true;
    }
}

function hideElement(idDesc){
    var element = document.getElementById(idDesc);
    element.style.visibility = 'hidden';
    element.style.position = 'absolute';
}

function showElement(idDesc) {
    var element = document.getElementById(idDesc);
    element.style.visibility = '';
    element.style.position = '';
}

function returnBack() {
    showElement("loginDiv");
    hideElement("registrationDiv");
}