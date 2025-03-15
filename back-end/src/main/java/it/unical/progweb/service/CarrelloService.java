package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.CarrelloDAO;
import it.unical.progweb.persistence.dao.DisponibilitaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrelloService {

    private final CarrelloDAO carrelloDAO;
    private final DisponibilitaDAO disponibilitaDAO;

    @Autowired
    public CarrelloService(CarrelloDAO carrelloDAO, DisponibilitaDAO disponibilitaDAO) {
        this.carrelloDAO = carrelloDAO;
        this.disponibilitaDAO = disponibilitaDAO;
    }

    public void addAlCarrello(int userId, int productId, int quantity, String taglia) {
        // Verifica disponibilità
        List<Disponibilita> disponibilita = disponibilitaDAO.findByProdottoId(productId);

        boolean disponibile = false;
        for (Disponibilita d : disponibilita) {
            if (d.getTaglia().equals(taglia) && d.getQuantita() >= quantity) {
                disponibile = true;
                break;
            }
        }

        if (!disponibile) {
            throw new NotFoundException("Quantità non disponibile");
        }

        carrelloDAO.addAlCarrello(userId, productId, quantity);
    }

    public List<Prodotto> getCart(int userId) {
        return carrelloDAO.getCarrello(userId);
    }

    public void clearCart(int userId) {
        carrelloDAO.clear(userId);
    }

    public int getCartTotal(int userId) {
        List<Prodotto> carrello = carrelloDAO.getCarrello(userId);
        int total = 0;
        for (Prodotto prodotto : carrello) {
            total += prodotto.getPrezzo();
        }
        return total;
    }
}