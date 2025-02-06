package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Recensione;

import java.util.List;

public interface RecensioneDAO {
    void addRecensione(Recensione recensione);

    //admin
    void deleteRecensione(int recensioneId);


    List<Recensione> findByProdottoId(int prodottoId);

    //void update(Recensione recensione);

}
