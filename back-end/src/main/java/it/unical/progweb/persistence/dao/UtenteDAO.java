package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Utente;

import java.util.List;

public interface UtenteDAO {
    Utente findById(int id);
    List<Utente> findAll();
    boolean save(Utente utente);
    boolean updateEmail(Utente utente, String email);
    void delete(int id);
    Utente findByEmail(String email);
    void changePassword(int id, String newPassword);
    Utente validateUser(String email, String password);
}
