package it.unical.progweb.model;

public class DettagliOrdini {
    public int id;
    public int idOrdine;
    public int idProdotto;
    public int quantita;

    public DettagliOrdini(int id, int idOrdine, int idProdotto, int quantita) {
        this.id = id;
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;
        this.quantita = quantita;
    }

    public int getId() {
        return id;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public int getQuantita() {
        return quantita;
    }
}
