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

    // Calcolo totale carrello
    @GetMapping("/{userId}/total")
    public ResponseEntity<Integer> getCartTotal(@PathVariable int userId) {
        int total = carrelloService.getCartTotal(userId);
        return ResponseEntity.ok(total);
    }

    // Svuota carrello
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable int userId) {
        carrelloService.clearCart(userId);
        return ResponseEntity.ok("Carrello svuotato");
    }

}