package it.unical.progweb.controller;

import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.service.MetodoDiPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/metodipagamento")  // Nota: questo dovrebbe corrispondere all'URL nel frontend
public class MetodoPagamentoController {

    @Autowired
    private MetodoDiPagamentoService metodoDiPagamentoService;

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<MetodoDiPagamento>> getMetodiPagamentoByUtente(@PathVariable int utenteId) {
        List<MetodoDiPagamento> metodiPagamento = metodoDiPagamentoService.getMetodiPagamentoUtente(utenteId);
        return ResponseEntity.ok(metodiPagamento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoDiPagamento> getMetodoPagamentoById(@PathVariable int id) {
        MetodoDiPagamento metodoPagamento = metodoDiPagamentoService.getMetodoDiPagamentoByID(id);
        if (metodoPagamento != null) {
            return ResponseEntity.ok(metodoPagamento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addMetodoPagamento(@RequestBody MetodoDiPagamento metodoPagamento) {
        try {
            metodoDiPagamentoService.salvaMetodoDiPagamento(metodoPagamento, metodoPagamento.getIdUtente());
             return ResponseEntity.ok("Metodo di pagamento aggiunto con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMetodoPagamento(@PathVariable int id, @RequestBody MetodoDiPagamento metodoPagamento) {
        // Implementa l'aggiornamento se necessario
        return ResponseEntity.ok("Metodo di pagamento aggiornato");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMetodoPagamento(@PathVariable int id) {
        // Implementa l'eliminazione se necessario
        return ResponseEntity.ok("Metodo di pagamento eliminato");
    }
}