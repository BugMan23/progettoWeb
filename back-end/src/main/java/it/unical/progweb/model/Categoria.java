package it.unical.progweb.model;

import javax.lang.model.element.NestingKind;

public class Categoria {
    public int id;
    public String nome;
    public String descrizione;

    public Categoria(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
