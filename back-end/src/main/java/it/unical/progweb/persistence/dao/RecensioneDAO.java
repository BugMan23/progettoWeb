package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Recensione;

import java.util.List;

public interface RecensioneDAO {
    void addRecensione(Recensione recensione);

    void deleteRecensione(int recensioneId);

    List<Recensione> findByProdottoId(int prodottoId);

    // Nuovo metodo per trovare recensioni per utente
    List<Recensione> findByUserId(int userId);
    List<Recensione> findByUserAndProduct(int userId, int productId);
}