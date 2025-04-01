package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.ProdottoDAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdottoDAOJDBC implements ProdottoDAO {
    private final DataSource dataSource;

    public ProdottoDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addProdotto(Prodotto prodotto) {
        String query = "INSERT INTO prodotto (nome, marca, colore, prezzo, descrizione, scontato, image, idcategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getMarca());
            ps.setString(3, prodotto.getColore());
            ps.setInt(4, prodotto.getPrezzo());
            ps.setString(5, prodotto.getDescrizione());
            ps.setBoolean(6, prodotto.getScontato());
            ps.setString(7, prodotto.getUrlImage());
            ps.setInt(8, prodotto.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProdotto(Prodotto prodotto) {
        String query = "UPDATE prodotto SET nome = ?, marca = ?, colore = ?, prezzo = ?, descrizione = ?, scontato = ?, image = ?, idcategoria = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getMarca());
            ps.setString(3, prodotto.getColore());
            ps.setInt(4, prodotto.getPrezzo());
            ps.setString(5, prodotto.getDescrizione());
            ps.setBoolean(6, prodotto.getScontato());
            ps.setString(7, prodotto.getUrlImage());
            ps.setInt(8, prodotto.getIdCategoria());
            ps.setInt(9, prodotto.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento del prodotto", e);
        }
    }


    @Override
    public void deleteProdotto(int id) {
        try (Connection connection = dataSource.getConnection()) {

            // 1. Elimina da disponibilita
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM disponibilita WHERE idprodotto = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Errore eliminando da disponibilita: " + e.getMessage());
            }

            // 1. Elimina recensioni collegate
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM recensione WHERE idprodotto = ?")) {
                System.out.println("Eliminazione da recensione...");
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Errore eliminando da recensione: " + e.getMessage());
            }

            // 2. Elimina dettagli ordini collegati
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM dettagliordini WHERE idprodotto = ?")) {
                System.out.println("Eliminazione da dettagli_ordini...");
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Errore eliminando da dettagli_ordini: " + e.getMessage());
            }

            // 3. Elimina dal carrello
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM carrello WHERE idprodotto = ?")) {
                System.out.println("Eliminazione da carrello...");
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Errore eliminando da carrello: " + e.getMessage());
            }

            // 4. Infine, elimina il prodotto
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM prodotto WHERE id = ?")) {
                System.out.println("Eliminazione da prodotto...");
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Errore durante l'eliminazione del prodotto", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione del prodotto e delle sue dipendenze", e);
        }
    }


    @Override
    public List<Prodotto> findProdottiByCategoria(String categoria) {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE categoria = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, categoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idCategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public Prodotto findById(int id) {
        String query = "SELECT * FROM prodotto WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idCategoria")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Prodotto> findAll() {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                prodotti.add(new Prodotto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("marca"),
                        rs.getString("colore"),
                        rs.getInt("prezzo"),
                        rs.getString("descrizione"),
                        rs.getBoolean("scontato"),
                        rs.getString("image"),
                        rs.getInt("idCategoria")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> findByNome(String nome) {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE nome = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idCategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> findByColore(String colore) {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE colore = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, colore);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idCategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> findByPrezzo(int prezzo) {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE prezzo = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prezzo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idCategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> findByPrezzoMinEMax(int min, int max) {
        List<Prodotto> prodotti = new ArrayList<>();
        String query = "SELECT * FROM prodotto WHERE prezzo >= ? AND prezzo <= ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, min);
            ps.setInt(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato"),
                            rs.getString("image"),
                            rs.getInt("idCategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }
}