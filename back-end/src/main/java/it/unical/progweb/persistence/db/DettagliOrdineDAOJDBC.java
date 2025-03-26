package it.unical.progweb.persistence.db;

import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.persistence.dao.DettagliOrdineDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DettagliOrdineDAOJDBC implements DettagliOrdineDAO {
    private final DataSource dataSource;

    public DettagliOrdineDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addArticoliOrdine(int idOrdine, int idProdotto, int quantita) {
        String query = "INSERT INTO dettagliordini (idOrdine, idProdotto, quantita) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idOrdine);
            ps.setInt(2, idProdotto);
            ps.setInt(3, quantita);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DettagliOrdini> findByOrderId(int ordineId) {
        List<DettagliOrdini> dettagli = new ArrayList<>();
        String query = "SELECT * FROM dettagliordini WHERE idOrdine = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, ordineId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    dettagli.add(new DettagliOrdini(
                            rs.getInt("id"),
                            rs.getInt("idOrdine"),
                            rs.getInt("idProdotto"),
                            rs.getInt("quantita")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dettagli;
    }
}