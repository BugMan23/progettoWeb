package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.IndirizzoDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndirizzoDAOJDBC implements IndirizzoDAO {
    private final DataSource dataSource;

    public IndirizzoDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Indirizzo> findByUtenteId(int utenteId) {
        List<Indirizzo> indirizzi = new ArrayList<>();
        String query = "SELECT * FROM indirizzo WHERE idUtente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, utenteId);

            // Aggiungi un log per debug
            System.out.println("Esecuzione query per indirizzi dell'utente ID: " + utenteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    indirizzi.add(new Indirizzo(
                            rs.getInt("id"),
                            rs.getString("nomeVia"),
                            rs.getString("civico"),
                            rs.getString("citta"),
                            rs.getString("cap"),
                            rs.getString("provincia"),
                            rs.getString("regione"),
                            rs.getInt("idUtente")
                    ));
                }
            }

            // Log del risultato
            System.out.println("Trovati " + indirizzi.size() + " indirizzi per l'utente ID: " + utenteId);

        } catch (SQLException e) {
            System.err.println("Errore query indirizzi: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return indirizzi;
    }
    @Override
    public void addIndirizzo(Indirizzo indirizzo, int idutente) {
        String query = "INSERT INTO indirizzo (nomeVia, civico, citta, cap, provincia, regione, idUtente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, indirizzo.getNomeVia());
            ps.setString(2, indirizzo.getCivico());
            ps.setString(3, indirizzo.getCitta());
            ps.setString(4, indirizzo.getCap());
            ps.setString(5, indirizzo.getProvincia());
            ps.setString(6, indirizzo.getRegione());
            ps.setInt(7, idutente);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}