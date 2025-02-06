package it.unical.progweb.service;

import it.unical.progweb.eccezioni.NotFoundException;
import it.unical.progweb.model.MetodoDiPagamento;
import it.unical.progweb.persistence.DBManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodoDiPagamentoService {


    public void salvaMetodoDiPagamento(MetodoDiPagamento metodoPagamento, int userId) {
        validaMetodoPagamento(metodoPagamento);
        if ( !DBManager.getInstance().getPaymentMethodDAO().addMetodoDiPagamento(metodoPagamento, userId)) {
            throw new RuntimeException("Errore nel salvataggio del metodo di pagamento");
        }
    }

    public List<MetodoDiPagamento> getMetodiPagamentoUtente(int userId) {
        return DBManager.getInstance().getPaymentMethodDAO().findByUtenteId(userId);
    }

    public MetodoDiPagamento getMetodoDiPagamentoByID(int id) {
        MetodoDiPagamento method = DBManager.getInstance().getPaymentMethodDAO().findById(id);
        if (method == null) {
            throw new NotFoundException("Metodo di pagamento non trovato");
        }
        return method;
    }

    private void validaMetodoPagamento(MetodoDiPagamento method) {
        if (method.getNumeroCarta() == null || !isValidCardNumber(method.getNumeroCarta())) {
            throw new IllegalArgumentException("Numero carta non valido");
        }
        if (method.getCvv() == null || !isValidCVV(method.getCvv())) {
            throw new IllegalArgumentException("CVV non valido");
        }
        if (method.getDataScadenza() == null || !isValidExpiryDate(method.getDataScadenza())) {
            throw new IllegalArgumentException("Data di scadenza non valida");
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }

    private boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }

    private boolean isValidExpiryDate(String expiryDate) {
        return expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}");
    }
}