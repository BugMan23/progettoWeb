package it.unical.progweb.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Classe di utilità per la gestione delle date nell'applicazione.
 */
public class DateUtils {

    // Formato date standard utilizzato nell'applicazione
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    // Lista di formati supportati per il parsing delle date
    private static final List<String> SUPPORTED_DATE_FORMATS = Arrays.asList(
            "dd/MM/yyyy HH:mm",
            "yyyy-MM-dd HH:mm",
            "dd-MM-yyyy HH:mm",
            "dd/MM/yyyy",
            "yyyy-MM-dd",
            "dd-MM-yyyy",
            "yyyy/MM/dd"
    );

    /**
     * Formatta una data LocalDate nel formato standard dell'applicazione
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * Formatta una data LocalDateTime nel formato standard dell'applicazione
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }

    /**
     * Tenta di analizzare una stringa data con i formati supportati
     *
     * @param dateString La stringa da parsare
     * @return Un oggetto LocalDate se il parsing ha successo, altrimenti null
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // Rimuovi eventuali spazi iniziali o finali
        dateString = dateString.trim();

        // Se la data include l'ora, rimuovila per ottenere solo la data
        if (dateString.contains(":")) {
            String[] parts = dateString.split(" ");
            if (parts.length > 0) {
                dateString = parts[0];
            }
        }

        // Prova tutti i formati supportati
        for (String format : SUPPORTED_DATE_FORMATS) {
            // Salta i formati con ora se stiamo cercando solo la data
            if (format.contains("HH:mm")) {
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Continua con il prossimo formato
            }
        }

        return null;
    }

    /**
     * Tenta di analizzare una stringa datetime con i formati supportati
     *
     * @param dateTimeString La stringa da parsare
     * @return Un oggetto LocalDateTime se il parsing ha successo, altrimenti null
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }

        // Rimuovi eventuali spazi iniziali o finali
        dateTimeString = dateTimeString.trim();

        // Prova tutti i formati supportati
        for (String format : SUPPORTED_DATE_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

                // Per i formati senza ora, aggiungi l'ora
                if (!format.contains("HH:mm") && !dateTimeString.contains(":")) {
                    return LocalDate.parse(dateTimeString, formatter).atStartOfDay();
                } else if (format.contains("HH:mm") && dateTimeString.contains(":")) {
                    return LocalDateTime.parse(dateTimeString, formatter);
                }
            } catch (DateTimeParseException e) {
                // Continua con il prossimo formato
            }
        }

        return null;
    }

    /**
     * Converte una stringa data in un'altra con un formato diverso
     *
     * @param dateString La stringa data da convertire
     * @param targetFormat Il formato di destinazione
     * @return La stringa data convertita o la stringa originale se la conversione fallisce
     */
    public static String convertDateFormat(String dateString, String targetFormat) {
        LocalDate date = parseDate(dateString);
        if (date != null) {
            try {
                return date.format(DateTimeFormatter.ofPattern(targetFormat));
            } catch (Exception e) {
                return dateString;
            }
        }

        LocalDateTime dateTime = parseDateTime(dateString);
        if (dateTime != null) {
            try {
                return dateTime.format(DateTimeFormatter.ofPattern(targetFormat));
            } catch (Exception e) {
                return dateString;
            }
        }

        return dateString;
    }

    /**
     * Controlla se una stringa rappresenta una data valida nei formati supportati
     */
    public static boolean isValidDate(String dateString) {
        return parseDate(dateString) != null;
    }

    /**
     * Controlla se una stringa rappresenta un datetime valido nei formati supportati
     */
    public static boolean isValidDateTime(String dateTimeString) {
        return parseDateTime(dateTimeString) != null;
    }
}