package it.unical.progweb.model;

public class Utente {
    public int id;
    public String nome;
    public String cognome;
    public String email;
    public String password;
    public String ruolo;

    public Utente(int id, String nome, String cognome, String email, String password, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
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


    public String getPassword() {
        return password;
    }


    public String getRuolo() {
        return ruolo;
    }

}
