package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Utente;

import java.util.List;

public interface UtenteDAO {
    Utente findById(int id);
    List<Utente> findAll();
    boolean save(Utente utente);
    void elimina(int id);
    Utente findByEmail(String email);
}
