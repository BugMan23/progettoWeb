package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.persistence.dao.DisponibilitaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilitaService {

    private final DisponibilitaDAO disponibilitaDAO;

    @Autowired
    public DisponibilitaService(DisponibilitaDAO disponibilitaDAO) {
        this.disponibilitaDAO = disponibilitaDAO;
    }

    public List<Disponibilita> getDisponibilitaProdotto(int prodottoId) {
        return disponibilitaDAO.findByProdottoId(prodottoId);
    }

    public void updateDisponibilita(int prodottoId, int quantita, String taglia) {
        validateQuantita(quantita);
        disponibilitaDAO.updateDisponibilita(prodottoId, quantita, taglia);
    }

    public void decrementaQuantita(int prodottoId, int quantita, String taglia) {
        List<Disponibilita> disponibilita = getDisponibilitaProdotto(prodottoId);
        validateDisponibilita(disponibilita, quantita, taglia);
        disponibilitaDAO.decrementaQuantita(prodottoId, quantita, taglia);
    }

    private void validateQuantita(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità non può essere negativa");
        }
    }

    public void aggiungiDisponibilita(Disponibilita d) {
        disponibilitaDAO.addDisponibilita(d);
    }


    private void validateDisponibilita(List<Disponibilita> disponibilita, int quantita, String taglia) {
        boolean disponibile = disponibilita.stream()
                .anyMatch(d -> d.getTaglia().equals(taglia) && d.getQuantita() >= quantita);

        if (!disponibile) {
            throw new NotFoundException("Quantità richiesta non disponibile per la taglia selezionata");
        }
    }
}