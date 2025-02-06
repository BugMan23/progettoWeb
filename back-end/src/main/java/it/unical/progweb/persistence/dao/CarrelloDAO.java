package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Prodotto;

import java.util.List;

public interface CarrelloDAO {

    void addAlCarrello(int userId, int prodottoId, int quantita);
    List<Prodotto>  getCarrello(int userId);

    void clear(int userId);
}

