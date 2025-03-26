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
import java.util.Arrays;
import java.util.List;

@Service
public class OrdineService {

    private final OrdineDAO ordineDAO;
    private final DettagliOrdineDAO dettagliOrdineDAO;
    private final ProdottoDAO prodottoDAO;

    // Valori validi per lo stato dell'ordine
    private static final List<String> STATI_VALIDI = Arrays.asList(
            "IN_ELABORAZIONE", "SPEDITO", "CONSEGNATO", "ANNULLATO"
    );

    @Autowired
    public OrdineService(OrdineDAO ordineDAO, DettagliOrdineDAO dettagliOrdineDAO, ProdottoDAO prodottoDAO) {
        this.ordineDAO = ordineDAO;
        this.dettagliOrdineDAO = dettagliOrdineDAO;
        this.prodottoDAO = prodottoDAO;
    }

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

        if (ordineId == -1) {
            throw new RuntimeException("Errore nella creazione dell'ordine");
        }

        ordine.setId(ordineId);

        for (DettagliOrdini item : articoliCarrello) {
            dettagliOrdineDAO.addArticoliOrdine(ordine.getId(), item.getIdProdotto(), item.getQuantita());
        }

        return ordineId;
    }

    public List<Ordine> getUserOrders(int userId) {
        return ordineDAO.getOrdiniByIdUtente(userId);
    }

    public Ordine getOrderById(int orderId) {
        Ordine ordine = ordineDAO.findById(orderId);
        if (ordine == null) {
            throw new NotFoundException("Ordine non trovato");
        }
        return ordine;
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        // Verifica se lo stato è valido
        if (!STATI_VALIDI.contains(newStatus)) {
            throw new IllegalArgumentException("Stato ordine non valido. Stati validi: " + String.join(", ", STATI_VALIDI));
        }

        // Verifica se l'ordine esiste
        Ordine ordine = getOrderById(orderId);
        if (ordine == null) {
            throw new RuntimeException("Errore nella ricerca dell'ordine");
        }

        // Aggiorna lo stato dell'ordine
        boolean updated = ordineDAO.updateStatus(orderId, newStatus);

        if (!updated) {
            throw new RuntimeException("Impossibile aggiornare lo stato dell'ordine");
        }
    }

    private void validazioneArticoliNelCarrello(List<DettagliOrdini> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Il carrello è vuoto");
        }
    }

    private int calcolaTotale(List<DettagliOrdini> items) {
        int totale = 0;
        for (DettagliOrdini item : items) {
            Prodotto prodotto = prodottoDAO.findById(item.getIdProdotto());
            totale += prodotto.getPrezzo() * item.getQuantita();
        }
        return totale;
    }

    public List<DettagliOrdini> getOrderDetails(int orderId) {
        // Verifica se l'ordine esiste
        getOrderById(orderId);

        return dettagliOrdineDAO.findByOrderId(orderId);
    }
}
