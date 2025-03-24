package it.unical.progweb.service;

import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.persistence.dao.IndirizzoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndirizzoService {

    private final IndirizzoDAO indirizzoDAO;

    @Autowired
    public IndirizzoService(IndirizzoDAO indirizzoDAO) {
        this.indirizzoDAO = indirizzoDAO;
    }

    public List<Indirizzo> getIndirizziByUtenteId(int utenteId) {
        System.out.println("Service: Richiesta indirizzi per utente ID " + utenteId);
        List<Indirizzo> indirizzi = indirizzoDAO.findByUtenteId(utenteId);
        System.out.println("Service: Trovati " + indirizzi.size() + " indirizzi");
        return indirizzi;
    }

    public void addIndirizzo(Indirizzo indirizzo, int utenteId) {
        System.out.println("Service: Aggiunta indirizzo per utente ID " + utenteId);
        System.out.println("Service: Dettagli indirizzo: " + indirizzo.getNomeVia() + ", " + indirizzo.getCitta());
        indirizzoDAO.addIndirizzo(indirizzo, utenteId);
    }
}