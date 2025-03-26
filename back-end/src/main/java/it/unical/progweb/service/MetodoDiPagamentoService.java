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
        if (!metodoDiPagamentoDAO.addMetodoDiPagamento(metodoPagamento, userId)) {
            throw new RuntimeException("Errore nel salvataggio del metodo di pagamento");
        }
    }

    public List<MetodoDiPagamento> getMetodiPagamentoUtente(int userId) {
        return metodoDiPagamentoDAO.findByUtenteId(userId);
    }

    public MetodoDiPagamento getMetodoDiPagamentoByID(int id) {
        System.out.println("Richiesta metodo di pagamento con ID: " + id);

        MetodoDiPagamento method = metodoDiPagamentoDAO.findById(id);
        if (method == null) {
            System.err.println("Metodo di pagamento non trovato con ID: " + id);
            throw new NotFoundException("Metodo di pagamento non trovato con ID: " + id);
        }

        System.out.println("Metodo di pagamento trovato: " + method.getTipoCarta() + " - " + method.getTitolare());
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