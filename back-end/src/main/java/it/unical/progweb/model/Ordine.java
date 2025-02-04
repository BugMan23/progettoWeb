package it.unical.progweb.model;

public class Ordine {
    public int idCarrello;
    public int idProdotto;
    public String data;
    public String stato;
    public String consegna;     // TODO MODIFICARE CAMPO NEL DATABASE

    public Ordine(int idCarrello, int idProdotto, String data, String stato, String consegna) {
        this.idCarrello = idCarrello;
        this.idProdotto = idProdotto;
        this.data = data;
        this.stato = stato;
        this.consegna = consegna;
    }

    public int getIdCarrello() {
        return idCarrello;
    }

    public void setIdCarrello(int idCarrello) {
        this.idCarrello = idCarrello;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getConsegna() {
        return consegna;
    }

    public void setConsegna(String consegna) {
        this.consegna = consegna;
    }
}
