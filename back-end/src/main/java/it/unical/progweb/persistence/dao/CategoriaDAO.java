package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Categoria;

import java.util.List;

// metodi usati dall'admin
public interface CategoriaDAO {
    void addCategory(Categoria categoria);
    List<Categoria> findAll();
    void deleteCategoria(int id);

    Categoria findById(int id);
}
