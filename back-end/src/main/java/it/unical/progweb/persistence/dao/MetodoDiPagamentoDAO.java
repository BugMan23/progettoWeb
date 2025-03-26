package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.MetodoDiPagamento;

import java.util.List;

public interface MetodoDiPagamentoDAO {
    List<MetodoDiPagamento> findByUtenteId(int utenteId);
    boolean addMetodoDiPagamento(MetodoDiPagamento metodoDiPagamento, int utenteId);
    MetodoDiPagamento findById(int id);
    boolean updateMetodoDiPagamento(MetodoDiPagamento metodoDiPagamento);
    boolean deleteMetodoDiPagamento(int id);
}