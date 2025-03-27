package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.DettagliOrdineDAO;
import it.unical.progweb.persistence.dao.OrdineDAO;
import it.unical.progweb.persistence.dao.ProdottoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrdineService {

    private final OrdineDAO ordineDAO;
    private final DettagliOrdineDAO dettagliOrdineDAO;
    private final ProdottoDAO prodottoDAO;

    @Autowired
    public OrdineService(OrdineDAO ordineDAO, DettagliOrdineDAO dettagliOrdineDAO, ProdottoDAO prodottoDAO) {
        this.ordineDAO = ordineDAO;
        this.dettagliOrdineDAO = dettagliOrdineDAO;
        this.prodottoDAO = prodottoDAO;
    }

    /**
     * Crea un nuovo ordine e ne restituisce l'ID
     */
    // Nel file back-end/src/main/java/it/unical/progweb/service/OrdineService.java
    public int createOrder(int userId, int idMetodoPagamento, List<DettagliOrdini> articoliCarrello) {
        validazioneArticoliNelCarrello(articoliCarrello);
        int totaleDaPagare = calcolaTotale(articoliCarrello);

        Ordine ordine = new Ordine(
                0,
                userId,
                LocalDate.now().toString(),
                "IN_ELABORAZIONE",
                totaleDaPagare,
                idMetodoPagamento
        );

        int ordineId = ordineDAO.creaOrdine(ordine);
        ordine.setId(ordineId);

        for (DettagliOrdini item : articoliCarrello) {
            dettagliOrdineDAO.addArticoliOrdine(ordine.getId(), item.getIdProdotto(), item.getQuantita());
        }

        return ordineId;  // Ritorna l'ID dell'ordine
    }
    /**
     * Restituisce tutti gli ordini di un utente
     */
    public List<Ordine> getUserOrders(int userId) {
        return ordineDAO.getOrdiniByIdUtente(userId);
    }

    /**
     * Restituisce i dettagli di un ordine specifico
     */
    public Ordine getOrderById(int orderId) {
        Ordine ordine = ordineDAO.findById(orderId);
        if (ordine == null) {
            throw new NotFoundException("Ordine non trovato con ID: " + orderId);
        }
        return ordine;
    }

    /**
     * Verifica la validità degli articoli nel carrello
     */
    private void validazioneArticoliNelCarrello(List<DettagliOrdini> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Il carrello è vuoto");
        }

        // Verifica che tutti gli articoli abbiano un ID prodotto valido
        for (DettagliOrdini item : items) {
            if (item.getIdProdotto() <= 0) {
                throw new IllegalArgumentException("ID prodotto non valido: " + item.getIdProdotto());
            }
            if (item.getQuantita() <= 0) {
                throw new IllegalArgumentException("Quantità non valida per il prodotto: " + item.getIdProdotto());
            }
        }
    }

    /**
     * Calcola il totale dell'ordine
     */
    private int calcolaTotale(List<DettagliOrdini> items) {
        int totale = 0;
        for (DettagliOrdini item : items) {
            Prodotto prodotto = prodottoDAO.findById(item.getIdProdotto());
            if (prodotto == null) {
                throw new NotFoundException("Prodotto non trovato con ID: " + item.getIdProdotto());
            }
            totale += prodotto.getPrezzo() * item.getQuantita();
        }
        return totale;
    }

    /**
     * Ottiene i dettagli di un ordine specifico
     */
    public List<DettagliOrdini> getOrderDetails(int orderId) {
        List<DettagliOrdini> dettagli = dettagliOrdineDAO.findByOrderId(orderId);
        if (dettagli.isEmpty()) {
            throw new NotFoundException("Nessun dettaglio trovato per l'ordine con ID: " + orderId);
        }
        return dettagli;
    }
}