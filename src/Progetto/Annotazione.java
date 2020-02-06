package Progetto;

/**
 * Classe che rappresenta l'oggetto Annotazione con i suoi attributi e metodi
 *
 */

public class Annotazione {
    private String id;
    private String data_creazione;
    private int validita;
    private int elevazione;
    private String nome;
    private String nomi_localizzati;
    private int stato;
    private String user;
    private int id_picco;

    public Annotazione(String id, String data_creazione, int validita, int elevazione, String nome, String nomi_localizzati, int stato, String user, int id_picco) {
        this.id = id;
        this.data_creazione = data_creazione;
        this.validita = validita;
        this.elevazione = elevazione;
        this.nome = nome;
        this.nomi_localizzati = nomi_localizzati;
        this.stato = stato;
        this.user = user;
        this.id_picco = id_picco;
    }

    public Annotazione() {
        this.id = "";
        this.data_creazione = "";
        this.validita = 0;
        this.elevazione = 0;
        this.nome = "";
        this.nomi_localizzati = "";
        this.stato = -1;
        this.user = "";
        this.id_picco = 0;
    }

    public String getId() {
        return id;
    }

    public String getData_creazione() {
        return data_creazione;
    }

    public int getValidita() {
        return validita;
    }

    public String getValiditaDesc() {

        if(validita==0)
            return "non valida";
        else if(validita==1)
            return "valida";

        return "";

    }

    public int getElevazione() {
        return elevazione;
    }

    public String getNome() {
        return nome;
    }

    public String getNomi_localizzati() {
        return nomi_localizzati;
    }

    public int getStato() {
        return stato;
    }

    public String getStatoDesc() {

        if(stato==0)
            return "respinta";
        else if(stato==1)
            return "non respinta";

        return "";

    }

    public String getUser() {
        return user;
    }

    public int getId_picco() {
        return id_picco;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setData_creazione(String data_creazione) {
        this.data_creazione = data_creazione;
    }

    public void setValidita(int validita) {
        this.validita = validita;
    }

    public void setElevazione(int elevazione) {
        this.elevazione = elevazione;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNomi_localizzati(String nomi_localizzati) {
        this.nomi_localizzati = nomi_localizzati;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setId_picco(int id_picco) {
        this.id_picco = id_picco;
    }
}
