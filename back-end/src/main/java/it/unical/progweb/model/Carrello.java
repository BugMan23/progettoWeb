package it.unical.progweb.model;
public class Carrello {
    public int id;
    public int idUtente;
    public int idProdotto;
    public int quantita;
    public String taglia;
    public Boolean isOrdinato;
    public Boolean rimosso;

    public Carrello(int id, int idUtente, int idProdotto, int quantita, String taglia, Boolean isOrdinato, Boolean rimosso) {
        this.id = id;
        this.idUtente = idUtente;
        this.idProdotto = idProdotto;
        this.quantita = quantita;
        this.taglia = taglia;
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

    public int getQuantita() {
        return quantita;
    }

    public String getTaglia() {
        return taglia;
    }

    public Boolean getOrdinato() {
        return isOrdinato;
    }
}