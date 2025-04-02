package it.unical.progweb.service;

import it.unical.progweb.eccezioni.AuthenticationException;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.CarrelloDAO;
import it.unical.progweb.persistence.dao.IndirizzoDAO;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UtenteDAO utenteDAO;
    private final IndirizzoDAO indirizzoDAO;
    private final CarrelloDAO carrelloDao;

    @Autowired
    public UserService(UtenteDAO utenteDAO, IndirizzoDAO indirizzoDAO, CarrelloDAO carrelloDAO) {
        this.utenteDAO = utenteDAO;
        this.indirizzoDAO = indirizzoDAO;
        this.carrelloDao = carrelloDAO;
    }

    public void registraUtente(Utente utente) {
        // Validazione più dettagliata
        if (utente == null) {
            throw new IllegalArgumentException("Dati utente nulli");
        }

        if (utente.getNome() == null || utente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome obbligatorio");
        }

        if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) {
            throw new IllegalArgumentException("Cognome obbligatorio");
        }

        if (utente.getEmail() == null || !utente.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email non valida");
        }

        if (utente.getPassword() == null || utente.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password non valida");
        }


        // Hash della password
        String hashedPassword = BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt());
        utente.setPassword(hashedPassword);

        // Salvataggio
        boolean salvato = utenteDAO.save(utente);
        if (!salvato) {
            throw new RuntimeException("Impossibile salvare l'utente");
        }

        Utente utenteRegistrato = utenteDAO.findByEmail(utente.getEmail());
        if (utenteRegistrato != null) {
            // Inizializza il carrello vuoto
            carrelloDao.initializeEmptyCart(utenteRegistrato.getId());
        }
    }

    public Utente login(String email, String password) {
        Utente utente = utenteDAO.findByEmail(email);
        if (utente == null) {
            throw new AuthenticationException("Utente non trovato");
        }

        // Usa BCrypt per verificare la password
        if (!BCrypt.checkpw(password, utente.getPassword())) {
            throw new AuthenticationException("Password non corretta");
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


    public Utente findByEmail(String email) {
        Utente utente = utenteDAO.findByEmail(email);
        if (utente == null) {
            throw new NotFoundException("Utente non trovato");
        }
        return utente;
    }

    public List<Indirizzo> getUserAddresses(int utenteId) {
        List<Indirizzo> ind = new ArrayList<>();
        return ind;
    }

    public void eliminaUtente(int id) {
        Utente utente = findById(id);
        if (utente == null) {
            throw new NotFoundException("Utente non trovato");
        }
        utenteDAO.elimina(id);
    }



}