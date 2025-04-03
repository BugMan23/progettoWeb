package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.persistence.dao.DisponibilitaDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisponibilitaDAOJDBC implements DisponibilitaDAO {
    private final DataSource dataSource;

    public DisponibilitaDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Disponibilita> findByProdottoId(int prodottoId) {
        List<Disponibilita> disponibilita = new ArrayList<>();
        String query = "SELECT * FROM disponibilita WHERE idProdotto = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prodottoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    disponibilita.add(new Disponibilita(
                            rs.getInt("id"),
                            rs.getInt("quantita"),
                            rs.getString("taglia"),
                            rs.getInt("idProdotto")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return disponibilita;
    }

    @Override
    public void updateDisponibilita(int idProdotto, int quantita, String taglia) {
        String query = "UPDATE disponibilita SET quantita = ? WHERE idprodotto = ? AND taglia = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantita);
            ps.setInt(2, idProdotto);
            ps.setString(3, taglia);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del nome: " + e.getMessage());
        }
    }

    @Override
    public void decrementaQuantita(int idProdotto, int quantita, String taglia) {
        String query = "UPDATE disponibilita SET quantita = quantita - ? WHERE idprodotto = ? AND taglia = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantita);
            ps.setInt(2, idProdotto);
            ps.setString(3, taglia);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del nome: " + e.getMessage());
        }
    }

    @Override
    public void addDisponibilita(Disponibilita disponibilita) {
        String query = "INSERT INTO disponibilita (idprodotto, taglia, quantita) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, disponibilita.getIdProdotto());
            ps.setString(2, disponibilita.getTaglia());
            ps.setInt(3, disponibilita.getQuantita());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'inserimento disponibilità", e);
        }
    }

}