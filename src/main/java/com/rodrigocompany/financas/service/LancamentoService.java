package com.rodrigocompany.financas.service;

import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamento);
    void atualizarStatus(Lancamento lancamento, StatusLancamento status);

    void validar(Lancamento lancamento);

    Optional<Lancamento> obterPorId(Long id);

}
