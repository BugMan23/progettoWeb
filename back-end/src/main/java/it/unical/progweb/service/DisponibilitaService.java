package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilitaService {

    // recupera lista disponibilità per un prodotto
    public List<Disponibilita> getDisponibilitaProdotto(int prodottoId) {
        return DBManager.getInstance().getDisponibilitaDAO().findByProdottoId(prodottoId);
    }

    // aggiorna quantità per taglia specifica
    public void updateDisponibilita(int prodottoId, int quantita, String taglia) {
        validateQuantita(quantita);
        DBManager.getInstance().getDisponibilitaDAO().updateDisponibilita(prodottoId, quantita, taglia);
    }

    // Riduce disponibilità dopo acquisto, con validazione
    public void decrementaQuantita(int prodottoId, int quantita, String taglia) {
        List<Disponibilita> disponibilita = getDisponibilitaProdotto(prodottoId);
        validateDisponibilita(disponibilita, quantita, taglia);
        DBManager.getInstance().getDisponibilitaDAO().decrementaQuantita(prodottoId, quantita, taglia);
    }

    private void validateQuantita(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità non può essere negativa");
        }
    }

    private void validateDisponibilita(List<Disponibilita> disponibilita, int quantita, String taglia) {
        boolean disponibile = disponibilita.stream()
                .anyMatch(d -> d.getTaglia().equals(taglia) && d.getQuantita() >= quantita);


        if (!disponibile) {
            throw new NotFoundException("Quantità richiesta non disponibile per la taglia selezionata");
        }
    }
}


