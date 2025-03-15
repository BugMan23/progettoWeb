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
        String token = authService.login(utente.getEmail(), utente.getPassword());
        return token != null ? ResponseEntity.ok(token) :
                ResponseEntity.status(401).body("Credenziali errate");
    }
}