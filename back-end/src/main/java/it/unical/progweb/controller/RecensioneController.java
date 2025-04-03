package it.unical.progweb.controller;

import it.unical.progweb.model.Recensione;
import it.unical.progweb.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/reviews")
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Recensione recensione) {
        try {
            recensioneService.addReview(recensione);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Recensione aggiunta con successo");
            response.put("success", true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Recensione>> getProductReviews(@PathVariable int productId) {
        List<Recensione> recensioni = recensioneService.getProductReviews(productId);
        return ResponseEntity.ok(recensioni);
    }


    @GetMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<List<Recensione>> getUserProductReview(@PathVariable int userId, @PathVariable int productId) {
        List<Recensione> recensioni = recensioneService.getUserProductReviews(userId, productId);
        return ResponseEntity.ok(recensioni);
    }

    // Recupero recensioni di un utente
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recensione>> getUserReviews(@PathVariable int userId) {
        List<Recensione> recensioni = recensioneService.getUserReviews(userId);
        return ResponseEntity.ok(recensioni);
    }

    // Endpoint admin per eliminare recensione
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable int reviewId) {
        try {
            recensioneService.deleteReview(reviewId);
            return ResponseEntity.ok("Recensione eliminata");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}