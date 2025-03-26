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

    @GetMapping("/utente/{userId}")
    public ResponseEntity<List<MetodoDiPagamento>> getMetodiPagamentoUtente(@PathVariable int userId) {
        try {
            List<MetodoDiPagamento> metodiPagamento = metodoDiPagamentoService.getMetodiPagamentoUtente(userId);
            return ResponseEntity.ok(metodiPagamento);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMetodoPagamentoById(@PathVariable int id) {
        try {
            System.out.println("Ricevuta richiesta per metodo di pagamento con ID: " + id);
            MetodoDiPagamento metodoPagamento = metodoDiPagamentoService.getMetodoDiPagamentoByID(id);
            return ResponseEntity.ok(metodoPagamento);
        } catch (NotFoundException e) {
            System.err.println("Metodo di pagamento non trovato: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero del metodo di pagamento: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> salvaMetodoPagamento(@RequestBody MetodoDiPagamento metodoPagamento) {
        try {
            // Il parametro userId dovrebbe essere incluso nella richiesta
            int userId = metodoPagamento.getIdUtente();
            metodoDiPagamentoService.salvaMetodoDiPagamento(metodoPagamento, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Metodo di pagamento salvato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel salvataggio del metodo di pagamento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMetodoPagamento(@PathVariable int id, @RequestBody MetodoDiPagamento metodoPagamento) {
        try {
            // Verifica che l'ID nel path corrisponda all'ID nell'oggetto
            if (metodoPagamento.getId() != id) {
                return ResponseEntity.badRequest().body("ID nel path non corrisponde all'ID nell'oggetto");
            }


            return ResponseEntity.ok("Metodo di pagamento aggiornato con successo");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'aggiornamento del metodo di pagamento: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMetodoPagamento(@PathVariable int id) {
        try {
            return ResponseEntity.ok("Metodo di pagamento eliminato con successo");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nell'eliminazione del metodo di pagamento: " + e.getMessage());
        }
    }
}