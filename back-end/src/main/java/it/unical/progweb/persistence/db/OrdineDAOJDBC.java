package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Ordine;
import it.unical.progweb.persistence.dao.OrdineDAO;

import java.sql.Connection;
import java.util.List;

public class OrdineDAOJDBC implements OrdineDAO {
    private Connection connection;

    public OrdineDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Ordine findById(int id) {
        return null;
    }

    @Override
    public List<Ordine> findAll() {
        return List.of();
    }

    @Override
    public List<Ordine> findByUtenteId(int utenteId) {
        return List.of();
    }

    @Override
    public void save(Ordine ordine) {

    }

    @Override
    public void update(Ordine ordine) {

    }

    @Override
    public void delete(int id) {

    }
}
