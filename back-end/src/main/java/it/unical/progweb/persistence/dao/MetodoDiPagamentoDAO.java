package it.unical.progweb.persistence.dao;

import it.unical.progweb.model.MetodoDiPagamento;

import java.util.List;

public interface MetodoDiPagamentoDAO {
    MetodoDiPagamento findById(int id);
    List<MetodoDiPagamento> findByUtenteId(int utenteId);
    void save(MetodoDiPagamento metodoDiPagamento);
    void update(MetodoDiPagamento metodoDiPagamento);
    void delete(int id);
}
