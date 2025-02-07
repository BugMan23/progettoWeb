package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.Recensione;
import it.unical.progweb.service.ProdottoService;
import it.unical.progweb.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private RecensioneService recensioneService;

    // Endpoint per tutti i prodotti
    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProducts() {
        return ResponseEntity.ok(prodottoService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        try {
            Prodotto prodotto = prodottoService.getProductById(id);
            List<Recensione> recensioni = recensioneService.getProductReviews(id);
            return ResponseEntity.ok(new ProductDetailResponse(prodotto, recensioni));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Filtri prodotti
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Prodotto>> getProductsByCategory(@PathVariable String categoria) {
        return ResponseEntity.ok(prodottoService.getProductsByCategory(categoria));
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Prodotto>> getProductByName(@PathVariable String nome) {
        return ResponseEntity.ok(prodottoService.getProductsByName(nome));
    }

    @GetMapping("/colore")
    public ResponseEntity<List<Prodotto>> getProductByColore(@PathVariable String colore) {
        return ResponseEntity.ok(prodottoService.getProductsByColor(colore));
    }

    @GetMapping("/prezzo")
    public ResponseEntity<List<Prodotto>> getProductByPrezzo(@PathVariable int prezzo) {
        return ResponseEntity.ok(prodottoService.getProductsByPrice(prezzo));
    }



    @GetMapping("/prezzoRange")
    public ResponseEntity<List<Prodotto>> getProductsByPriceRange(
            @RequestParam int min,
            @RequestParam int max
    ) {
        return ResponseEntity.ok(prodottoService.getProductsByPriceRange(min, max));
    }

    // Endpoint per admin
    @PostMapping("/admin")
    public ResponseEntity<?> addProduct(@RequestBody Prodotto prodotto) {
        try {
            prodottoService.addProduct(prodotto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Prodotto aggiunto");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class ProductDetailResponse {
        private Prodotto prodotto;
        private List<Recensione> recensioni;

        public ProductDetailResponse(Prodotto prodotto, List<Recensione> recensioni) {
            this.prodotto = prodotto;
            this.recensioni = recensioni;
        }
    }
}