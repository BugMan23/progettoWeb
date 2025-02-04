package it.unical.progweb.service;

import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.DBManager;
import it.unical.progweb.persistence.dao.UtenteDAO;

import java.util.List;

public class UserService {
    public UserService() {
    }

    public Utente getUserById(int id){
        return DBManager.getInstance().getUserDAO().findById(id);
    }

    public List<Utente> getAllUser(){
        return DBManager.getInstance().getUserDAO().findAll();
    }

    public Utente getUtenteByEmail(String email){
        return DBManager.getInstance().getUserDAO().findByEmail(email);
    }

    public boolean aggiornaEmail(String email){
        return DBManager.getInstance().getUserDAO().updateEmail(email);
    }
}
