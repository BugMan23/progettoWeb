package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.persistence.dao.ProdottoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdottoService {

    private final ProdottoDAO prodottoDAO;

    @Autowired
    public ProdottoService(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    public List<Prodotto> getAllProducts() {
        return prodottoDAO.findAll();
    }

    public Prodotto getProductById(int id) {
        Prodotto prodotto = prodottoDAO.findById(id);
        if (prodotto == null) {
            throw new NotFoundException("Prodotto non trovato");
        }
        return prodotto;
    }

    // Filtri
    public List<Prodotto> getProductsByCategory(String categoria) {
        return prodottoDAO.findProdottiByCategoria(categoria);
    }

    public List<Prodotto> getProductsByName(String nome) {
        return prodottoDAO.findByNome(nome);
    }

    public List<Prodotto> getProductsByColor(String colore) {
        return prodottoDAO.findByColore(colore);
    }

    public List<Prodotto> getProductsByPrice(int prezzo) {
        return prodottoDAO.findByPrezzo(prezzo);
    }

    public List<Prodotto> getProductsByPriceRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Il prezzo minimo non può essere maggiore del massimo");
        }
        return prodottoDAO.findByPrezzoMinEMax(min, max);
    }

    public void addProduct(Prodotto prodotto) {
        validateProduct(prodotto);
        prodottoDAO.addProdotto(prodotto);
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