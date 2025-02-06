package it.unical.progweb.controller;

import it.unical.progweb.model.Recensione;
import it.unical.progweb.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/reviews")
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    // Aggiunta recensione
    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Recensione recensione) {
        try {
            recensioneService.addReview(recensione);
            return ResponseEntity.status(HttpStatus.CREATED).body("Recensione aggiunta con successo");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Recupero recensioni per prodotto
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Recensione>> getProductReviews(@PathVariable int productId) {
        List<Recensione> recensioni = recensioneService.getProductReviews(productId);
        return ResponseEntity.ok(recensioni);
    }

    // Endpoint admin per eliminare recensione
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable int reviewId) {
        try {
            // Implementa logica di eliminazione recensione
            return ResponseEntity.ok("Recensione eliminata");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}