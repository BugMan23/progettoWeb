package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Carrello;

import java.util.List;

public interface CarrelloDAO {
    Carrello findById(int id);
    Carrello findByUtenteId(int utenteId);
    void addProdotto(int carrelloId, int prodottoId, int quantità);
    void removeProdotto(int carrelloId, int prodottoId);
    void clear(int carrelloId);
}

