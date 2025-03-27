package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.DettagliOrdineDAO;
import it.unical.progweb.persistence.dao.OrdineDAO;
import it.unical.progweb.persistence.dao.ProdottoDAO;
import it.unical.progweb.utility.OrdineStatusCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     * Crea un nuovo ordine e restituisce l'ID dell'ordine creato
     */
    public int createOrder(int userId, int idMetodoPagamento, List<DettagliOrdini> articoliCarrello) {
        // Validazione degli articoli nel carrello
        validazioneArticoliNelCarrello(articoliCarrello);

        // Calcolo del totale da pagare
        int totaleDaPagare = calcolaTotale(articoliCarrello);

        // Genera la data attuale in formato dd/MM/yyyy HH:mm
        LocalDateTime now = LocalDateTime.now();
        String currentDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        // Creazione dell'oggetto ordine
        Ordine ordine = new Ordine(
                0, // ID sarà generato dal database
                userId,
                currentDate, // Data formattata
                OrdineStatusCalculator.STATO_CONFERMATO, // Stato iniziale
                totaleDaPagare,
                idMetodoPagamento
        );

        // Salva l'ordine nel database e ottieni l'ID generato
        int ordineId = ordineDAO.creaOrdine(ordine);

        // Log l'ID dell'ordine creato
        System.out.println("Ordine creato con ID: " + ordineId);

        // Imposta l'ID nell'oggetto ordine
        ordine.setId(ordineId);

        // Salva gli articoli dell'ordine
        for (DettagliOrdini item : articoliCarrello) {
            dettagliOrdineDAO.addArticoliOrdine(ordineId, item.getIdProdotto(), item.getQuantita());
        }

        // Restituisci l'ID dell'ordine creato
        return ordineId;
    }

    /**
     * Ottiene gli ordini di un utente
     */
    public List<Ordine> getUserOrders(int userId) {
        // Recupera gli ordini dell'utente dal database
        List<Ordine> ordini = ordineDAO.findByUserId(userId);

        // Log per debug
        System.out.println("Trovati " + ordini.size() + " ordini per l'utente " + userId);

        // Aggiorna lo stato di ogni ordine in base alla data
        for (Ordine ordine : ordini) {
            String statoOriginale = ordine.getStato();

            // Calcola lo stato aggiornato dell'ordine
            String statoAggiornato = OrdineStatusCalculator.calcolaStatoOrdine(ordine);
            ordine.setStato(statoAggiornato);

            // Log per debug
            System.out.println("Ordine #" + ordine.getId() +
                    " - Data: " + ordine.getData() +
                    " - Stato: " + statoOriginale + " → " + statoAggiornato);
        }

        return ordini;
    }

    /**
     * Ottiene un ordine specifico per ID
     */
    public Ordine getOrderById(int orderId) {
        Ordine ordine = ordineDAO.findById(orderId);
        if (ordine == null) {
            throw new NotFoundException("Ordine non trovato con ID: " + orderId);
        }

        // Calcola lo stato aggiornato dell'ordine
        String statoOriginale = ordine.getStato();
        String statoAggiornato = OrdineStatusCalculator.calcolaStatoOrdine(ordine);
        ordine.setStato(statoAggiornato);

        // Log per debug
        System.out.println("Ordine #" + ordine.getId() +
                " - Data: " + ordine.getData() +
                " - Stato: " + statoOriginale + " → " + statoAggiornato);

        return ordine;
    }

    /**
     * Valida gli articoli nel carrello
     */
    private void validazioneArticoliNelCarrello(List<DettagliOrdini> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Il carrello è vuoto");
        }

        // Validazione opzionale: verifica che ogni articolo abbia una quantità positiva
        for (DettagliOrdini item : items) {
            if (item.getQuantita() <= 0) {
                throw new IllegalArgumentException("La quantità deve essere maggiore di zero");
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
            System.out.println("Nessun dettaglio trovato per l'ordine ID: " + orderId);
        } else {
            System.out.println("Trovati " + dettagli.size() + " dettagli per l'ordine ID: " + orderId);
        }
        return dettagli;
    }

    /**
     * Verifica se un utente ha acquistato un prodotto
     */
    public boolean haUserPurchasedProduct(int userId, int productId) {
        // Ottieni tutti gli ordini dell'utente
        List<Ordine> ordini = ordineDAO.findByUserId(userId);

        // Per ogni ordine, verifica i dettagli per il prodotto
        for (Ordine ordine : ordini) {
            List<DettagliOrdini> dettagliOrdini = dettagliOrdineDAO.findByOrderId(ordine.getId());
            for (DettagliOrdini dettaglio : dettagliOrdini) {
                if (dettaglio.getIdProdotto() == productId) {
                    return true; // Prodotto trovato negli ordini dell'utente
                }
            }
        }

        return false; // Prodotto non trovato negli ordini dell'utente
    }
}