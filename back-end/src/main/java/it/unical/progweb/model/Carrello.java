package it.unical.progweb.model;

public class Carrello {
    public int id;
    public int idUtente;
    public int idProdotto;
    public String quantita;
    public Boolean isOrdinato;
    public Boolean rimosso;  // Nuovo campo

    public Carrello(int id, int idUtente, int idProdotto, String quantita, Boolean isOrdinato, Boolean rimosso) {
        this.id = id;
        this.idUtente = idUtente;
        this.idProdotto = idProdotto;
        this.quantita = quantita;
        this.isOrdinato = isOrdinato;
        this.rimosso = rimosso;
    }

    public int getId() {
        return id;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public String getQuantita() {
        return quantita;
    }

    public Boolean getOrdinato() {
        return isOrdinato;
    }

    public Boolean getRimosso() {
        return rimosso;
    }

    public void setRimosso(Boolean rimosso) {
        this.rimosso = rimosso;
    }
}