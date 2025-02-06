package it.unical.progweb.service;

import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Recensione;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecensioneService {

    // Aggiunge una recensione dopo verifica
    public void addReview(Recensione recensione) {
        validateReview(recensione);
        checkUserPurchasedProduct(recensione.getIdUtente(), recensione.getIdProdotto());
        recensione.setData(LocalDate.now().toString());
        DBManager.getInstance().getReviewDAO().addRecensione(recensione);
    }

    // Ottiene le recensioni di un prodotto
    public List<Recensione> getProductReviews(int productId) {
        return DBManager.getInstance().getReviewDAO().findByProdottoId(productId);
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
        List<Ordine> ordini = DBManager.getInstance().getOrderDAO().findByUserId(userId);

        boolean hasPurchased = false;

        // Per ogni ordine, verifica i dettagli ordine per il prodotto
        for (Ordine ordine : ordini) {
            List<DettagliOrdini> dettagliOrdini = DBManager.getInstance().getDettagliOrdiniDAO().findByOrderId(ordine.getId());
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