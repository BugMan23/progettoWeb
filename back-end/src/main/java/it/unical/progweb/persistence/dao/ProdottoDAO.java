package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Prodotto;

import java.util.List;

public interface ProdottoDAO {
    void addProdotto(Prodotto prodotto);
    void deleteProdotto(int id);
    void updateProdotto(Prodotto prodotto);

    List<Prodotto> findProdottiByCategoria(String categoria);
    Prodotto findById(int id);
    List<Prodotto> findAll();
    List<Prodotto> findByNome(String nome);
    List<Prodotto> findByColore(String colore);
    List<Prodotto> findByPrezzo(int prezzo);
    List<Prodotto> findByPrezzoMinEMax(int min, int max);

    Prodotto findLastInserted();


}

