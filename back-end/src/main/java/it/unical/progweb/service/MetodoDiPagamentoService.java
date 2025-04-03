package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.persistence.dao.MetodoDiPagamentoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodoDiPagamentoService {

    private final MetodoDiPagamentoDAO metodoDiPagamentoDAO;

    @Autowired
    public MetodoDiPagamentoService(MetodoDiPagamentoDAO metodoDiPagamentoDAO) {
        this.metodoDiPagamentoDAO = metodoDiPagamentoDAO;
    }

    public void salvaMetodoDiPagamento(MetodoDiPagamento metodoPagamento, int userId) {
        try {
            System.out.println("Service: Salvataggio metodo di pagamento per utente ID: " + userId);
            System.out.println("Service: Dati metodo di pagamento: " + metodoPagamento);

            if (metodoPagamento.getIdUtente() <= 0) {
                metodoPagamento.setIdUtente(userId);
            }

            validaMetodoPagamento(metodoPagamento);

            String numeroCarta = metodoPagamento.getNumeroCarta().replaceAll("\\s+", "");
            metodoPagamento.setNumeroCarta(numeroCarta);

            boolean risultato = metodoDiPagamentoDAO.addMetodoDiPagamento(metodoPagamento, userId);

            if (!risultato) {
                throw new RuntimeException("Errore nel salvataggio del metodo di pagamento");
            }

        } catch (Exception e) {
            System.err.println("Service: Errore durante il salvataggio del metodo di pagamento: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<MetodoDiPagamento> getMetodiPagamentoUtente(int userId) {
        System.out.println("Service: Recupero metodi di pagamento per utente ID: " + userId);
        return metodoDiPagamentoDAO.findByUtenteId(userId);
    }

    public MetodoDiPagamento getMetodoDiPagamentoByID(int id) {
        MetodoDiPagamento method = metodoDiPagamentoDAO.findById(id);
        if (method == null) {
            throw new NotFoundException("Metodo di pagamento non trovato");
        }
        return method;
    }


    public void disattivaMetodoDiPagamento(int id) {
        System.out.println("Service: Disattivazione metodo di pagamento con ID " + id);
        metodoDiPagamentoDAO.disattivaMetodoDiPagamento(id);
    }

    private void validaMetodoPagamento(MetodoDiPagamento method) {
        if (method.getTitolare() == null || method.getTitolare().trim().isEmpty()) {
            throw new IllegalArgumentException("Titolare della carta obbligatorio");
        }

        if (method.getNumeroCarta() == null || !isValidCardNumber(method.getNumeroCarta())) {
            throw new IllegalArgumentException("Numero carta non valido");
        }

        if (method.getCvv() == null || !isValidCVV(method.getCvv())) {
            throw new IllegalArgumentException("CVV non valido");
        }

        if (method.getDataScadenza() == null) {
            throw new IllegalArgumentException("Data di scadenza non valida");
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        String cleanCardNumber = cardNumber.replaceAll("\\s+", "");
        return cleanCardNumber.matches("\\d{16}");
    }

    private boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }

    private boolean isValidExpiryDate(String expiryDate) {
        return expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}") || true; // Temporaneamente permissivo
    }
}