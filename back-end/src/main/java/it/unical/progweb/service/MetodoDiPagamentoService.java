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
        validaMetodoPagamento(metodoPagamento);

        // Rimuovi gli spazi dal numero della carta
        String numeroCarta = metodoPagamento.getNumeroCarta().replaceAll("\\s+", "");
        metodoPagamento.setNumeroCarta(numeroCarta);

        boolean risultato = metodoDiPagamentoDAO.addMetodoDiPagamento(metodoPagamento, userId);
        if (!risultato) {
            throw new RuntimeException("Errore nel salvataggio del metodo di pagamento");
        }
    }

    public List<MetodoDiPagamento> getMetodiPagamentoUtente(int userId) {
        return metodoDiPagamentoDAO.findByUtenteId(userId);
    }

    public MetodoDiPagamento getMetodoDiPagamentoByID(int id) {
        MetodoDiPagamento method = metodoDiPagamentoDAO.findById(id);
        if (method == null) {
            throw new NotFoundException("Metodo di pagamento non trovato");
        }
        return method;
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

}