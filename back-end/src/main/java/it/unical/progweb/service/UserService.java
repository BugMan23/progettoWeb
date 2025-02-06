package it.unical.progweb.service;

import it.unical.progweb.eccezioni.AuthenticationException;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.DBManager;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public UserService() {
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

        DBManager.getInstance().getUserDAO().addUtente(utente);
    }

    public Utente login(String email, String password) {
        Utente utente = DBManager.getInstance().getUserDAO().findByEmail(email);
        if (utente == null || !BCrypt.checkpw(password, utente.getPassword())) {
            throw new AuthenticationException("Credenziali non valide");
        }
        return utente;
    }

    public void updateEmail(int id, String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Email non valida");
        }
        DBManager.getInstance().getUserDAO().updateEmail(id, newEmail);
    }

    public Utente findById(int id) {
        Utente utente = DBManager.getInstance().getUserDAO().findById(id);
        if (utente == null) {
            throw new NotFoundException("Utente non trovato");
        }
        return utente;
    }

    public List<Utente> getAllUtenti() {
        return DBManager.getInstance().getUserDAO().findAll();
    }

    public void aggiungiIndirizzoSpedizione(int utenteId, Indirizzo indirizzo) {
        DBManager.getInstance().getAddressDAO().addIndirizzo(indirizzo, utenteId);
    }


    public void changePassword(int userId, String currentPassword, String newPassword) {
        if(currentPassword.equals(newPassword)){
            throw new IllegalArgumentException("Le password sono uguali");
        }
        DBManager.getInstance().getUserDAO().updatePassword(userId, newPassword);
    }
}


