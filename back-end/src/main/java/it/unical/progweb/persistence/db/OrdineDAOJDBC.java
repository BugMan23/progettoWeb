package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Ordine;
import it.unical.progweb.persistence.dao.OrdineDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAOJDBC implements OrdineDAO {
    private final DataSource dataSource;

    public OrdineDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int creaOrdine(Ordine ordine) {
        String query = "INSERT INTO ordine (idutente, dataoridne, stato, totaledapagare, idmetodopagamento) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, ordine.getIdUtente());
            ps.setString(2, ordine.getData());
            ps.setString(3, ordine.getStato());
            ps.setInt(4, ordine.getTotalePagare());
            ps.setInt(5, ordine.getIdMetodoPagamento());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public List<Ordine> getOrdiniByIdUtente(int userId) {
        List<Ordine> ordini = new ArrayList<>();
        String query = "SELECT * FROM ordine WHERE idutente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordini.add(new Ordine(
                            rs.getInt("id"),
                            rs.getInt("idutente"),
                            rs.getString("dataoridne"),
                            rs.getString("stato"),
                            rs.getInt("totaledapagare"),
                            rs.getInt("idmetodopagamento")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }

    @Override
    public Ordine findById(int id) {
        String query = "SELECT * FROM ordine WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Ordine(
                            rs.getInt("id"),
                            rs.getInt("idutente"),
                            rs.getString("dataoridne"),
                            rs.getString("stato"),
                            rs.getInt("totaledapagare"),
                            rs.getInt("idmetodopagamento")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Ordine> findByUserId(int userid) {
        List<Ordine> ordini = new ArrayList<>();
        String query = "SELECT * FROM ordine WHERE idutente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ordini.add(new Ordine(
                            rs.getInt("id"),
                            rs.getInt("idutente"),
                            rs.getString("dataoridne"),
                            rs.getString("stato"),
                            rs.getInt("totaledapagare"),
                            rs.getInt("idmetodopagamento")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }

    @Override
    public boolean updateStatus(int orderId, String nuovoStato) {
        String query = "UPDATE ordine SET stato = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nuovoStato);
            ps.setInt(2, orderId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'aggiornamento dello stato dell'ordine: " + e.getMessage(), e);
        }
    }
}