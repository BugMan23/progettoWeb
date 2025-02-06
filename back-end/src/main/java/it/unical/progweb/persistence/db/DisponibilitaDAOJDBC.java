package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Disponibilita;
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


    @Override
    public void updateDisponibilita(int idProdotto, int quantita, String taglia) {
        String query = "UPDATE disponibilita SET quantita = ? WHERE idprodotto = ? AND taglia = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantita);
            ps.setInt(2, idProdotto);
            ps.setString(3, taglia);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del nome: " + e.getMessage());
        }
    }

    @Override
    public void decrementaQuantita(int idProdotto, int quantita, String taglia) {
        String query = "UPDATE disponibilita SET quantita = quantita - ? WHERE idprodotto = ? AND taglia = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantita);
            ps.setInt(2, idProdotto);
            ps.setString(3, taglia);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del nome: " + e.getMessage());
        }
    }

}
