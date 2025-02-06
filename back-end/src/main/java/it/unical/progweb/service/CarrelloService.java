package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrelloService {

    public void addAlCarrello(int userId, int productId, int quantity, String taglia) {
        // Verifica disponibilità
        List<Disponibilita> disponibilita = DBManager.getInstance()
                .getDisponibilitaDAO().findByProdottoId(productId);

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

        DBManager.getInstance().getCartDAO().addAlCarrello(userId, productId, quantity);
    }

    public List<Prodotto> getCart(int userId) {
        return DBManager.getInstance().getCartDAO().getCarrello(userId);
    }

    public void clearCart(int userId) {
        DBManager.getInstance().getCartDAO().clear(userId);
    }

    public int getCartTotal(int userId) {
        List<Prodotto> carrello = DBManager.getInstance().getCartDAO().getCarrello(userId);
        int total = 0;
        for (Prodotto prodotto : carrello) {
            total += prodotto.getPrezzo();
        }
        return total;
    }
}
