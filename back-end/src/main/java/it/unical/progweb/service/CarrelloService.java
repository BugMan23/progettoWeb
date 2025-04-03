package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Carrello;
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
        if (userId <= 0 || productId <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        if (taglia == null || taglia.trim().isEmpty()) {
            taglia = "M";
        }

        List<Disponibilita> disponibilita = disponibilitaDAO.findByProdottoId(productId);
        boolean disponibile = false;

        if (disponibilita.isEmpty()) {
            throw new NotFoundException("Prodotto non disponibile");
        }

        for (Disponibilita d : disponibilita) {
            if (d.getTaglia().equals(taglia) && d.getQuantita() >= quantity) {
                disponibile = true;
                break;
            }
        }

        if (!disponibile) {
            throw new NotFoundException("Quantità o taglia non disponibile");
        }

        carrelloDAO.addAlCarrello(userId, productId, quantity);
    }

    public List<Prodotto> getCart(int userId) {
        return carrelloDAO.getCarrello(userId);
    }

    public List<Carrello> getCartDetails(int userId) {
        return carrelloDAO.getCartDetails(userId);
    }

    /**
     * Contrassegna gli articoli del carrello come ordinati invece di eliminarli
     * Questo mantiene lo storico e permette una migliore tracciabilità
     */
    public void clearCart(int userId) {
        System.out.println("Contrassegno gli articoli del carrello come ordinati per l'utente: " + userId);
        carrelloDAO.clear(userId);
    }

    public int getCartTotal(int userId) {
        List<Prodotto> carrello = carrelloDAO.getCarrello(userId);
        List<Carrello> dettagli = carrelloDAO.getCartDetails(userId);

        int total = 0;
        for (Prodotto prodotto : carrello) {
            int quantity = dettagli.stream()
                    .filter(d -> d.getIdProdotto() == prodotto.getId())
                    .findFirst()
                    .map(Carrello::getQuantita)
                    .orElse(1);

            total += prodotto.getPrezzo() * quantity;
        }
        return total;
    }

    public int getCartCount(int userId) {
        List<Prodotto> carrello = carrelloDAO.getCarrello(userId);
        return carrello.size();
    }

    public void removeFromCart(int userId, int productId) {
        carrelloDAO.removeFromCart(userId, productId);
    }

    public void updateCartItem(int userId, int productId, int quantity, String taglia) {
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
        List<Disponibilita> disponibilita = disponibilitaDAO.findByProdottoId(productId);
        boolean tagliaDisponibile = disponibilita.stream()
                .anyMatch(d -> d.getTaglia().equals(taglia) && d.getQuantita() > 0);

        if (!tagliaDisponibile) {
            throw new NotFoundException("Taglia non disponibile");
        }

        carrelloDAO.updateCartItemTaglia(userId, productId, taglia);
    }
}