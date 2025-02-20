package it.unical.progweb.controller;

import it.unical.progweb.model.Utente;
import it.unical.progweb.model.request.LoginRequest;
import it.unical.progweb.service.UserService;
import it.unical.progweb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Utente utente = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new UtenteController.LoginResponse(
                    utente.getId(),
                    utente.getNome(),
                    utente.getRuolo()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenziali non valide");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utente utente) {
        try {
            userService.registraUtente(utente);
            return ResponseEntity.ok("Registrazione completata con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

