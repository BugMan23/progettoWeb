package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.ProdottoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAOJDBC implements ProdottoDAO {
    private Connection connection;

    public ProdottoDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Prodotto findById(int id) {
        Prodotto prodotto = null;

        String query = "SELECT * FROM prodotto WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    prodotto = new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotto;
    }

    @Override
    public List<Prodotto> findAll() {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();

        String query = "SELECT * FROM prodotti";
        try(Statement ps = connection.createStatement()) {
            ResultSet rs = ps.executeQuery(query);

            while(rs.next()){
                prodotti.add(new Prodotto(
                                rs.getInt("id"),
                                rs.getString("nome"),
                                rs.getString("marca"),
                                rs.getString("colore"),
                                rs.getInt("prezzo"),
                                rs.getString("descrizione"),
                                rs.getBoolean("scontato")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    // TODO : da rivedere se lasciarle o meno

    @Override
    public void save(Prodotto prodotto) {

    }
    @Override
    public void update(Prodotto prodotto) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Prodotto> searchByNome(String nome) {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();

        String query = "SELECT * FROM prodotti WHERE nome = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nome);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> searchByColore(String colore) {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();

        String query = "SELECT * FROM prodotti WHERE colore = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, colore);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> searchByDescrizione(String descrizione) {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();

        String query = "SELECT * FROM prodotti WHERE descrizione = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, descrizione);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> searchByPrezzo(int prezzo) {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();

        String query = "SELECT * FROM prodotti WHERE prezzo = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prezzo);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }

    @Override
    public List<Prodotto> searchByPrezzo(int prezzoMin, int prezzoMax) {
        List<Prodotto> prodotti = new ArrayList<Prodotto>();

        String query = "SELECT * FROM prodotti WHERE prezzo >= ? AND prezzo <= ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prezzoMin);
            ps.setInt(2, prezzoMax);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    prodotti.add(new Prodotto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("colore"),
                            rs.getInt("prezzo"),
                            rs.getString("descrizione"),
                            rs.getBoolean("scontato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prodotti;
    }
}
