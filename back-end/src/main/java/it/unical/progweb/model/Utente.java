package it.unical.progweb.model;

public class Utente {
    public int id;
    public String nome;
    public String cognome;
    public String email;
    public String telefono;
    public String username;
    public String password;
    public Boolean ruolo;


    public Utente(int id, String nome, String cognome, String email, String telefono, String username, String password, Boolean ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRuolo() {
        return ruolo;
    }
}