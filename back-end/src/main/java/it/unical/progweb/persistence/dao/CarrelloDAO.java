package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Prodotto;

import java.util.List;

public interface CarrelloDAO {
    void addAlCarrello(int userId, int prodottoId, int quantita);
    List<Prodotto> getCarrello(int userId);
    List<Carrello> getCartDetails(int userId);
    void clear(int userId);
    void removeFromCart(int userId, int productId);
    void updateCartItem(int userId, int productId, int quantity, String taglia);
    void updateCartItemTaglia(int userId, int productId, String taglia);

    void initializeEmptyCart(int id);
}