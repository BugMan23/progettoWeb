package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Prodotto;
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
        // Controlla se il prodotto è già nel carrello
        String checkQuery = "SELECT quantita FROM carrello WHERE idutente = ? AND idprodotto = ? AND isordinato = false";
        String insertQuery = "INSERT INTO carrello (idutente, idprodotto, quantita, taglia, isordinato) VALUES (?, ?, ?, ?, false)";
        String updateQuery = "UPDATE carrello SET quantita = ? WHERE idutente = ? AND idprodotto = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
            checkPs.setInt(1, userId);
            checkPs.setInt(2, prodottoId);

            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    // Prodotto già nel carrello, aggiorna quantità
                    int currentQuantity = rs.getInt("quantita");
                    int newQuantity = currentQuantity + quantita;

                    try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                        updatePs.setInt(1, newQuantity);
                        updatePs.setInt(2, userId);
                        updatePs.setInt(3, prodottoId);
                        updatePs.executeUpdate();
                    }
                } else {
                    // Prodotto non nel carrello, inserisci nuovo
                    try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                        insertPs.setInt(1, userId);
                        insertPs.setInt(2, prodottoId);
                        insertPs.setInt(3, quantita);
                        insertPs.setString(4, "M"); // Default taglia
                        insertPs.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Prodotto> getCarrello(int userId) {
        List<Prodotto> carrello = new ArrayList<>();
        String query = "SELECT p.* FROM prodotto p JOIN carrello c ON p.id = c.idprodotto WHERE c.idutente = ? AND c.isordinato = false";

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

    @Override
    public List<Carrello> getCartDetails(int userId) {
        List<Carrello> dettagli = new ArrayList<>();
        String query = "SELECT * FROM carrello WHERE idutente = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dettagli.add(new Carrello(
                            rs.getInt("id"),
                            rs.getInt("idutente"),
                            rs.getInt("idprodotto"),
                            rs.getString("quantita"),
                            rs.getBoolean("isordinato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dettagli;
    }

    @Override
    public void clear(int userId) {
        String deleteQuery = "DELETE FROM carrello WHERE idutente = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFromCart(int userId, int productId) {
        String query = "DELETE FROM carrello WHERE idutente = ? AND idprodotto = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCartItem(int userId, int productId, int quantity, String taglia) {
        String query = "UPDATE carrello SET quantita = ?, taglia = ? WHERE idutente = ? AND idprodotto = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setString(2, taglia);
            ps.setInt(3, userId);
            ps.setInt(4, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCartItemTaglia(int userId, int productId, String taglia) {
        String query = "UPDATE carrello SET taglia = ? WHERE idutente = ? AND idprodotto = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, taglia);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initializeEmptyCart(int userId) {
        // Non c'è bisogno di inserire record nel database, ma possiamo verificare
        // che non esista già un carrello per questo utente
        String query = "SELECT COUNT(*) FROM carrello WHERE idutente = ? AND isordinato = false";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Non esistono record, possiamo creare un record "segnaposto" se necessario
                    // In alternativa, possiamo semplicemente non fare nulla e gestire
                    // il caso di carrello vuoto nel servizio e controller
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'inizializzazione del carrello", e);
        }
    }

}