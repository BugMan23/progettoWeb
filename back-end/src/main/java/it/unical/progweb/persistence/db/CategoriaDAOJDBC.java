package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Categoria;
import it.unical.progweb.persistence.dao.CategoriaDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOJDBC implements CategoriaDAO {
    private final DataSource dataSource;

    public CategoriaDAOJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCategory(Categoria categoria) {
        String query = "INSERT INTO categoria (name, description) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getDescrizione());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Categoria> findAll() {
        List<Categoria> categorie = new ArrayList<>();
        String query = "SELECT * FROM categoria";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                categorie.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorie;
    }

    @Override
    public Categoria findById(int id) {
        String query = "SELECT * FROM categoria WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Categoria(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}