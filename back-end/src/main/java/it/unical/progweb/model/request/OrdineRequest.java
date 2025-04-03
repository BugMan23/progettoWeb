package it.unical.progweb.model.request;

import it.unical.progweb.model.DettagliOrdini;

import java.util.List;

public class OrdineRequest {
    private int userId;
    private int idMetodoPagamento;
    private List<DettagliOrdini> articoliCarrello;

    public OrdineRequest() {}

    public OrdineRequest(int userId, int idMetodoPagamento, List<DettagliOrdini> articoliCarrello) {
        this.userId = userId;
        this.idMetodoPagamento = idMetodoPagamento;
        this.articoliCarrello = articoliCarrello;
    }

    public int getUserId() {
        return userId;
    }

    public int getIdMetodoPagamento() {
        return idMetodoPagamento;
    }

    public List<DettagliOrdini> getArticoliCarrello() {
        return articoliCarrello;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIdMetodoPagamento(int idMetodoPagamento) {
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public void setArticoliCarrello(List<DettagliOrdini> articoliCarrello) {
        this.articoliCarrello = articoliCarrello;
    }
}