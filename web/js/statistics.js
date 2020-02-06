
function PicchiAnnotati(){

    //picchi annotati
    showElement("picchiAnnotati");
    showElement("picchiAnnotatiTableDiv");
    hideElement("annotationTableDiv");
    hideElement("titoloImg");
    hideElement("backImg");

    //picchi non annotati
    hideElement("picchiNonAnnotati");

    //picchi con annotazioni rifiutate
    hideElement("annotazioniRifiutate");
    hideElement("picchiRifiutatiTableDiv");
    hideElement("annotazioniRifiutateTableDiv");

    //conflitti
    hideElement("conflitti");

    var titolo = document.getElementById("Titolo");
    titolo.innerText="Picchi con almeno un'Annotazione";

    SelectButton(1);
}

function returnBack(){
    var selezione = document.getElementById("selezione");

    if(selezione.value==1)
        PicchiAnnotati();
    if(selezione.value==3)
        AnnotazioniRifiutate();
    if(selezione.value==4)
        Conflitti();
}

function PicchiNonAnnotati(){

    //picchi annotati
    hideElement("picchiAnnotati");
    hideElement("picchiAnnotatiTableDiv");
    hideElement("annotationTableDiv");
    hideElement("titoloImg");
    hideElement("backImg");

    //picchi non annotati
    showElement("picchiNonAnnotati");

    //picchi con annotazioni rifiutate
    hideElement("annotazioniRifiutate");
    hideElement("picchiRifiutatiTableDiv");
    hideElement("annotazioniRifiutateTableDiv");

    //conflitti
    hideElement("conflitti");


    var titolo = document.getElementById("Titolo");
    titolo.innerText="Picchi Non Annotati";

    SelectButton(2);
}

function AnnotazioniRifiutate() {
    //picchi annotati
    hideElement("picchiAnnotati");
    hideElement("picchiAnnotatiTableDiv");
    hideElement("annotationTableDiv");
    hideElement("titoloImg");
    hideElement("backImg");

    //picchi non annotati
    hideElement("picchiNonAnnotati");

    //picchi con annotazioni rifiutate
    showElement("annotazioniRifiutate");
    showElement("picchiRifiutatiTableDiv");
    hideElement("annotazioniRifiutateTableDiv");

    //conflitti
    hideElement("conflitti");

    var titolo = document.getElementById("Titolo");
    titolo.innerText="Picchi con Annotazioni rifiutate";

    SelectButton(3);
}

function Conflitti() {
    //picchi annotati
    hideElement("picchiAnnotati");
    hideElement("picchiAnnotatiTableDiv");
    hideElement("annotationTableDiv");
    hideElement("titoloImg");
    hideElement("backImg");

    //picchi non annotati
    hideElement("picchiNonAnnotati");

    //picchi con annotazioni rifiutate
    hideElement("annotazioniRifiutate");
    hideElement("picchiRifiutatiTableDiv");
    hideElement("annotazioniRifiutateTableDiv");

    //conflitti
    showElement("conflitti");
    showElement("conflittiPicchiTableDiv");
    hideElement("annotazioniConflittiTableDiv");

    var titolo = document.getElementById("Titolo");
    titolo.innerText="Conflitti";

    SelectButton(4);
}

function SelectButton(numero){
    var button1 = document.getElementById("picchiAnnotatiButton");
    var button2 = document.getElementById("picchiNonAnnotatiButton");
    var button3 = document.getElementById("annotazioniRifiutateButton");
    var button4 = document.getElementById("conflittiButton");

    var numero1 = document.getElementById("numeroPicchiAnnotati");
    var numero2 = document.getElementById("numeroPicchiNonAnnotati");
    var numero3 = document.getElementById("numeroPicchiRifiutati");
    var numero4 = document.getElementById("numeroConflitti");

    if(numero==1){
        button1.className = "SelectedDetailsButton";
        button2.className = "DetailsButton";
        button3.className = "DetailsButton";
        button4.className = "DetailsButton";

        numero1.style.color = "black";
        numero2.style.color = "#bfbfbf";
        numero3.style.color = "#bfbfbf";
        numero4.style.color = "#bfbfbf";

        document.getElementById("selezione").value=1;
    }
    if(numero==2){
        button1.className = "DetailsButton";
        button2.className = "SelectedDetailsButton";
        button3.className = "DetailsButton";
        button4.className = "DetailsButton";

        numero1.style.color = "#bfbfbf";
        numero2.style.color = "black";
        numero3.style.color = "#bfbfbf";
        numero4.style.color = "#bfbfbf";

        document.getElementById("selezione").value=2;
    }
    if(numero==3){
        button1.className = "DetailsButton";
        button2.className = "DetailsButton";
        button3.className = "SelectedDetailsButton";
        button4.className = "DetailsButton";

        numero1.style.color = "#bfbfbf";
        numero2.style.color = "#bfbfbf";
        numero3.style.color = "black";
        numero4.style.color = "#bfbfbf";

        document.getElementById("selezione").value=3;
    }
    if(numero==4){
        button1.className = "DetailsButton";
        button2.className = "DetailsButton";
        button3.className = "DetailsButton";
        button4.className = "SelectedDetailsButton";

        numero1.style.color = "#bfbfbf";
        numero2.style.color = "#bfbfbf";
        numero3.style.color = "#bfbfbf";
        numero4.style.color = "black";

        document.getElementById("selezione").value=4;
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

function backToDetails(){
    self.location.href = "../campaignInfoAdmin.jsp";
}