package com.rodrigocompany.financas.model.repository;

import com.rodrigocompany.financas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    // public interface LancamentoRepository extends JpaRepository<Objeto, Tipo da chave primaria>
    // com o JpaRepository os metodos padroes ja estao criados
    // Inserir , consulatr , editar , deletar ...
}
