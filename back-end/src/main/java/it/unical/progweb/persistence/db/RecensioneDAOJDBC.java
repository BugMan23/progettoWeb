package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Recensione;
import it.unical.progweb.persistence.dao.RecensioneDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RecensioneDAOJDBC implements RecensioneDAO {
    private Connection connection;

    public RecensioneDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    // TODO : da rivedere perche recensione è chiave composta
    @Override
    public Recensione findById(int id) {
        Recensione recensione = null;

        String query = "SELECT * FROM recensione WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    recensione = new Recensione(
                            rs.getInt("idUtente"),
                            rs.getInt("idProdotto"),
                            rs.getInt("valutazione"),
                            rs.getString("testo"),
                            rs.getString("data")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recensione;
    }

    @Override
    public List<Recensione> findByProdottoId(int prodottoId) {
        List<Recensione> recensioni = null;

        String query = "SELECT * FROM recensione WHERE idProdotto = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prodottoId);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    recensioni.add(new Recensione(
                            rs.getInt("idUtente"),
                            rs.getInt("idProdotto"),
                            rs.getInt("valutazione"),
                            rs.getString("testo"),
                            rs.getString("data")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recensioni;
    }

    @Override
    public boolean save(Recensione recensione) {
        String query = "INSERT INTO recensioni (idUtente, idProdotto, valutazione, testo, data) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, recensione.getIdUtente());
            ps.setInt(2, recensione.getIdProdotto());
            ps.setInt(3, recensione.getValutazione());
            ps.setString(4, recensione.getTesto());
            ps.setString(5, recensione.getData());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO : da rivedere se farle fare

    @Override
    public void update(Recensione recensione) {

    }

    @Override
    public void delete(int id) {

    }
}
