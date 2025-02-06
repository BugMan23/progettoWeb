package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Indirizzo;

import java.util.List;

public interface IndirizzoDAO {
    List<Indirizzo> findByUtenteId(int utenteId);
    void addIndirizzo(Indirizzo indirizzo, int utenteId);
}
