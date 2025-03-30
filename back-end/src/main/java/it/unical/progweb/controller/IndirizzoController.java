package it.unical.progweb.controller;

import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.service.IndirizzoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/indirizzi")
public class IndirizzoController {

    @Autowired
    private IndirizzoService indirizzoService;

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<Indirizzo>> getIndirizziByUtenteId(@PathVariable int utenteId) {
        List<Indirizzo> indirizzi = indirizzoService.getIndirizziByUtenteId(utenteId);
        return ResponseEntity.ok(indirizzi);
    }

    @PostMapping
    public ResponseEntity<?> saveIndirizzo(@RequestBody Indirizzo indirizzo) {
        try {
            if (indirizzo.getIdUtente() <= 0) {
                return ResponseEntity.badRequest().body("ID utente mancante o non valido");
            }
            indirizzoService.addIndirizzo(indirizzo, indirizzo.getIdUtente());
            return ResponseEntity.status(HttpStatus.CREATED).body("Indirizzo salvato con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore nel salvataggio dell'indirizzo: " + e.getMessage());
        }
    }


    @PutMapping("/disattiva/{id}")
    public ResponseEntity<?> disattivaIndirizzo(@PathVariable int id) {
        try {
            indirizzoService.disattivaIndirizzo(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Indirizzo rimosso con successo");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Errore nella rimozione dell'indirizzo: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}