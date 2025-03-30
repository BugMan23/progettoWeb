package it.unical.progweb.persistence.db;

import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.persistence.dao.MetodoDiPagamentoDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodoDiPagamentoDAOJDBC implements MetodoDiPagamentoDAO {
    private final DataSource dataSource;

    public MetodoDiPagamentoDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<MetodoDiPagamento> findByUtenteId(int utenteId) {
        List<MetodoDiPagamento> metodiDiPagamento = new ArrayList<>();
        String query = "SELECT * FROM metododiPagamento WHERE idUtente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, utenteId);
            System.out.println("DAO: Esecuzione query findByUtenteId con ID: " + utenteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    metodiDiPagamento.add(new MetodoDiPagamento(
                            rs.getInt("id"),
                            rs.getString("tipoPagamento"),
                            rs.getString("titolare"),
                            rs.getString("tipoCarta"),
                            rs.getString("numCarta"),
                            rs.getString("dataScadenza"),
                            rs.getString("cvv"),
                            rs.getInt("idUtente")
                    ));
                }
            }

            System.out.println("DAO: Trovati " + metodiDiPagamento.size() + " metodi di pagamento");
        } catch (SQLException e) {
            System.err.println("DAO: Errore SQL in findByUtenteId: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return metodiDiPagamento;
    }

    @Override
    public boolean addMetodoDiPagamento(MetodoDiPagamento metodoDiPagamento, int utenteId) {
        String query = "INSERT INTO metodoDiPagamento (tipoPagamento, titolare, tipoCarta, numCarta, dataScadenza, cvv, idUtente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, metodoDiPagamento.getTipoPagamento());
            ps.setString(2, metodoDiPagamento.getTitolare());
            ps.setString(3, metodoDiPagamento.getTipoCarta());
            ps.setString(4, metodoDiPagamento.getNumeroCarta());
            ps.setString(5, metodoDiPagamento.getDataScadenza());
            ps.setString(6, metodoDiPagamento.getCvv());
            ps.setInt(7, utenteId);

            System.out.println("DAO: Esecuzione INSERT per metodo di pagamento con utenteId: " + utenteId);
            System.out.println("DAO: Query: " + query);
            System.out.println("DAO: Valori parametri:");
            System.out.println("- tipoPagamento: " + metodoDiPagamento.getTipoPagamento());
            System.out.println("- titolare: " + metodoDiPagamento.getTitolare());
            System.out.println("- tipoCarta: " + metodoDiPagamento.getTipoCarta());
            System.out.println("- numCarta: " + metodoDiPagamento.getNumeroCarta());
            System.out.println("- dataScadenza: " + metodoDiPagamento.getDataScadenza());
            System.out.println("- cvv: " + metodoDiPagamento.getCvv());
            System.out.println("- idUtente: " + utenteId);

            int rowsUpdated = ps.executeUpdate();
            System.out.println("DAO: Righe inserite: " + rowsUpdated);
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("DAO: Errore SQL in addMetodoDiPagamento: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public MetodoDiPagamento findById(int id) {
        MetodoDiPagamento metodoDiPagamento = null;
        String query = "SELECT * FROM metodoDiPagamento WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    metodoDiPagamento = new MetodoDiPagamento(
                            rs.getInt("id"),
                            rs.getString("tipoPagamento"),
                            rs.getString("titolare"),
                            rs.getString("tipoCarta"),
                            rs.getString("numCarta"),
                            rs.getString("dataScadenza"),
                            rs.getString("cvv"),
                            rs.getInt("idUtente")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metodoDiPagamento;
    }

    @Override
    public boolean updateMetodoDiPagamento(MetodoDiPagamento metodoDiPagamento) {
        String query = "UPDATE metodoDiPagamento SET tipoPagamento = ?, titolare = ?, tipoCarta = ?, numCarta = ?, dataScadenza = ?, cvv = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, metodoDiPagamento.getTipoPagamento());
            ps.setString(2, metodoDiPagamento.getTitolare());
            ps.setString(3, metodoDiPagamento.getTipoCarta());
            ps.setString(4, metodoDiPagamento.getNumeroCarta());
            ps.setString(5, metodoDiPagamento.getDataScadenza());
            ps.setString(6, metodoDiPagamento.getCvv());
            ps.setInt(7, metodoDiPagamento.getId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteMetodoDiPagamento(int id) {
        String query = "DELETE FROM metodoDiPagamento WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}