package Progetto;

/**
 * Classe che rappresenta l'oggetto User con i suoi attributi e metodi
 *
 */

public class User {
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private String isAdmin;

    public User() {
        this.username="";
        this.password="";
        this.email="";
        this.nome="";
        this.cognome="";
        this.isAdmin="";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setAdmin(String admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String isAdmin() {
        return isAdmin;
    }
}
