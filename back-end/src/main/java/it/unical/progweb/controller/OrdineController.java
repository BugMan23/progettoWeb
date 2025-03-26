package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.request.OrdineRequest;
import it.unical.progweb.service.OrdineService;
import it.unical.progweb.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private ProdottoService prodottoService;

    // Creazione ordine
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrdineRequest orderRequest) {
        try {
            int ordineId = ordineService.createOrder(
                    orderRequest.getUserId(),
                    orderRequest.getIdMetodoPagamento(),
                    orderRequest.getArticoliCarrello()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("id", ordineId);
            response.put("message", "Ordine creato con successo");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    // Recupero degli articoli di un ordine
    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable int orderId) {
        try {
            List<DettagliOrdini> dettagli = ordineService.getOrderDetails(orderId);
            return ResponseEntity.ok(dettagli);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Recupero prodotti di un ordine con dettagli
    @GetMapping("/{orderId}/products")
    public ResponseEntity<?> getOrderProducts(@PathVariable int orderId) {
        try {
            List<DettagliOrdini> dettagli = ordineService.getOrderDetails(orderId);
            List<OrderProductResponse> prodotti = new ArrayList<>();

            for (DettagliOrdini dettaglio : dettagli) {
                Prodotto prodotto = prodottoService.getProductById(dettaglio.getIdProdotto());
                prodotti.add(new OrderProductResponse(dettaglio, prodotto));
            }

            return ResponseEntity.ok(prodotti);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Aggiorna lo stato di un ordine (solo admin)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable int orderId,
            @RequestBody Map<String, String> request) {
        try {
            String newStatus = request.get("status");
            if (newStatus == null) {
                return ResponseEntity.badRequest().body("Status field is required");
            }

            ordineService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok("Stato ordine aggiornato con successo");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

        public Ordine getOrdine() {
            return ordine;
        }

        public List<DettagliOrdini> getDettagli() {
            return dettagli;
        }
    }

    // Classe per la risposta con prodotto e dettagli ordine
    public static class OrderProductResponse {
        private DettagliOrdini dettaglio;
        private Prodotto prodotto;

        public OrderProductResponse(DettagliOrdini dettaglio, Prodotto prodotto) {
            this.dettaglio = dettaglio;
            this.prodotto = prodotto;
        }

        public DettagliOrdini getDettaglio() {
            return dettaglio;
        }

        public Prodotto getProdotto() {
            return prodotto;
        }
    }
}