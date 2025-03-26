package it.unical.progweb.controller;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.DettagliOrdini;
import it.unical.progweb.model.Ordine;
import it.unical.progweb.model.Prodotto;
import it.unical.progweb.model.request.OrdineRequest;
import it.unical.progweb.service.OrdineService;
import it.unical.progweb.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private ProdottoService prodottoService;

    // Creazione ordine
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrdineRequest orderRequest) {
        try {
            int ordineId = ordineService.createOrder(
                    orderRequest.getUserId(),
                    orderRequest.getIdMetodoPagamento(),
                    orderRequest.getArticoliCarrello()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrdineResponse(ordineId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante la creazione dell'ordine: " + e.getMessage());
        }
    }

    // Recupero ordini utente
    @GetMapping("/utente/{userId}")
    public ResponseEntity<List<Ordine>> getUserOrders(@PathVariable int userId) {
        try {
            List<Ordine> ordini = ordineService.getUserOrders(userId);
            return ResponseEntity.ok(ordini);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Dettaglio singolo ordine
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int orderId) {
        try {
            // Ottieni l'ordine
            Ordine ordine = ordineService.getOrderById(orderId);

            // Ottieni i dettagli dell'ordine (articoli)
            List<DettagliOrdini> dettagliBase = ordineService.getOrderDetails(orderId);

            // Crea la lista di dettagli arricchita con informazioni sui prodotti
            List<DettagliOrdineConProdotto> dettagliCompleti = new ArrayList<>();

            for (DettagliOrdini dettaglio : dettagliBase) {
                Prodotto prodotto = null;
                try {
                    prodotto = prodottoService.getProductById(dettaglio.getIdProdotto());
                } catch (NotFoundException e) {
                    // Il prodotto non esiste più, usiamo informazioni di base
                }

                // Crea un oggetto di dettaglio arricchito
                DettagliOrdineConProdotto dettaglioCompleto = new DettagliOrdineConProdotto(
                        dettaglio.getId(),
                        dettaglio.getIdOrdine(),
                        dettaglio.getIdProdotto(),
                        dettaglio.getQuantita(),
                        prodotto != null ? prodotto.getNome() : "Prodotto non disponibile",
                        prodotto != null ? prodotto.getPrezzo() : 0
                );

                dettagliCompleti.add(dettaglioCompleto);
            }

            // Crea la risposta completa
            OrderDetailResponse response = new OrderDetailResponse(ordine, dettagliCompleti);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il recupero dei dettagli dell'ordine: " + e.getMessage());
        }
    }

    // Classe interna per la risposta con dettagli dell'ordine
    public static class OrderDetailResponse {
        private Ordine ordine;
        private List<DettagliOrdineConProdotto> dettagli;

        public OrderDetailResponse(Ordine ordine, List<DettagliOrdineConProdotto> dettagli) {
            this.ordine = ordine;
            this.dettagli = dettagli;
        }

        // Getter necessari per la serializzazione JSON
        public Ordine getOrdine() {
            return ordine;
        }

        public List<DettagliOrdineConProdotto> getDettagli() {
            return dettagli;
        }
    }

    // Classe per risposta alla creazione dell'ordine
    public static class OrdineResponse {
        private int id;

        public OrdineResponse(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Classe per dettagli dell'ordine arricchiti con informazioni sul prodotto
    public static class DettagliOrdineConProdotto {
        private int id;
        private int idOrdine;
        private int idProdotto;
        private int quantita;
        private String nomeProdotto;
        private int prezzoProdotto;

        public DettagliOrdineConProdotto(int id, int idOrdine, int idProdotto, int quantita,
                                         String nomeProdotto, int prezzoProdotto) {
            this.id = id;
            this.idOrdine = idOrdine;
            this.idProdotto = idProdotto;
            this.quantita = quantita;
            this.nomeProdotto = nomeProdotto;
            this.prezzoProdotto = prezzoProdotto;
        }

        // Getter necessari per la serializzazione JSON
        public int getId() {
            return id;
        }

        public int getIdOrdine() {
            return idOrdine;
        }

        public int getIdProdotto() {
            return idProdotto;
        }

        public int getQuantita() {
            return quantita;
        }

        public String getNomeProdotto() {
            return nomeProdotto;
        }

        public int getPrezzoProdotto() {
            return prezzoProdotto;
        }
    }
}