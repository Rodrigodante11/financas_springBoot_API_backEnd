package com.rodrigocompany.financas.model.repository;

import static org.assertj.core.api.Assertions.*;
import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.enums.StatusLancamento;
import com.rodrigocompany.financas.model.enums.TipoLancamento;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //o @DataJpaTest cria um instância propria do BD , com essa anotation dessa linha ele nao sobrescreve a configuracao e vai usuar o (aplication-test.properties)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Lancamento criarLancamento(){
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("lancamento Test")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }
    public Lancamento criarEPersistirUmLancamento(){
        Lancamento lancamento =criarLancamento();
        entityManager.persist(lancamento);
        return lancamento;
    }

    @Test
    public void deveSalvarUmlancamento(){
        //Cenario
        Lancamento lancamento = criarLancamento();

        //Acao/execucao
        lancamento =  repository.save(lancamento);


        //verificacao
        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deletarUmLancamento(){
        //Cenario
        Lancamento lancamento = criarEPersistirUmLancamento();

        //Acao/execucao
        //entityManager.find(entityClass, primaryKey);
        lancamento = entityManager.find(Lancamento.class, lancamento.getId()); // Achando o lancamento criado na base de dados

        repository.delete(lancamento);

        //Procurando Novamente se existe o lancamento apos deletado
        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        //verificacao
        assertThat(lancamentoInexistente).isNull(); // se foi deletado nao vai ter nada em lancamentoInexistente

    }
    @Test
    public void deveAtualizarUmLancamento(){
        //Cenario
        Lancamento lancamento = criarEPersistirUmLancamento();

        //Acao/execucao  Modificando os lancamentos
        lancamento.setAno(2020);
        lancamento.setMes(10);
        lancamento.setDescricao("lancamento Test Editado");
        lancamento.setValor(BigDecimal.valueOf(105));
        lancamento.setTipo(TipoLancamento.DESPESA);
        lancamento.setStatus(StatusLancamento.EFETIVADO);
        repository.save(lancamento);

        // Procurando o lancamento na base de dados
        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId()); // Achando o lancamento criado na base de dados

        //verificacao
        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
        assertThat(lancamentoAtualizado.getMes()).isEqualTo(10);
        assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("lancamento Test Editado");
//      assertThat(lancamentoAtualizado.getDescricao()).isEqualTo(BigDecimal.valueOf(105));
        assertThat(lancamentoAtualizado.getTipo()).isEqualTo(TipoLancamento.DESPESA);
        assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.EFETIVADO);

    }
    @Test
    public void deveBuscarUmLancamentoPorId(){
        //Cenario
        Lancamento lancamento = criarEPersistirUmLancamento();


        // Procurando o lancamento na base de dados
        Lancamento result = entityManager.find(Lancamento.class, lancamento.getId()); // Achando o lancamento criado na base de dados

        //verificacao
        assertThat(result.getId()).isNotNull();
    }
}

//import org.assertj.core.api.Assertions;
//Assertions.assertThat(lancamento.getId()).isNotNull();

//import static org.assertj.core.api.Assertions.*;
//assertThat(lancamento.getId()).isNotNull();
