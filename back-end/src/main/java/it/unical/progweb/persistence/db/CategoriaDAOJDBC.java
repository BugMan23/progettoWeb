package it.unical.progweb.persistence.db;

import it.unical.progweb.model.Categoria;
import it.unical.progweb.persistence.dao.CategoriaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOJDBC implements CategoriaDAO {
    private Connection connection;

    public CategoriaDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCategory(Categoria categoria) {
        String query = "INSERT INTO categoria (name, description) VALUES (?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
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
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    categorie.add(new Categoria(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorie;
    }

    @Override
    public void deleteCategoria(int id) {
        String query = "DELETE FROM categoria WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Categoria findById(int id) {
        String query = "SELECT * FROM categoria WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
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