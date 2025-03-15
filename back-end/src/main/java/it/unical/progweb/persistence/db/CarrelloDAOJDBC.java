package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.CarrelloDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarrelloDAOJDBC implements CarrelloDAO {
    private final DataSource dataSource;

    public CarrelloDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addAlCarrello(int userId, int prodottoId, int quantita) {
        // todo: da rivedere per gestire meglio la quantità
        String query = "INSERT INTO carrello (idutente, idprodotto, quantita, isordinato) VALUES (?, ?, ?, ?) ON CONFLICT (userId, prodottoId) DO UPDATE SET quantita = carrello.quantita + EXCLUDED.quantita;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, prodottoId);
            ps.setInt(3, quantita);
            ps.setString(4, "false");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Prodotto> getCarrello(int userId) {
        List<Prodotto> carrello = new ArrayList<>();
        String query = "SELECT p.* FROM Prodotti p JOIN carrello c ON p.id = c.idprodotto WHERE c.idutente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    carrello.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idcategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carrello;
    }

    // todo: da rivedere
    @Override
    public void clear(int idUser) {
        String updateQuery = "UPDATE CarrelloItems SET isOrdinato = TRUE WHERE idutente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement psUpdate = connection.prepareStatement(updateQuery)) {
            psUpdate.setInt(1, idUser);
            int updatedRows = psUpdate.executeUpdate();
            if (updatedRows == 0) {
                System.out.println("Nessun prodotto aggiornato, verificare l'ID dell'utente.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'impostazione di is_ordered: " + e.getMessage(), e);
        }
    }
}