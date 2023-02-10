package com.rodrigocompany.financas.model.repository;

import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.enums.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    // public interface LancamentoRepository extends JpaRepository<Objeto, Tipo da chave primaria>
    // com o JpaRepository os metodos padroes ja estao criados
    // Inserir , consulatr , editar , deletar ...

    // nao foi usado o query Method no metodo abaixo
    @Query( value =
            " SELECT sum(l.valor) FROM Lancamento l " +
            "JOIN l.usuario u " +
            "WHERE u.id =:idUsuario " +
            "AND l.tipo = :tipo " +
                    "GROUP BY u")

    BigDecimal obterSaldoPorTipoLancamentodeUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("tipo") TipoLancamento tipo);
}
