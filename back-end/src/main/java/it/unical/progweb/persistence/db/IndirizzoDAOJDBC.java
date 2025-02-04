package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.persistence.dao.IndirizzoDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class IndirizzoDAOJDBC implements IndirizzoDAO {
    private Connection connection;

    public IndirizzoDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Indirizzo findById(int id) {
        Indirizzo indirizzo = null;

        String query = "SELECT * FROM indirizzo WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    indirizzo = new Indirizzo(
                            rs.getInt("id"),
                            rs.getString("nomeVia"),
                            rs.getString("civico"),
                            rs.getString("citta"),
                            rs.getString("cap"),
                            rs.getString("provincia"),
                            rs.getString("regione"),
                            rs.getInt("idUtente")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return indirizzo;
    }

    @Override
    public List<Indirizzo> findByUtenteId(int utenteId)  {
        List<Indirizzo> indirizzi = null;

        String query = "SELECT * FROM indirizzo WHERE idUtente = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, utenteId);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    indirizzi.add(new Indirizzo(
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
        return indirizzi;
    }

    // TODO: da rivedere

    @Override
    public boolean save(Indirizzo indirizzo, int utenteId) {
        String query = "INSERT INTO indirizzo (nomeVia, civico, citta, cap, provincia, regione, idUtente) VALUES (?, ?, ?, ?, ?, ? , ? )";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, indirizzo.getNomeVia());
            ps.setString(2, indirizzo.getCivico());
            ps.setString(3, indirizzo.getCitta());
            ps.setString(4, indirizzo.getCap());
            ps.setString(5, indirizzo.getProvincia());
            ps.setString(6, indirizzo.getRegione());
            ps.setInt(7, utenteId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Indirizzo indirizzo) {

    }

    @Override
    public void delete(int id) {

    }
}
