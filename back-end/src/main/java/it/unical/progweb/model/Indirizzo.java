package it.unical.progweb.model;

public class Indirizzo {
    public int id;
    public String nomeVia;
    public String civico;
    public String citta;
    public String cap;
    public String provincia;
    public String regione;
    public int idUtente;

    public Indirizzo(int id, String nomeVia, String civico, String citta, String cap, String provincia, String regione, int idUtente) {
        this.id = id;
        this.nomeVia = nomeVia;
        this.civico = civico;
        this.citta = citta;
        this.cap = cap;
        this.provincia = provincia;
        this.regione = regione;
        this.idUtente = idUtente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeVia() {
        return nomeVia;
    }

    public void setNomeVia(String nomeVia) {
        this.nomeVia = nomeVia;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}
