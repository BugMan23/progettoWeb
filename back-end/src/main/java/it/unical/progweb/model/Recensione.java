package it.unical.progweb.model;

public class Recensione {
    public int id;
    public int idProdotto;
    public int idUtente;
    public int valutazione;
    public String testo;
    public String data;

    public Recensione(int id, int idProdotto, int idUtente, int valutazione, String testo, String data) {
        this.id = id;
        this.idProdotto = idProdotto;
        this.idUtente = idUtente;
        this.valutazione = valutazione;
        this.testo = testo;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public String getTesto() {
        return testo;
    }

    public int getValutazione() {
        return valutazione;
    }

    public String getData() {
        return data;
    }

    public void setData(String data){
        this.data  = data;
    }

}