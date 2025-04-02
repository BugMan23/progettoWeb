package it.unical.progweb.controller;

import it.unical.progweb.model.Disponibilita;
import it.unical.progweb.service.DisponibilitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/disponibilita")
public class DisponibilitaController {

    @Autowired
    private DisponibilitaService disponibilitaService;

    @GetMapping("/prodotto/{prodottoId}")
    public ResponseEntity<List<Disponibilita>> getDisponibilitaProdotto(@PathVariable int prodottoId) {
        List<Disponibilita> disponibilita = disponibilitaService.getDisponibilitaProdotto(prodottoId);
        return ResponseEntity.ok(disponibilita);
    }

    @PutMapping
    public ResponseEntity<?> updateDisponibilita(@RequestBody DisponibilitaRequest request) {
        disponibilitaService.updateDisponibilita(
                request.getProdottoId(),
                request.getQuantita(),
                request.getTaglia()
        );
        return ResponseEntity.ok("Disponibilità aggiornata");
    }

    @PutMapping("/decrementa")
    public ResponseEntity<?> decrementaQuantita(@RequestBody DisponibilitaRequest request) {
        disponibilitaService.decrementaQuantita(
                request.getProdottoId(),
                request.getQuantita(),
                request.getTaglia()
        );
        return ResponseEntity.ok("Quantità decrementata");
    }

    @PostMapping("/admin")
    public ResponseEntity<?> addDisponibilita(@RequestBody List<Disponibilita> disponibilita) {
        try {
            for (Disponibilita d : disponibilita) {
                disponibilitaService.aggiungiDisponibilita(d);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore nell'inserimento delle disponibilità");
        }
    }


    // Classe di supporto per le richieste
    public static class DisponibilitaRequest {
        private int prodottoId;
        private int quantita;
        private String taglia;

        // Getter e setter
        public int getProdottoId() { return prodottoId; }
        public void setProdottoId(int prodottoId) { this.prodottoId = prodottoId; }
        public int getQuantita() { return quantita; }
        public void setQuantita(int quantita) { this.quantita = quantita; }
        public String getTaglia() { return taglia; }
        public void setTaglia(String taglia) { this.taglia = taglia; }
    }
}