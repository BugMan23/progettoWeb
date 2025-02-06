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
    public String urlImage;
    public int idCategoria;

    public Prodotto(int id, String nome, String marca, String colore, int prezzo, String descrizione, Boolean scontato, String urlImage, int idCategoria) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.colore = colore;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.scontato = scontato;
        this.urlImage = urlImage;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMarca() {
        return marca;
    }

    public String getColore() {
        return colore;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Boolean getScontato() {
        return scontato;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public int getIdCategoria() {
        return idCategoria;
    }
}