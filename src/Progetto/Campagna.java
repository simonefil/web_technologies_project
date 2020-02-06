package Progetto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe che rappresenta l'oggetto Campagna con i suoi attributi e metodi
 *
 */

public class Campagna {
    private String id;
    private String nome;
    private int stato;
    private String dataInizio;
    private String dataFine;
    private ArrayList<Picco> picchi;

    public Campagna() {
        this.id = "";
        this.nome = "";
        this.stato = -1;
        this.dataFine = "";
        this.dataInizio = "";
        this.picchi=null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public String getNome() {
        return nome;
    }

    public int getStato() {
        return stato;
    }

    public String getDataInizio() {
        return dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public String getId() {
        return id;
    }


    public ArrayList<Picco> getPicchi() {
        return picchi;
    }

    public void setPicchi(ArrayList<Picco> picchi) {
        this.picchi = picchi;
    }

    public  ArrayList<Campagna> fillCampagne(ResultSet rs) {
        ArrayList<Campagna> arrayCampagne = new ArrayList<>();
        try {
        while(rs.next()) {
            Campagna temp = new Campagna();
            temp.setId(rs.getString("ID"));
            temp.setNome(rs.getString("Nome"));
            temp.setStato(rs.getInt("Stato"));
            temp.setDataInizio(rs.getString("Data_Inizio"));
            temp.setDataFine(rs.getString("Data_Fine"));
            arrayCampagne.add(temp);
        }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.toString());
        }
        return arrayCampagne;
    }

}
