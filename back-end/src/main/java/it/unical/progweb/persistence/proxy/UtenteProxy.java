package it.unical.progweb.persistence.proxy;

import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtenteProxy {
    private final UtenteDAO utenteDAO;

    public UtenteProxy(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
        System.out.println("✅ UtenteProxy creato con successo!");
    }

    public Utente findById(int id) {
        System.out.println("Proxy: Cercando utente con ID " + id);
        return utenteDAO.findById(id);
    }

    public List<Utente> findAll() {
        System.out.println("Proxy: Recuperando tutti gli utenti...");
        return utenteDAO.findAll();
    }

    public boolean save(Utente utente) {
        System.out.println("Proxy: Salvataggio utente in corso...");
        return utenteDAO.save(utente);
    }

    public Utente validateUser(String email, String password) {
        System.out.println("Proxy: Validazione utente in corso...");
        return utenteDAO.validateUser(email, password);
    }
}
