package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.CarrelloDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CarrelloDAOJDBC implements CarrelloDAO {
    private Connection connection;

    public CarrelloDAOJDBC(Connection connection) {
    }

    @Override
    public Carrello findById(int id) {
        Carrello carrello = null;

        String query = "SELECT * FROM Carrello WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    carrello = new Carrello(
                            rs.getInt("id"),
                            rs.getInt("idUtente")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carrello;
    }

    @Override
    public Carrello findByUtenteId(int utenteId) {
        Carrello carrello = null;

        String query = "SELECT * FROM Carrello WHERE idUtente = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, utenteId);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    carrello = new Carrello(
                            rs.getInt("id"),
                            rs.getInt("idUtente")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carrello;
    }


    // todo: dA RIFARE
    @Override
    public void addProdotto(int carrelloId, int prodottoId, int quantità) {
        String query = "INSERT INTO ordine (idPordotto, data, email, password, isAdmin) VALUES (?, ?, ?, ?, ?)";
        /*try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, hashedPassword);
            ps.setBoolean(5, false);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }

    // TODO  DA RIVEDERE
    @Override
    public void removeProdotto(int carrelloId, int prodottoId) {

    }

    @Override
    public void clear(int carrelloId) {

    }
}
