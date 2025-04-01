package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.*;
import it.unical.progweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public ResponseEntity<?> getAdminUsers() {
        List<Utente> adminUsers = userService.getAllUtenti().stream()
                .filter(Utente::getRuolo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adminUsers);
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomerUsers() {
        List<Utente> customerUsers = userService.getAllUtenti().stream()
                .filter(utente -> !utente.getRuolo())
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerUsers);
    }


    @PostMapping("/indirizzi")
    public ResponseEntity<?> aggiungiIndirizzo(@RequestBody Indirizzo indirizzo) {
        try {
            userService.aggiungiIndirizzoSpedizione(indirizzo.getIdUtente(), indirizzo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Indirizzo aggiunto");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable int id) {
        try {
            Utente utente = userService.findById(id);
            return ResponseEntity.ok(utente);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    public static class LoginResponse {
        private int id;
        private String nome;
        private Boolean isAdmin;

        public LoginResponse(int id, String nome, Boolean isAdmin) {
            this.id = id;
            this.nome = nome;
            this.isAdmin = isAdmin;
        }

        public int getId() { return id; }
        public String getNome() { return nome; }
        public Boolean getIsAdmin() { return isAdmin; }
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Utente utente = userService.findByEmail(email);
            if (utente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
            }

            // Crea un oggetto di risposta senza dati sensibili
            Map<String, Object> response = new HashMap<>();
            response.put("id", utente.getId());
            response.put("nome", utente.getNome());
            response.put("cognome", utente.getCognome());
            response.put("email", utente.getEmail());
            response.put("ruolo", utente.getRuolo());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recupero dell'utente");
        }
    }

    @PatchMapping("/{id}/promuovi")
    public ResponseEntity<?> promuoviUtente(@PathVariable int id) {
        try {
            userService.promuoviAAdmin(id);
            return ResponseEntity.ok("Utente promosso ad amministratore con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la promozione.");
        }
    }


    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> eliminaUtente(@PathVariable int id) {
        try {
            userService.eliminaUtente(id);
            return ResponseEntity.ok("Utente eliminato con successo.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'utente.");
        }
    }

}