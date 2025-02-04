package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Prodotto;

import java.util.List;

public interface ProdottoDAO {
    Prodotto findById(int id);
    List<Prodotto> findAll();
    void save(Prodotto prodotto);
    void update(Prodotto prodotto);
    void delete(int id);
    List<Prodotto> searchByNome(String nome);
    List<Prodotto> searchByColore(String colore);
    List<Prodotto> searchByDescrizione(String descrizione);
    List<Prodotto> searchByPrezzo(int prezzo);
    List<Prodotto> searchByPrezzo(int min, int max);

}

