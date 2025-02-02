package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Disponibilita;

import java.util.List;

public interface DisponibilitaDAO {
    Disponibilita findById(int id);
    List<Disponibilita> findByProdottoId(int prodottoId);
    void save(Disponibilita disponibilita);
    void update(Disponibilita disponibilita);
    void delete(int id);
}
