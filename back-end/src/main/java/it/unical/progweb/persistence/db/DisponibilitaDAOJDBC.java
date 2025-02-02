package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.persistence.dao.DisponibilitaDAO;

import java.sql.Connection;
import java.util.List;

public class DisponibilitaDAOJDBC implements DisponibilitaDAO {
    private Connection connection;

    public DisponibilitaDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Disponibilita findById(int id) {
        return null;
    }

    @Override
    public List<Disponibilita> findByProdottoId(int prodottoId) {
        return List.of();
    }

    @Override
    public void save(Disponibilita disponibilita) {

    }

    @Override
    public void update(Disponibilita disponibilita) {

    }

    @Override
    public void delete(int id) {

    }
}
