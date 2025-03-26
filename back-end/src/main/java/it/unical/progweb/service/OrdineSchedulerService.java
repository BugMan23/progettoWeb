package it.unical.progweb.service;

import it.unical.progweb.model.Ordine;
import it.unical.progweb.persistence.dao.OrdineDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OrdineSchedulerService {

    @Autowired
    private OrdineDAO ordineDAO;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Esegui automaticamente ogni notte alle 1:00
    @Scheduled(cron = "0 0 1 * * ?")
    public void aggiornaStatoOrdini() {
        List<Ordine> ordini = ordineDAO.findAll();

        LocalDate oggi = LocalDate.now();

        for (Ordine ordine : ordini) {
            try {
                // Converti la data dell'ordine in LocalDate
                LocalDate dataOrdine = LocalDate.parse(ordine.getData(), dateFormatter);

                // Calcola i giorni lavorativi trascorsi
                long giorniLavorativi = calcolaGiorniLavorativi(dataOrdine, oggi);

                // Aggiorna lo stato in base ai giorni lavorativi
                String nuovoStato = calcolaNuovoStato(giorniLavorativi);

                // Se lo stato è cambiato, aggiornalo
                if (!ordine.getStato().equals(nuovoStato)) {
                    ordine.setStato(nuovoStato);
                    ordineDAO.updateStatus(ordine.getId(), nuovoStato);
                }
            } catch (Exception e) {
                // Log dell'errore ma continua con gli altri ordini
                System.err.println("Errore nell'aggiornamento dello stato per l'ordine " + ordine.getId() + ": " + e.getMessage());
            }
        }
    }

    private String calcolaNuovoStato(long giorniLavorativi) {
        if (giorniLavorativi <= 1) {
            return "IN_ELABORAZIONE";
        } else if (giorniLavorativi <= 3) {
            return "IN_PREPARAZIONE";
        } else if (giorniLavorativi <= 5) {
            return "SPEDITO";
        } else {
            return "CONSEGNATO";
        }
    }

    private long calcolaGiorniLavorativi(LocalDate dataInizio, LocalDate dataFine) {
        long giorni = 0;
        LocalDate data = dataInizio;

        while (data.isBefore(dataFine) || data.isEqual(dataFine)) {
            // Controlla se è un giorno lavorativo (dal lunedì al venerdì)
            if (data.getDayOfWeek() != DayOfWeek.SATURDAY && data.getDayOfWeek() != DayOfWeek.SUNDAY) {
                giorni++;
            }
            data = data.plusDays(1);
        }

        return giorni;
    }
}