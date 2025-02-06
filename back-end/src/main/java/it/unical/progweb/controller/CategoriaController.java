package it.unical.progweb.controller;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.Categoria;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.service.CategoriaService;
import it.unical.progweb.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/categorie")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProdottoService prodottoService;

    // Recupera tutte le categorie
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategories() {
        return ResponseEntity.ok(categoriaService.getAllCategories());
    }

    // Recupera prodotti per categoria
    @GetMapping("/{categoriaId}/prodotti")
    public ResponseEntity<List<Prodotto>> getProductsByCategory(@PathVariable int categoriaId) {
        try {
            Categoria categoria = categoriaService.getCategoriaById(categoriaId);
            List<Prodotto> prodotti = prodottoService.getProductsByCategory(categoria.getNome());
            return ResponseEntity.ok(prodotti);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Aggiunta categoria (solo admin)
    @PostMapping("/admin")
    public ResponseEntity<?> addCategory(@RequestBody Categoria categoria) {
        try {
            categoriaService.addCategory(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body("Categoria aggiunta");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}