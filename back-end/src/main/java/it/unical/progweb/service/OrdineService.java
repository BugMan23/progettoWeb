package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrdineService {

    public void createOrder(int userId, int idMetodoPagamento, List<DettagliOrdini> articoliCarrello) {
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

        int ordineId = DBManager.getInstance().getOrderDAO().creaOrdine(ordine);
        ordine.setId(ordineId);

        for(DettagliOrdini item : articoliCarrello) {
            DBManager.getInstance().getDettagliOrdiniDAO()
                    .addArticoliOrdine(ordine.getId(), item.getIdProdotto(), item.getQuantita());
        }
    }

    public List<Ordine> getUserOrders(int userId) {
        return DBManager.getInstance().getOrderDAO().getOrdiniByIdUtente(userId);
    }

    public Ordine getOrderById(int orderId) {
        Ordine ordine = DBManager.getInstance().getOrderDAO().findById(orderId);
        if (ordine == null) {
            throw new NotFoundException("Ordine non trovato");
        }
        return ordine;
    }

    private void validazioneArticoliNelCarrello(List<DettagliOrdini> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Il carrello è vuoto");
        }
    }

    private int calcolaTotale(List<DettagliOrdini> items) {
        int totale = 0;
        for (DettagliOrdini item : items) {
            Prodotto prodotto = DBManager.getInstance().getProductDAO().findById(item.getIdProdotto());
            totale += prodotto.getPrezzo() * item.getQuantita();
        }
        return totale;
    }

}