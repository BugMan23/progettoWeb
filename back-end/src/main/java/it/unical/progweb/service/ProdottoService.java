package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService {

    public List<Prodotto> getAllProducts() {
        return DBManager.getInstance().getProductDAO().findAll();
    }

    public Prodotto getProductById(int id) {
        Prodotto prodotto = DBManager.getInstance().getProductDAO().findById(id);
        if (prodotto == null) {
            throw new NotFoundException("Prodotto non trovato");
        }
        return prodotto;
    }

    // Filtri
    public List<Prodotto> getProductsByCategory(String categoria) {
        return DBManager.getInstance().getProductDAO().findProdottiByCategoria(categoria);
    }

    public List<Prodotto> getProductsByName(String nome) {
        return DBManager.getInstance().getProductDAO().findByNome(nome);
    }

    public List<Prodotto> getProductsByColor(String colore) {
        return DBManager.getInstance().getProductDAO().findByColore(colore);
    }

    public List<Prodotto> getProductsByPrice(int prezzo) {
        return DBManager.getInstance().getProductDAO().findByPrezzo(prezzo);
    }

    public List<Prodotto> getProductsByPriceRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere maggiore del massimo");
        }
        return DBManager.getInstance().getProductDAO().findByPrezzoMinEMax(min, max);
    }

    public void addProduct(Prodotto prodotto) {
        validateProduct(prodotto);
        DBManager.getInstance().getProductDAO().addProdotto(prodotto);
    }

    private void validateProduct(Prodotto prodotto) {
        if (prodotto.getNome() == null || prodotto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome prodotto obbligatorio");
        }
        if (prodotto.getPrezzo() <= 0) {
            throw new IllegalArgumentException("Prezzo non valido");
        }
        if (prodotto.getMarca() == null || prodotto.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("Marca obbligatoria");
        }
    }
}
