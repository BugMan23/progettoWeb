package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.ProdottoDAO;

import java.sql.Connection;
import java.util.List;

public class ProdottoDAOJDBC implements ProdottoDAO {
    private Connection connection;

    public ProdottoDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Prodotto findById(int id) {
        return null;
    }

    @Override
    public List<Prodotto> findAll() {
        return List.of();
    }

    @Override
    public List<Prodotto> findByCategoria(String categoria) {
        return List.of();
    }

    @Override
    public void save(Prodotto prodotto) {

    }

    @Override
    public void update(Prodotto prodotto) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Prodotto> searchByNome(String nome) {
        return List.of();
    }

    @Override
    public List<Prodotto> searchByColore(String colore) {
        return List.of();
    }

    @Override
    public List<Prodotto> searchByDescrizione(String descrizione) {
        return List.of();
    }

    @Override
    public List<Prodotto> searchByPrezzo(String prezzo) {
        return List.of();
    }

    @Override
    public List<Prodotto> searchByPrezzo(String prezzo, int min, int max) {
        return List.of();
    }
}
