package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Recensione;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.RecensioneDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAOJDBC implements RecensioneDAO {
    private Connection connection;

    public RecensioneDAOJDBC(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void addRecensione(Recensione recensione) {
        String query = "INSERT INTO recensione (idProdotto, idUtente, valutazione, testo, data)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, recensione.getIdProdotto());
            ps.setInt(2, recensione.getIdUtente());
            ps.setInt(3, recensione.getValutazione());
            ps.setString(4, recensione.getTesto());
            ps.setString(5, recensione.getData());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // todo: da rivedere
    @Override
    public void deleteRecensione(int recensioneId) {

    }

    @Override
    public List<Recensione> findByProdottoId(int prodottoId) {
        List<Recensione> recensioni = new ArrayList<>();
        String query = "SELECT * FROM recensioni WHERE idProdotto = ? ";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prodottoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    recensioni.add(new Recensione(rs.getInt("id"),
                            rs.getInt("idprodotto"),
                            rs.getInt("idutente"),
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
}