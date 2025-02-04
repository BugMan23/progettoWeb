package it.unical.progweb.persistence.db;

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
    public Utente findById(int id) {
        Utente utente = null;

        String query = "SELECT * FROM utente WHERE id = ? ";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    utente = new Utente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("password"),
                            false
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    @Override
    public List<Utente> findAll() {
        List<Utente> utenti = new ArrayList<Utente>();

        String query = "SELECT * FROM utente";
        try(Statement ps = connection.createStatement()) {
            ResultSet rs = ps.executeQuery(query);

            while(rs.next()){
                utenti.add(new Utente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        false)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;
    }

    @Override
    public boolean save(Utente utente) {
        String hashedPassword = BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt());

        String query = "INSERT INTO utente (nome, cognome, email, password, isAdmin) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, hashedPassword);
            ps.setBoolean(5, false);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utente validateUser(String email, String password){
        String query = "SELECT * FROM utente WHERE email = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    if(BCrypt.checkpw(password, rs.getString("password"))){
                        return new Utente(
                                rs.getInt("id"),
                                rs.getString("nome"),
                                rs.getString("cognome"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getBoolean("isAdmin")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean updateEmail(Utente utente, String email) {
        String query = "UPDATE utente SET email = ? WHERE id = ?";
        boolean risultato = false;

        try(PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, utente.getId());
            ps.setString(2, email);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0)
                risultato = true;
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del nome: " + e.getMessage());
        }

        return risultato;

    }

    @Override
    public void delete(int id) {

    }

    // TODO : DA RIVEDERE
    @Override
    public Utente findByEmail(String email) {
        return null;
    }


    @Override
    public void changePassword(int id, String newPassword) {

    }
}
