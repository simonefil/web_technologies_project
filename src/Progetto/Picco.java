package Progetto;

import java.util.ArrayList;

/**
 * Classe che rappresenta l'oggetto Picco con i suoi attributi e metodi
 *
 */

public class Picco {

    private String id;
    private String provenienza;
    private int elevazione;
    private String longitudine;
    private String latitudine;
    private String nome;
    private String nomi_localizzati;
    private int daAnnotare;
    private int campagnaId;
    private ArrayList<Annotazione> annotazioni;

    Picco() {
        this.id = "";
        this.provenienza = "";
        this.elevazione = 0;
        this.longitudine = "";
        this.latitudine = "";
        this.nome = "";
        this.nomi_localizzati = "";
        this.daAnnotare = 0;
        this.campagnaId = 0;
        this.annotazioni=null;
    }

    public String getId() {
        return id;
    }

    public String getProvenienza() {
        return provenienza;
    }

    public int getElevazione() {
        return elevazione;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public String getNome() {
        return nome;
    }

    public String getNomi_localizzati() {
        return nomi_localizzati;
    }

    public int getDaAnnotare() {
        return daAnnotare;
    }

    public int getCampagnaId() {
        return campagnaId;
    }

    public void setId(String id) {
        this.id = id;
    }

    void setProvenienza(String provenienza) {
        this.provenienza = provenienza;
    }

    public void setElevazione(int elevazione) {
        this.elevazione = elevazione;
    }

    void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNomi_localizzati(String nomi_localizzati) {
        this.nomi_localizzati = nomi_localizzati;
    }

    void setDaAnnotare(int daAnnotare) {
        this.daAnnotare = daAnnotare;
    }

    void setCampagnaId(int campagnaId) {
        this.campagnaId = campagnaId;
    }

    public ArrayList<Annotazione> getAnnotazioni() {
        return annotazioni;
    }

    void setAnnotazioni(ArrayList<Annotazione> annotazioni) {
        this.annotazioni = annotazioni;
    }

    void fillAnnotazioni(ArrayList<Annotazione> annotazioni) {
        ArrayList<Annotazione> annotazioniDaAggiungere = new ArrayList<>();

        for(Annotazione annotazioneTmp : annotazioni){
            if(annotazioneTmp.getId_picco() == Integer.parseInt(this.getId())) {
                //se l'id_picco dell'annotazione è uguale a quello del picco allora è un'annotazione che fa riferimento a quel picco
                annotazioniDaAggiungere.add(annotazioneTmp);
            }
        }
        this.setAnnotazioni(annotazioniDaAggiungere);
    }

    public boolean hasAnnotazioni() {
        if(this.annotazioni != null && this.annotazioni.size()>0)
            return true;
        else
            return false;
    }

}
