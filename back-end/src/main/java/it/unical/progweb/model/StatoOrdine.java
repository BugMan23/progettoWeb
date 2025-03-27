package it.unical.progweb.model;

public enum StatoOrdine {
    CONFERMATO("Confermato"),
    IN_PREPARAZIONE("In Preparazione"),
    SPEDITO("Spedito"),
    CONSEGNATO("Consegnato");

    private final String descrizione;

    StatoOrdine(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}