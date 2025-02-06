package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.AuthenticationException;
import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.*;
import it.unical.progweb.model.request.LoginRequest;
import it.unical.progweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    @Autowired
    private UserService userService;

    @PostMapping("/registrazione")
    public ResponseEntity<?> registerUser(@RequestBody Utente utente) {
        try {
            utente.setRuolo(false);
            userService.registraUtente(utente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utente registrato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Utente utente = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new LoginResponse(utente.getId(), utente.getNome(), utente.getRuolo()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

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

    /*@PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            userService.changePassword(
                    passwordChangeRequest.getUserId(),
                    passwordChangeRequest.getCurrentPassword(),
                    passwordChangeRequest.getNewPassword()
            );
            return ResponseEntity.ok("Password modificata con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/


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
}