package it.unical.progweb.controller;

import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.service.MetodoDiPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/metodipagamento")
public class MetodoPagamentoController {

    @Autowired
    private MetodoDiPagamentoService metodoDiPagamentoService;

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<MetodoDiPagamento>> getMetodiPagamentoUtente(@PathVariable int utenteId) {
        List<MetodoDiPagamento> metodiPagamento = metodoDiPagamentoService.getMetodiPagamentoUtente(utenteId);
        return ResponseEntity.ok(metodiPagamento);
    }

    @PostMapping
    public ResponseEntity<?> addMetodoPagamento(@RequestBody MetodoDiPagamento metodoPagamento) {
        metodoDiPagamentoService.salvaMetodoDiPagamento(metodoPagamento, metodoPagamento.getIdUtente());
        return ResponseEntity.ok().build();
    }


}