package it.unical.progweb.persistence.db;

import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.persistence.dao.MetodoDiPagamentoDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodoDiPagamentoDAOJDBC implements MetodoDiPagamentoDAO {
    private final DataSource dataSource;

    public MetodoDiPagamentoDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<MetodoDiPagamento> findByUtenteId(int utenteId) {
        List<MetodoDiPagamento> metodiDiPagamento = new ArrayList<>();
        String query = "SELECT * FROM metodoDiPagamento WHERE idUtente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, utenteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    metodiDiPagamento.add(new MetodoDiPagamento(
                            rs.getInt("id"),
                            rs.getString("tipoPagamento"),
                            rs.getString("titolare"),
                            rs.getString("tipoCarta"),
                            rs.getString("numeroCarta"),
                            rs.getString("dataScadenza"),
                            rs.getString("cvv"),
                            rs.getInt("idUtente")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metodiDiPagamento;
    }

    @Override
    public boolean addMetodoDiPagamento(MetodoDiPagamento metodoDiPagamento, int utenteId) {
        String query = "INSERT INTO MetodoDiPagamento (tipoPagamento, titolare, tipoCarta, numeroCarta, dataScadenza, cvv, idUtente) VALUES (?, ?, ?, ?, ?, ? , ? )";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, metodoDiPagamento.getTipoPagamento());
            ps.setString(2, metodoDiPagamento.getTitolare());
            ps.setString(3, metodoDiPagamento.getTipoCarta());
            ps.setString(4, metodoDiPagamento.getNumeroCarta());
            ps.setString(5, metodoDiPagamento.getDataScadenza());
            ps.setString(6, metodoDiPagamento.getCvv());
            ps.setInt(7, utenteId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MetodoDiPagamento findById(int id) {
        MetodoDiPagamento metodoDiPagamento = null;
        String query = "SELECT * FROM metodoDiPagamento WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    metodoDiPagamento = new MetodoDiPagamento(
                            rs.getInt("id"),
                            rs.getString("tipoPagamento"),
                            rs.getString("titolare"),
                            rs.getString("tipoCarta"),
                            rs.getString("numeroCarta"),
                            rs.getString("dataScadenza"),
                            rs.getString("cvv"),
                            rs.getInt("idUtente")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metodoDiPagamento;
    }
}