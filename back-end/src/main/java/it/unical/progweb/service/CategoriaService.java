package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Categoria;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    public void addCategory(Categoria categoria) {
        validateCategory(categoria);
        DBManager.getInstance().getCategoriaDAO().addCategory(categoria);
    }

    public List<Categoria> getAllCategories() {
        return DBManager.getInstance().getCategoriaDAO().findAll();
    }

    /*public void deleteCategory(int id) {
        validateCategoryHasNoProducts(categoria);
        DBManager.getInstance().getCategoriaDAO().deleteCategoria(id);
    }*/

    private void validateCategory(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome categoria obbligatorio");
        }
    }

    private void validateCategoryHasNoProducts(String categoria) {
        List<Prodotto> products = DBManager.getInstance().getProductDAO()
                .findProdottiByCategoria(categoria);
        if (!products.isEmpty()) {
            throw new IllegalStateException("Impossibile eliminare categoria con prodotti associati");
        }
    }

    public Categoria getCategoriaById(int id) {
        Categoria categoria = DBManager.getInstance().getCategoriaDAO().findById(id);
        if (categoria == null) {
            throw new NotFoundException("Categoria non trovata");
        }
        return categoria;
    }
}
