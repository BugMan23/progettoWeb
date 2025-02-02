package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Recensione;

import java.util.List;

public interface RecensioneDAO {
    Recensione findById(int id);
    List<Recensione> findByProdottoId(int prodottoId);
    boolean save(Recensione recensione);
    void update(Recensione recensione);
    void delete(int id);
}
