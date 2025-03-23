package it.unical.progweb.controller;


import it.unical.progweb.model.Indirizzo;
import it.unical.progweb.persistence.dao.IndirizzoDAO;
import it.unical.progweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/indirizzi")
public class IndirizzoController {

    @Autowired
    private UserService userService;
    @Autowired
    private IndirizzoDAO indirizzoDAO;

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<Indirizzo>> findByUtenteId(@PathVariable int utenteId) {
        List<Indirizzo> indirizzi = userService.getUserAddresses(utenteId);
        return ResponseEntity.ok(indirizzi);
    }

    @PostMapping
    public ResponseEntity<?> addIndirizzo(@RequestBody Indirizzo indirizzo) {
        userService.aggiungiIndirizzoSpedizione(indirizzo.getIdUtente(), indirizzo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/indirizzi/utente/{utenteId}")
    public ResponseEntity<List<Indirizzo>> getIndirizziByUtenteId(@PathVariable int utenteId) {
        List<Indirizzo> indirizzi = indirizzoDAO.findByUtenteId(utenteId);
        return ResponseEntity.ok(indirizzi);
    }
}