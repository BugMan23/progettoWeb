package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Categoria;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.CategoriaDAO;
import it.unical.progweb.persistence.dao.ProdottoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;
    private final ProdottoDAO prodottoDAO;

    @Autowired
    public CategoriaService(CategoriaDAO categoriaDAO, ProdottoDAO prodottoDAO) {
        this.categoriaDAO = categoriaDAO;
        this.prodottoDAO = prodottoDAO;
    }

    public void addCategory(Categoria categoria) {
        validateCategory(categoria);
        categoriaDAO.addCategory(categoria);
    }

    public List<Categoria> getAllCategories() {
        return categoriaDAO.findAll();
    }


    private void validateCategory(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome categoria obbligatorio");
        }
    }


    public Categoria getCategoriaById(int id) {
        Categoria categoria = categoriaDAO.findById(id);
        if (categoria == null) {
            throw new NotFoundException("Categoria non trovata");
        }
        return categoria;
    }
}