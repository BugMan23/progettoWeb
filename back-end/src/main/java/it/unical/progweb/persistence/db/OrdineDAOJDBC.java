package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.OrdineDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrdineDAOJDBC implements OrdineDAO {
    private Connection connection;

    public OrdineDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    // TODO: da sistemare per vedere come recuperare la chiave composta
    @Override
    public Ordine findById(int id) {
        Ordine ordine= null;

        String query = "SELECT * FROM carrProd WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    ordine = new Ordine(
                            rs.getInt("idUtente"),
                            rs.getInt("idProdotto"),
                            rs.getString("data"),
                            rs.getString("stato"),
                            rs.getString("consegna")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordine;
    }


    @Override
    public List<Ordine> findByCarrelloId(int carrelloID) {
        List<Ordine> ordini = null;

        String query = "SELECT * FROM carrProd WHERE idCarrello = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, carrelloID);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    ordini.add(new Ordine(
                            rs.getInt("idUtente"),
                            rs.getInt("idProdotto"),
                            rs.getString("data"),
                            rs.getString("stato"),
                            rs.getString("consegna")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }


    // TODO: DA RIVEDERE

    @Override
    public void save(Ordine ordine) {

    }

    @Override
    public void update(Ordine ordine) {

    }

    @Override
    public void delete(int id) {

    }
}
