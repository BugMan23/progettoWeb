package it.unical.progweb.service;

import it.unical.progweb.eccezioni.AuthenticationException;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.IndirizzoDAO;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;
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

    public void registraUtente(Utente utente) {
        // Validazione dati
        if (utente.getPassword() == null || utente.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password non valida");
        }
        if (utente.getEmail() == null || !utente.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email non valida");
        }

        String hashedPassword = BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt());
        utente.setPassword(hashedPassword);

        utenteDAO.save(utente);
    }

    public Utente login(String email, String password) {
        Utente utente = utenteDAO.validateUser(email, password);
        if (utente == null) {
            throw new AuthenticationException("Credenziali non valide");
        }
        return utente;
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

    /*public void changePassword(int userId, String currentPassword, String newPassword) {
        if(currentPassword.equals(newPassword)){
            throw new IllegalArgumentException("Le password sono uguali");
        }
        utenteDAO.updatePassword(userId, newPassword);
    }*/
}