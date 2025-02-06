package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Recensione;
import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAOJDBC implements UtenteDAO {
    private Connection connection;

    public UtenteDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUtente(Utente utente) {
        String query = "INSERT INTO utente (nome, cognome, email, telefono, username, passwrod, ruolo)";

        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getTelefono());
            ps.setString(5, utente.getUsername());

            // TODO: CRIPTARE LA PASSWORD
            ps.setString(6, utente.getPassword());
            ps.setBoolean(7, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utente findByEmail(String email) {
        String query = "SELECT * FROM utente WHERE email = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return new Utente(rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBoolean("ruolo")
                            );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Utente findById(int id) {
        String query = "SELECT * FROM utente WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return new Utente(rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBoolean("ruolo")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateEmail(int id, String email) {
        String query = "UPDATE utente SET email = ? WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del nome: " + e.getMessage());
        }
    }

    @Override
    public List<Utente> findAll() {
        List<Utente> utenti = new ArrayList<>();
        String query = "SELECT * FROM utente ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    utenti.add(new Utente(rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBoolean("ruolo")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
