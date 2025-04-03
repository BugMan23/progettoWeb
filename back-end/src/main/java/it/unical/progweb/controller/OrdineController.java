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
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;


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
            response.put("success", true);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (NotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("success", false);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Si è verificato un errore durante la creazione dell'ordine");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("success", false);

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/utente/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable int userId) {
        try {
            List<Ordine> ordini = ordineService.getUserOrders(userId);
            return ResponseEntity.ok(ordini);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Errore nel recupero degli ordini dell'utente");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int orderId) {
        try {
            Ordine ordine = ordineService.getOrderById(orderId);
            List<DettagliOrdini> dettagli = ordineService.getOrderDetails(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("ordine", ordine);
            response.put("dettagli", dettagli);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Errore nel recupero dei dettagli dell'ordine");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/verifica-acquisto")
    public ResponseEntity<?> hasUserPurchasedProduct(
            @RequestParam int userId,
            @RequestParam int productId) {
        try {
            boolean hasProductPurchased = ordineService.haUserPurchasedProduct(userId, productId);

            Map<String, Object> response = new HashMap<>();
            response.put("hasPurchased", hasProductPurchased);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Errore nella verifica dell'acquisto");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable int orderId) {
        try {
            List<DettagliOrdini> dettagli = ordineService.getOrderDetails(orderId);
            return ResponseEntity.ok(dettagli);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Errore nel recupero degli articoli dell'ordine");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}