package it.unical.progweb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.Recensione;
import it.unical.progweb.service.ProdottoService;
import it.unical.progweb.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    // Endpoint per tutti i prodotti
    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProducts() {
        try {
            List<Prodotto> prodotti = prodottoService.getAllProducts();
            return ResponseEntity.ok(prodotti);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nel recupero dei prodotti");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id) {
        try {
            Prodotto prodotto = prodottoService.getProductById(id);
            return ResponseEntity.ok(prodotto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recupero del prodotto");
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
    @PostMapping("/admin/completo")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            Prodotto prodotto = mapper.convertValue(payload.get("prodotto"), Prodotto.class);

            List<Disponibilita> disponibilitaList = mapper.convertValue(
                    payload.get("disponibilita"), new TypeReference<List<Disponibilita>>() {}
            );

            prodottoService.addProduct(prodotto, disponibilitaList);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Prodotto e disponibilità inseriti correttamente"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Errore durante la creazione del prodotto con disponibilità");
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Prodotto prodotto) {
        try {
            prodottoService.updateProduct(prodotto);
            return ResponseEntity.ok("Prodotto aggiornato con successo");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'aggiornamento del prodotto");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            prodottoService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    public static class ProductDetailResponse {
        private Prodotto prodotto;
        private List<Recensione> recensioni;

        public ProductDetailResponse(Prodotto prodotto, List<Recensione> recensioni) {
            this.prodotto = prodotto;
            this.recensioni = recensioni;
        }

        public Prodotto getProdotto() {
            return prodotto;
        }

        public List<Recensione> getRecensioni() {
            return recensioni;
        }
    }
}