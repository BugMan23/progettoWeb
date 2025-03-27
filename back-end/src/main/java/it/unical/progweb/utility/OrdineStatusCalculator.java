package it.unical.progweb.utility;

import it.unical.progweb.model.Ordine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Classe di utilità per calcolare lo stato attuale di un ordine
 * in base alla data di creazione e al tempo trascorso.
 */
public class OrdineStatusCalculator {

    // Definizione degli stati
    public static final String STATO_CONFERMATO = "CONFERMATO";
    public static final String STATO_IN_ELABORAZIONE = "IN_ELABORAZIONE";
    public static final String STATO_SPEDITO = "SPEDITO";
    public static final String STATO_CONSEGNATO = "CONSEGNATO";
    public static final String STATO_COMPLETATO = "COMPLETATO";

    // Periodi di tempo per il passaggio di stato (in giorni lavorativi)
    private static final int GIORNI_ELABORAZIONE = 1;  // 1 giorno per il passaggio a IN_ELABORAZIONE
    private static final int GIORNI_SPEDIZIONE = 2;    // 2 giorni per il passaggio a SPEDITO
    private static final int GIORNI_CONSEGNA = 5;      // 5 giorni per il passaggio a CONSEGNATO
    private static final int GIORNI_COMPLETAMENTO = 14; // 14 giorni per il passaggio a COMPLETATO

    /**
     * Calcola lo stato corrente dell'ordine in base alla sua data
     * e allo stato attuale.
     *
     * @param ordine L'ordine di cui calcolare lo stato
     * @return Lo stato aggiornato dell'ordine
     */
    public static String calcolaStatoOrdine(Ordine ordine) {
        // Ottieni la data dell'ordine
        LocalDate dataOrdine = parseDataOrdine(ordine.getData());
        if (dataOrdine == null) {
            System.err.println("Impossibile parsare la data dell'ordine: " + ordine.getData());
            return ordine.getStato(); // Mantieni lo stato attuale
        }

        // Calcola i giorni trascorsi dall'ordine
        long giorniTrascorsi = ChronoUnit.DAYS.between(dataOrdine, LocalDate.now());
        System.out.println("Ordine #" + ordine.getId() + " - Giorni trascorsi: " + giorniTrascorsi);

        // Determina il nuovo stato in base ai giorni trascorsi
        if (giorniTrascorsi >= GIORNI_COMPLETAMENTO) {
            return STATO_COMPLETATO;
        } else if (giorniTrascorsi >= GIORNI_CONSEGNA) {
            return STATO_CONSEGNATO;
        } else if (giorniTrascorsi >= GIORNI_SPEDIZIONE) {
            return STATO_SPEDITO;
        } else if (giorniTrascorsi >= GIORNI_ELABORAZIONE) {
            return STATO_IN_ELABORAZIONE;
        } else {
            return STATO_CONFERMATO;
        }
    }

    /**
     * Tenta di analizzare la data dell'ordine con vari formati
     * per gestire diversi possibili formati di input.
     *
     * @param dataString La stringa contenente la data dell'ordine
     * @return La data dell'ordine come LocalDate o null se non è possibile parsarla
     */
    private static LocalDate parseDataOrdine(String dataString) {
        if (dataString == null || dataString.trim().isEmpty()) {
            return LocalDate.now(); // Default a oggi se la data è vuota
        }

        // Pulisci la stringa della data
        dataString = dataString.trim();

        // Lista di possibili formati da provare
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        );

        // Prova ciascun formato
        for (DateTimeFormatter formatter : formatters) {
            try {
                // Se la data contiene anche l'ora
                if (dataString.contains(":")) {
                    return LocalDateTime.parse(dataString, formatter).toLocalDate();
                } else {
                    return LocalDate.parse(dataString, formatter);
                }
            } catch (DateTimeParseException e) {
                // Continua con il prossimo formato
                System.out.println("Formato " + formatter.toString() + " non valido per la data " + dataString);
            }
        }

        // Se tutti i tentativi falliscono, prova a estrarre la data in formato dd/MM/yyyy
        try {
            // Pattern per estrarre date in formato dd/MM/yyyy
            if (dataString.matches(".*\\d{1,2}/\\d{1,2}/\\d{4}.*")) {
                String[] parts = dataString.split("\\s+");
                for (String part : parts) {
                    if (part.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                        return LocalDate.parse(part, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nell'analisi della data con regex: " + e.getMessage());
        }

        System.err.println("Tutti i formati standard falliti per la data: " + dataString);

        // Se tutti i formati falliscono, ritorna la data attuale
        return LocalDate.now();
    }
}