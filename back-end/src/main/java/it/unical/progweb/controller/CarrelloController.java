package it.unical.progweb.controller;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Carrello;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.request.CarrelloRequest;
import it.unical.progweb.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    // Aggiunta prodotto al carrello
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CarrelloRequest cartRequest) {
        try {
            carrelloService.addAlCarrello(
                    cartRequest.getUserId(),
                    cartRequest.getProductId(),
                    cartRequest.getQuantity(),
                    cartRequest.getTaglia()
            );
            return ResponseEntity.ok("Prodotto aggiunto al carrello");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Recupero carrello utente
    @GetMapping("/{userId}")
    public ResponseEntity<List<Prodotto>> getUserCart(@PathVariable int userId) {
        List<Prodotto> carrello = carrelloService.getCart(userId);
        return ResponseEntity.ok(carrello);
    }

    // Recupero dettagli prodotti nel carrello (quantità, taglia)
    @GetMapping("/details/{userId}")
    public ResponseEntity<List<Carrello>> getCartDetails(@PathVariable int userId) {
        List<Carrello> dettagli = carrelloService.getCartDetails(userId);
        return ResponseEntity.ok(dettagli);
    }

    // Calcolo totale carrello
    @GetMapping("/{userId}/total")
    public ResponseEntity<Integer> getCartTotal(@PathVariable int userId) {
        int total = carrelloService.getCartTotal(userId);
        return ResponseEntity.ok(total);
    }

    // Conteggio elementi nel carrello
    @GetMapping("/{userId}/count")
    public ResponseEntity<Integer> getCartCount(@PathVariable int userId) {
        int count = carrelloService.getCartCount(userId);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable int userId) {
        try {
            System.out.println("Richiesta di contrassegnare il carrello come ordinato per utente: " + userId);
            carrelloService.clearCart(userId);
            // Restituisce una risposta esplicita con un messaggio più appropriato
            return ResponseEntity.ok("Carrello confermato come ordinato");
        } catch (Exception e) {
            System.err.println("Errore nell'aggiornamento dello stato del carrello: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }

    // Rimuovi prodotto dal carrello
    @DeleteMapping("/{userId}/product/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable int userId, @PathVariable int productId) {
        carrelloService.removeFromCart(userId, productId);
        return ResponseEntity.ok("Prodotto rimosso dal carrello");
    }

    // Aggiorna quantità e taglia
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody Map<String, Object> request) {
        try {
            int userId = (Integer) request.get("userId");
            int productId = (Integer) request.get("productId");
            int quantity = (Integer) request.get("quantity");
            String taglia = (String) request.get("taglia");

            carrelloService.updateCartItem(userId, productId, quantity, taglia);
            return ResponseEntity.ok("Articolo aggiornato");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Aggiorna solo la taglia
    @PutMapping("/update-taglia")
    public ResponseEntity<?> updateCartItemTaglia(@RequestBody Map<String, Object> request) {
        try {
            int userId = (Integer) request.get("userId");
            int productId = (Integer) request.get("productId");
            String taglia = (String) request.get("taglia");

            carrelloService.updateCartItemTaglia(userId, productId, taglia);
            return ResponseEntity.ok("Taglia aggiornata");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}