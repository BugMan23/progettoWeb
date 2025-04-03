package it.unical.progweb.model;

public class Ordine {
    private int id;
    private int idUtente;
    private String data;
    private String stato;
    private int totalePagare;
    private int idMetodoPagamento;

    public Ordine(int id, int idUtente, String data, String stato, int totalePagare, int idMetodoPagamento) {
        this.id = id;
        this.idUtente = idUtente;
        this.data = data;
        this.stato = stato;
        this.totalePagare = totalePagare;
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public Ordine(int id, int idUtente, String stato, int totalePagare, int idMetodoPagamento) {
        this.id = id;
        this.idUtente = idUtente;
        this.stato = stato;
        this.totalePagare = totalePagare;
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public int getId() {
        return id;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public String getData() {
        return data;
    }

    public String getStato() {
        return stato;
    }

    public int getIdMetodoPagamento() {
        return idMetodoPagamento;
    }

    public int getTotalePagare() {
        return totalePagare;
    }

    public void setId(int ordineId) {
        this.id = ordineId;
    }

    public void setStato(String nuovoStato) {
        this.stato = nuovoStato;
    }
}