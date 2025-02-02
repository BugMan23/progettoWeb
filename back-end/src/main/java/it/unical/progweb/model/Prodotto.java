package it.unical.progweb.model;

import java.text.DecimalFormat;

public class Prodotto {
    public int id;
    public String nome;
    public String marca;
    public String colore;
    public DecimalFormat prezzo;
    public String descrizione;
    public Boolean scontato;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public DecimalFormat getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(DecimalFormat prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getScontato() {
        return scontato;
    }

    public void setScontato(Boolean scontato) {
        this.scontato = scontato;
    }
}
