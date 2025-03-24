package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.request.OrdineRequest;
import it.unical.progweb.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrdineRequest orderRequest) {
        try {
            ordineService.createOrder(
                    orderRequest.getUserId(),
                    orderRequest.getIdMetodoPagamento(),
                    orderRequest.getArticoliCarrello()
            );
            // Restituisci un oggetto JSON valido invece di una stringa
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new HashMap<String, Object>() {{
                        put("message", "Ordine creato con successo");
                        put("success", true);
                    }}
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HashMap<String, Object>() {{
                        put("message", e.getMessage());
                        put("success", false);
                    }}
            );
        }
    }

    // Recupero ordini utente
    @GetMapping("/utente/{userId}")
    public ResponseEntity<List<Ordine>> getUserOrders(@PathVariable int userId) {
        List<Ordine> ordini = ordineService.getUserOrders(userId);
        return ResponseEntity.ok(ordini);
    }

    // Dettaglio singolo ordine
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int orderId) {
        try {
            Ordine ordine = ordineService.getOrderById(orderId);
            List<DettagliOrdini> dettagli = ordineService.getOrderDetails(orderId);
            return ResponseEntity.ok(new OrderDetailResponse(ordine, dettagli));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    // Classe per la risposta con dettagli ordine
    public static class OrderDetailResponse {
        private Ordine ordine;
        private List<DettagliOrdini> dettagli;

        public OrderDetailResponse(Ordine ordine, List<DettagliOrdini> dettagli) {
            this.ordine = ordine;
            this.dettagli = dettagli;
        }

    }
}
