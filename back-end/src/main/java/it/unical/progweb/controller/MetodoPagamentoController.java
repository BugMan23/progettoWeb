package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.service.MetodoDiPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/metodipagamento")
public class MetodoPagamentoController {

    @Autowired
    private MetodoDiPagamentoService metodoDiPagamentoService;

    // Ottiene tutti i metodi di pagamento dell'utente
    @GetMapping("/utente/{userId}")
    public ResponseEntity<List<MetodoDiPagamento>> getMetodiPagamentoUtente(@PathVariable int userId) {
        List<MetodoDiPagamento> metodiPagamento = metodoDiPagamentoService.getMetodiPagamentoUtente(userId);
        return ResponseEntity.ok(metodiPagamento);
    }

    // Ottiene un metodo di pagamento specifico tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMetodoPagamentoById(@PathVariable int id) {
        try {
            MetodoDiPagamento metodoPagamento = metodoDiPagamentoService.getMetodoDiPagamentoByID(id);
            return ResponseEntity.ok(metodoPagamento);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Aggiunge un nuovo metodo di pagamento
    @PostMapping
    public ResponseEntity<?> saveMetodoPagamento(@RequestBody MetodoDiPagamento metodoPagamento,
                                                 @RequestParam int userId) {
        try {
            metodoDiPagamentoService.salvaMetodoDiPagamento(metodoPagamento, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Metodo di pagamento aggiunto con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Aggiorna un metodo di pagamento esistente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMetodoPagamento(@PathVariable int id,
                                                   @RequestBody MetodoDiPagamento metodoPagamento) {
        try {
            // Assicuriamoci che l'ID nel path coincida con quello nell'oggetto
            if (metodoPagamento.getId() != id) {
                metodoPagamento.setId(id);
            }

            metodoDiPagamentoService.updateMetodoDiPagamento(metodoPagamento);
            return ResponseEntity.ok("Metodo di pagamento aggiornato con successo");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Elimina un metodo di pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMetodoPagamento(@PathVariable int id) {
        try {
            metodoDiPagamentoService.deleteMetodoDiPagamento(id);
            return ResponseEntity.ok("Metodo di pagamento eliminato con successo");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}