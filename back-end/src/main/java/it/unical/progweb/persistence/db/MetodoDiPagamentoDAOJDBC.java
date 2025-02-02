package it.unical.progweb.persistence.db;

import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.persistence.dao.MetodoDiPagamentoDAO;

import java.sql.Connection;
import java.util.List;

public class MetodoDiPagamentoDAOJDBC implements MetodoDiPagamentoDAO {
    private Connection connection;

    public MetodoDiPagamentoDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public MetodoDiPagamento findById(int id) {
        return null;
    }

    @Override
    public List<MetodoDiPagamento> findByUtenteId(int utenteId) {
        return List.of();
    }

    @Override
    public void save(MetodoDiPagamento metodoDiPagamento) {

    }

    @Override
    public void update(MetodoDiPagamento metodoDiPagamento) {

    }

    @Override
    public void delete(int id) {

    }
}
