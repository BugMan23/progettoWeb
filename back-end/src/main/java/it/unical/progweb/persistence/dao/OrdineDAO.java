package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Ordine;

import java.util.List;

public interface OrdineDAO {

    int creaOrdine(Ordine ordine);
    List<Ordine> getOrdiniByIdUtente(int userId);
    Ordine findById(int id);
    List<Ordine> findByUserId(int userId);
    List<Ordine> findAll();
    boolean updateStatus(int orderId, String nuovoStato);
}