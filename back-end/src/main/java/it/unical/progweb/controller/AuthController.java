package it.unical.progweb.controller;

import it.unical.progweb.model.Utente;
import it.unical.progweb.model.request.LoginRequest;
import it.unical.progweb.service.UserService;
import it.unical.progweb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Utente utente) {
        boolean success = authService.register(utente);
        return success ? ResponseEntity.ok("Registrazione completata") :
                ResponseEntity.badRequest().body("Errore nella registrazione");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            if (token != null) {
                // Ottieni i dati dell'utente
                Utente utente = userService.findByEmail(loginRequest.getEmail());

                // Crea un oggetto di risposta
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", utente.getId());
                response.put("nome", utente.getNome());
                response.put("isAdmin", utente.getRuolo());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Credenziali errate");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Errore durante il login: " + e.getMessage());
        }
    }
}