package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Indirizzo;

import java.util.List;

public interface IndirizzoDAO {
    Indirizzo findById(int id);
    List<Indirizzo> findByUtenteId(int utenteId);
    void save(Indirizzo indirizzo);
    void update(Indirizzo indirizzo);
    void delete(int id);
}
