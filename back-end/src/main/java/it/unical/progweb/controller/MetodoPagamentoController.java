package it.unical.progweb.controller;

import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.service.MetodoDiPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/metodipagamento")
public class MetodoPagamentoController {

    @Autowired
    private MetodoDiPagamentoService metodoDiPagamentoService;

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<MetodoDiPagamento>> getMetodiPagamentoByUtente(@PathVariable int utenteId) {
        try {
            System.out.println("Controller: Richiesta metodi di pagamento per utente ID: " + utenteId);
            List<MetodoDiPagamento> metodiPagamento = metodoDiPagamentoService.getMetodiPagamentoUtente(utenteId);
            return ResponseEntity.ok(metodiPagamento);
        } catch (Exception e) {
            System.err.println("Controller: Errore nel recupero metodi di pagamento: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoDiPagamento> getMetodoPagamentoById(@PathVariable int id) {
        try {
            MetodoDiPagamento metodoPagamento = metodoDiPagamentoService.getMetodoDiPagamentoByID(id);
            return ResponseEntity.ok(metodoPagamento);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addMetodoPagamento(@RequestBody MetodoDiPagamento metodoPagamento) {
        try {
            metodoDiPagamentoService.salvaMetodoDiPagamento(metodoPagamento, metodoPagamento.getIdUtente());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Metodo di pagamento aggiunto con successo");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @PutMapping("/disattiva/{id}")
    public ResponseEntity<?> disattivaMetodoPagamento(@PathVariable int id) {
        try {
            metodoDiPagamentoService.disattivaMetodoDiPagamento(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Metodo di pagamento rimosso con successo");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Errore nella rimozione del metodo di pagamento: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}