package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Carrello;
import it.unical.progweb.persistence.dao.CarrelloDAO;

import java.sql.Connection;
import java.util.List;

public class CarrelloDAOJDBC implements CarrelloDAO {
    private Connection connection;

    public CarrelloDAOJDBC(Connection connection) {
    }

    @Override
    public Carrello findById(int id) {
        return null;
    }

    @Override
    public List<Carrello> findByUtenteId(int utenteId) {
        return List.of();
    }

    @Override
    public void addProdotto(int carrelloId, int prodottoId, int quantità) {

    }

    @Override
    public void removeProdotto(int carrelloId, int prodottoId) {

    }

    @Override
    public void clear(int carrelloId) {

    }
}
