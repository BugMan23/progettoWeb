package it.unical.progweb.model;

public class DettagliOrdineResponse {
    private int id;
    private int idOrdine;
    private int idProdotto;
    private String nomeProdotto;
    private int prezzoProdotto;
    private int quantita;

    public DettagliOrdineResponse(int id, int idOrdine, int idProdotto, String nomeProdotto, int prezzoProdotto, int quantita) {
        this.id = id;
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;
        this.nomeProdotto = nomeProdotto;
        this.prezzoProdotto = prezzoProdotto;
        this.quantita = quantita;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public int getPrezzoProdotto() {
        return prezzoProdotto;
    }

    public void setPrezzoProdotto(int prezzoProdotto) {
        this.prezzoProdotto = prezzoProdotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}