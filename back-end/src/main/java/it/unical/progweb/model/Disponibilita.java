package it.unical.progweb.model;

public class Disponibilita {
    public int id;
    public int quantita;
    public String taglia;
    public int idProdotto;

    public Disponibilita(int id, int quantita, String taglia, int idProdotto) {
        this.id = id;
        this.quantita = quantita;
        this.taglia = taglia;
        this.idProdotto = idProdotto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }
}
