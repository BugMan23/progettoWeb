package it.unical.progweb.model;

public class Recensione {
    public int idUtente;
    public int idProdotto;
    public int valutazione;
    public String testo;
    public String data;

    public Recensione(int idUtente, int idProdotto, int valutazione, String testo, String data) {
        this.idUtente = idUtente;
        this.idProdotto = idProdotto;
        this.valutazione = valutazione;
        this.testo = testo;
        this.data = data;
    }

    public int getIdUtente() {
        return idUtente;
    }


    public int getIdProdotto() {
        return idProdotto;
    }


    public int getValutazione() {
        return valutazione;
    }


    public String getTesto() {
        return testo;
    }


    public String getData() {
        return data;
    }

}
