package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Disponibilita;

import java.util.List;

public interface DisponibilitaDAO {

    void updateDisponibilita(int idProdotto, int quantita, String taglia);

    List<Disponibilita> findByProdottoId(int prodottoId);

    void decrementaQuantita(int prodotto, int quantita, String taglia);
}
