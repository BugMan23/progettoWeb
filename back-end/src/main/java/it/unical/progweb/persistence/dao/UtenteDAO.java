package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Utente;

import java.util.List;

public interface UtenteDAO {
    void addUtente(Utente utente);

    Utente findById(int id);
    void updateEmail(int idutente, String email);

    //admim
    List<Utente> findAll();


    Utente findByEmail(String email);

}
