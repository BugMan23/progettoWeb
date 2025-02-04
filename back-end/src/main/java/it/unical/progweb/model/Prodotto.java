package it.unical.progweb.model;

import java.text.DecimalFormat;

public class Prodotto {
    public int id;
    public String nome;
    public String marca;
    public String colore;
    public int prezzo;
    public String descrizione;
    public Boolean scontato;

    public Prodotto(int id, String nome, String marca, String colore, int prezzo, String descrizione, Boolean scontato) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.colore = colore;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.scontato = scontato;
    }

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

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
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
