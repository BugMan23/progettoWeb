package it.unical.progweb.service;

import it.unical.progweb.eccezioni.AuthenticationException;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.IndirizzoDAO;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UtenteDAO utenteDAO;
    private final IndirizzoDAO indirizzoDAO;

    @Autowired
    public UserService(UtenteDAO utenteDAO, IndirizzoDAO indirizzoDAO) {
        this.utenteDAO = utenteDAO;
        this.indirizzoDAO = indirizzoDAO;
    }

    public Utente findById(int id) {
        Utente utente = utenteDAO.findById(id);
        if (utente == null) {
            throw new NotFoundException("Utente non trovato");
        }
        return utente;
    }

    public List<Utente> getAllUtenti() {
        return utenteDAO.findAll();
    }

    public void aggiungiIndirizzoSpedizione(int utenteId, Indirizzo indirizzo) {
        indirizzoDAO.addIndirizzo(indirizzo, utenteId);
    }


    public Utente findByEmail(String email) {
        Utente utente = utenteDAO.findByEmail(email);
        if (utente == null) {
            throw new NotFoundException("Utente non trovato");
        }
        return utente;
    }


    public void eliminaUtente(int id) {
        Utente utente = findById(id);
        if (utente == null) {
            throw new NotFoundException("Utente non trovato");
        }
        utenteDAO.elimina(id);
    }



}