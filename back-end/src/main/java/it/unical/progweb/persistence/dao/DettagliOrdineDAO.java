package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.DettagliOrdini;

import java.util.List;

public interface DettagliOrdineDAO {

    void addArticoliOrdine(int idOrdine, int idProdotto, int quantita);

    List<DettagliOrdini> findByOrderId(int ordineId);
}
