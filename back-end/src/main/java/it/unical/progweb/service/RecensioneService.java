package it.unical.progweb.service;

import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Recensione;
import it.unical.progweb.persistence.dao.DettagliOrdineDAO;
import it.unical.progweb.persistence.dao.OrdineDAO;
import it.unical.progweb.persistence.dao.RecensioneDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecensioneService {

    private final RecensioneDAO recensioneDAO;
    private final OrdineDAO ordineDAO;
    private final DettagliOrdineDAO dettagliOrdineDAO;

    @Autowired
    public RecensioneService(RecensioneDAO recensioneDAO, OrdineDAO ordineDAO, DettagliOrdineDAO dettagliOrdineDAO) {
        this.recensioneDAO = recensioneDAO;
        this.ordineDAO = ordineDAO;
        this.dettagliOrdineDAO = dettagliOrdineDAO;
    }

    // Aggiunge una recensione dopo verifica
    public void addReview(Recensione recensione) {
        validateReview(recensione);
        checkUserPurchasedProduct(recensione.getIdUtente(), recensione.getIdProdotto());
        recensione.setData(LocalDate.now().toString());
        recensioneDAO.addRecensione(recensione);
    }

    // Ottiene le recensioni di un prodotto
    public List<Recensione> getProductReviews(int productId) {
        return recensioneDAO.findByProdottoId(productId);
    }

    // Ottiene le recensioni di un utente
    public List<Recensione> getUserReviews(int userId) {
        return recensioneDAO.findByUserId(userId);
    }

    // Ottiene le recensioni di un utente per un prodotto specifico
    public List<Recensione> getUserProductReviews(int userId, int productId) {
        return recensioneDAO.findByUserAndProduct(userId, productId);
    }

    // Elimina una recensione
    public void deleteReview(int reviewId) {
        recensioneDAO.deleteRecensione(reviewId);
    }

    private void validateReview(Recensione recensione) {
        if (recensione.getValutazione() < 1 || recensione.getValutazione() > 5) {
            throw new IllegalArgumentException("La valutazione deve essere tra 1 e 5 stelle");
        }

        if (recensione.getTesto() == null || recensione.getTesto().trim().length() < 10) {
            throw new IllegalArgumentException("Il testo deve essere di almeno 10 caratteri");
        }
    }

    private void checkUserPurchasedProduct(int userId, int productId) {
        // Ottieni tutti gli ordini dell'utente
        List<Ordine> ordini = ordineDAO.findByUserId(userId);

        boolean hasPurchased = false;

        // Per ogni ordine, verifica i dettagli ordine per il prodotto
        for (Ordine ordine : ordini) {
            List<DettagliOrdini> dettagliOrdini = dettagliOrdineDAO.findByOrderId(ordine.getId());
            for (DettagliOrdini dettaglio : dettagliOrdini) {
                if (dettaglio.getIdProdotto() == productId) {
                    hasPurchased = true;
                    break;
                }
            }
            if (hasPurchased) {
                break;
            }
        }

        if (!hasPurchased) {
            throw new IllegalStateException("Puoi recensire solo prodotti che hai acquistato");
        }
    }
}