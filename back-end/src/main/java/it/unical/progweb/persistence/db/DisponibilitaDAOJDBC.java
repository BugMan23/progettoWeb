package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.DisponibilitaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DisponibilitaDAOJDBC implements DisponibilitaDAO {
    private Connection connection;

    public DisponibilitaDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Disponibilita findById(int id) {
        Disponibilita disponibilita = null;

        String query = "SELECT * FROM disponibilita WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    disponibilita = new Disponibilita(
                            rs.getInt("id"),
                            rs.getInt("quantita"),
                            rs.getString("taglia"),
                            rs.getInt("idProdotto")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return disponibilita;
    }



/* LO UTILIZZO PER : restituisce la disponibilità di un prodotto può essere utile
in vari contesti, ad esempio quando un cliente sta visualizzando i dettagli di un
prodotto e desidera vedere le taglie e le quantità disponibili, o quando sta per aggiungere
un prodotto al carrello e deve sapere se l'articolo è disponibile nella taglia e quantità desiderata.*/
    @Override
    public List<Disponibilita> findByProdottoId(int prodottoId) {
        List<Disponibilita> disponibilita = null;

        String query = "SELECT * FROM disponibilita WHERE idProdotto = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prodottoId);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
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


    // TODO DA RIVEDERE COME E COSA SERVONO
    @Override
    public void save(Disponibilita disponibilita) {

    }

    @Override
    public void update(Disponibilita disponibilita) {

    }

    @Override
    public void delete(int id) {

    }
}
