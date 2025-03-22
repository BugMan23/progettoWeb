package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.CarrelloDAO;
import it.unical.progweb.persistence.dao.DisponibilitaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        // Prima controlla se l'utente ha un carrello
        List<Prodotto> carrello = carrelloDAO.getCarrello(userId);

        // Se non ha un carrello, inizializzane uno vuoto
        if (carrello == null) {
            carrelloDAO.initializeEmptyCart(userId);
            return new ArrayList<>(); // Restituisci una lista vuota
        }

        return carrello;
    }

    public List<Carrello> getCartDetails(int userId) {
        List<Carrello> dettagli = carrelloDAO.getCartDetails(userId);

        if (dettagli == null) {
            return new ArrayList<>(); // Restituisci una lista vuota se non ci sono dettagli
        }

        return dettagli;
    }

    public void clearCart(int userId) {
        carrelloDAO.clear(userId);
    }

    public int getCartTotal(int userId) {
        // Se non ci sono prodotti, il totale è 0
        List<Prodotto> carrello = carrelloDAO.getCarrello(userId);
        if (carrello == null || carrello.isEmpty()) {
            return 0;
        }

        // Altrimenti calcola il totale come prima
        int total = 0;
        for (Prodotto prodotto : carrello) {
            total += prodotto.getPrezzo();
        }
        return total;
    }

    public int getCartCount(int userId) {
        List<Prodotto> carrello = carrelloDAO.getCarrello(userId);
        return carrello == null ? 0 : carrello.size();
    }

    public void removeFromCart(int userId, int productId) {
        carrelloDAO.removeFromCart(userId, productId);
    }

    public void updateCartItem(int userId, int productId, int quantity, String taglia) {
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

        carrelloDAO.updateCartItem(userId, productId, quantity, taglia);
    }

    public void updateCartItemTaglia(int userId, int productId, String taglia) {
        // Verifica disponibilità
        List<Disponibilita> disponibilita = disponibilitaDAO.findByProdottoId(productId);
        boolean tagliaDisponibile = disponibilita.stream()
                .anyMatch(d -> d.getTaglia().equals(taglia) && d.getQuantita() > 0);

        if (!tagliaDisponibile) {
            throw new NotFoundException("Taglia non disponibile");
        }

        carrelloDAO.updateCartItemTaglia(userId, productId, taglia);
    }

    public void initializeEmptyCart(int userId) {
        carrelloDAO.initializeEmptyCart(userId);
    }
}