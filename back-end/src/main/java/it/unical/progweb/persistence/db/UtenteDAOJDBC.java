package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Utente;
import it.unical.progweb.persistence.DbConn;
import it.unical.progweb.persistence.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAOJDBC implements UtenteDAO {
    private final DataSource dataSource;

    public UtenteDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Utente findById(int id) {
        Utente utente = null;
        String query = "SELECT * FROM utente WHERE id = ? ";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    utente = new Utente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBoolean("isadmin")
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
        List<Utente> utenti = new ArrayList<>();

        String query = "SELECT * FROM utente";
        try(Connection connection = dataSource.getConnection();
            Statement ps = connection.createStatement();
            ResultSet rs = ps.executeQuery(query)) {

            while(rs.next()){
                utenti.add(new Utente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("isadmin"))
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
        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, hashedPassword);
            ps.setBoolean(5, utente.getRuolo());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utente findByEmail(String email) {
        String query = "SELECT * FROM utente WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            // Stampa per debug
            System.out.println("Ricerca utente con email: " + email);

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Stampa dettagli utente trovato
                    System.out.println("Utente trovato: " +
                            "ID=" + rs.getInt("id") +
                            ", Nome=" + rs.getString("nome") +
                            ", Email=" + rs.getString("email")
                    );

                    return new Utente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBoolean("isAdmin") // o "isAdmin"
                    );
                } else {
                    // Stampa se nessun utente trovato
                    System.out.println("Nessun utente trovato con email: " + email);
                }
            }
        } catch (SQLException e) {
            // Stampa l'eccezione completa per debug
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void elimina(int id) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Elimina recensioni
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM recensione WHERE idutente = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 2. Elimina carrello
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM carrello WHERE idutente = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 3. Elimina dettagli ordini (devono venire prima degli ordini)
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM dettagliordini WHERE idordine IN (SELECT id FROM ordine WHERE idutente = ?)")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 4. Elimina ordini
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ordine WHERE idutente = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 5. Elimina indirizzi
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM indirizzo WHERE idutente = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 6. Elimina metodi di pagamento (ora è sicuro)
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM metododipagamento WHERE idutente = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // 7. Elimina l’utente
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM utente WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'utente", e);
        }
    }



}