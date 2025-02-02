package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.persistence.dao.IndirizzoDAO;

import java.sql.Connection;
import java.util.List;

public class IndirizzoDAOJDBC implements IndirizzoDAO {
    private Connection connection;

    public IndirizzoDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Indirizzo findById(int id) {
        return null;
    }

    @Override
    public List<Indirizzo> findByUtenteId(int utenteId) {
        return List.of();
    }

    @Override
    public void save(Indirizzo indirizzo) {

    }

    @Override
    public void update(Indirizzo indirizzo) {

    }

    @Override
    public void delete(int id) {

    }
}
