package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Ordine;

import java.util.List;

public interface OrdineDAO {
    Ordine findById(int id);
    List<Ordine> findAll();
    List<Ordine> findByUtenteId(int utenteId);
    void save(Ordine ordine);
    void update(Ordine ordine);
    void delete(int id);
}

