package it.unical.progweb.controller;

import it.unical.progweb.model.Utente;
import it.unical.progweb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Utente utente) {
        boolean success = authService.register(utente);
        return success ? ResponseEntity.ok("Registrazione completata") :
                ResponseEntity.badRequest().body("Errore nella registrazione");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Utente utente) {
        System.out.println("Login attempt received for email: " + utente.getEmail());
        try {
            String token = authService.login(utente.getEmail(), utente.getPassword());
            System.out.println("Login successful, generated token for user: " + utente.getEmail());
            return token != null ? ResponseEntity.ok(token) :
                    ResponseEntity.status(401).body("Credenziali errate");
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Errore durante il login: " + e.getMessage());
        }}
}