// MUITO IMPORTANTEEEEE SE ESTOU TESTANDO OS METODOS DA CLASSE SERVICE
// OS METODOS DE REPOSTORY USADOS DEVE SER MOCKADOS POIS NAO QUERO TESTAR O REPOSITRY DUAS VEZES
// JA QUE OS TESTES DE REPOSITORY JA ESTAO FEITOS E APROVADO

package com.rodrigocompany.financas.service;

import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.enums.StatusLancamento;
import com.rodrigocompany.financas.model.enums.TipoLancamento;
import com.rodrigocompany.financas.model.repository.LancamentoRepository;
import com.rodrigocompany.financas.service.implementation.LancamentoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl lancamentoServiceImpl; // classe que sera testada

    @MockBean
    LancamentoRepository lancamentoRepository; // classe que nao queremos testar e sera mockada

    @Test
    public void deveSalvarUmLancamento(){
        Lancamento lancamentoASalvar = criarLancamento();

        //verificacao
        Assertions.assertDoesNotThrow(() -> {


            // nao quero testar o metodo (validar)
            Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoASalvar); // mesma classe que estou tentando

            Lancamento lancamentoSalvo = criarLancamento();
            lancamentoSalvo.setId(1l);
            // nao quero testar o metodo (salvar) nem a classe Repository
            Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo); // classe que nao quero testar
            // quando chamar o lancamentoRepository.save com o objeto(lancamentoASalvar) retorn o objeto(lancamentoSalvo)

            //o que realmente quero testar
            Lancamento lancamentoSalvoImp = lancamentoServiceImpl.salvar(lancamentoASalvar);

            //verificacao
            Assertions.assertNotNull(lancamentoSalvoImp);
            Assertions.assertEquals(lancamentoSalvoImp.getId(), lancamentoSalvo.getId());
            Assertions.assertEquals(lancamentoSalvoImp.getAno(), lancamentoSalvo.getAno());
            Assertions.assertEquals(lancamentoSalvoImp.getDescricao(), lancamentoSalvo.getDescricao());
            Assertions.assertEquals(lancamentoSalvoImp.getValor(), lancamentoSalvo.getValor());
            Assertions.assertEquals(lancamentoSalvoImp.getTipo(), lancamentoSalvo.getTipo());
            Assertions.assertEquals(lancamentoSalvoImp.getStatus(), StatusLancamento.PENDENTE );

        });
    }

    @Test
    public void deveAtualizarUmLancamento() { // so precisar conferir se checou no metodo save do repository pois o
                                              // save repository ja foi testando que atualiza quando manda ID
        Lancamento lancamentoSalvo = criarLancamento();
        lancamentoSalvo.setId(1l);

        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        // nao quero testar o metodo (validar)
        Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoSalvo); // mesma classe que estou tentando

        //o que realmente quero testar
        lancamentoServiceImpl.atualizar(lancamentoSalvo);


        Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);

    }
    @Test
    public void deveAtualizarUmErroAoTentarUmLancamentoQueNaoFoiSaldo() {
        // save repository ja foi testando que atualiza quando manda ID

        Assertions.assertThrows(NullPointerException.class, () -> { // testando a excessao do (Objects.requireNonNull(lancamento.getId()))

            Lancamento lancamentoSalvo = criarLancamento();

            //lancamentoSalvo.setId(1l); // se nao  foi salvo entao nao tem ID

            //o que realmente quero testar
            lancamentoServiceImpl.atualizar(lancamentoSalvo);

            //Nunca chegue no metodo save do repository pois precisa lanca uma excessao antes
            Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoSalvo);

        });
    }

    @Test
    public void deveDelearUmLancamento(){ // apenas testo se chado ate o metodo deletar do repository , pois o delete
                                          // ja foi testado
        Lancamento lancamento = criarLancamento();
        lancamento.setId(1l);

        lancamentoServiceImpl.deletar(lancamento);

        //Verificacao se chego no metodo delete do repository || nao testo o depois de chamar pois isso ja foi feito
        // no teste delete de repository lancamento
        Mockito.verify(lancamentoRepository).delete(lancamento);
    }
    @Test
    public void deveLncarErroAoTentarDelearUmLancamentoNaoSalvo(){

        Assertions.assertThrows(NullPointerException.class, () -> { // testando a excessao do (Objects.requireNonNull(lancamento.getId()))

            Lancamento lancamento = criarLancamento();
            // lancamento.setId(1l);  nao salvo == nao tem ID

            lancamentoServiceImpl.deletar(lancamento);

            // Ja que vai lancar excessao nao pode chegar a deletar
            Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamento);
        });
    }
    @Test
    public void deveBuscarLancamento(){

        Lancamento lancamento = criarLancamento();
        lancamento.setId(1l);

        List<Lancamento> lista = Arrays.asList(lancamento); // cast para list

        // quando chamar o metodo(findAll()) retorna a lista
        // mockando pois isso eh teste do repository de lancamento
        Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execucao
        List<Lancamento> resultado = lancamentoServiceImpl.buscar(lancamento);

        //verificacao
        Assertions.assertNotNull(resultado);
        Assertions.assertArrayEquals(resultado.toArray(), lista.toArray());
        Assertions.assertEquals(resultado.size(), 1);

    }

    @Test
    public void deveAtualizarOsStatusdeUmLancamento(){
        Lancamento lancamento = criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;

        // so preciso verificar se vai chamar o metodo atualizar
        // mas nao precisa chamar o metodo de verdade pois esse medodo (atualizar) eh outro TESTE
        // agora estou testando o (atualizarStatus)
        Mockito.doReturn(lancamento).when(lancamentoServiceImpl).atualizar(lancamento);


        //execucao testando o metodo (atualizarStatus)
        lancamentoServiceImpl.atualizarStatus(lancamento,novoStatus);

        //verificacao se chamou o metodo atualizar, se apenas chamou
        Mockito.verify(lancamentoServiceImpl).atualizar(lancamento);
        Assertions.assertEquals(lancamento.getStatus(), novoStatus);
    }

    @Test
    public void deveObterUmLancamentoPorId(){
        //cenario
        Lancamento lancamento = criarLancamento();
        Long id = 1l;
        lancamento.setId(id);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o lancamento direto pois nao estou testando esse metodo
        Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));

        //execucao
        Optional<Lancamento> resultado = lancamentoServiceImpl.obterPorId(id);



        //verificacao
        Assertions.assertTrue(resultado.isPresent());

        //verificacao se chamou o metodo findById, se apenas chamou
        Mockito.verify(lancamentoRepository).findById(id);
    }

    @Test
    public void deveRetornarVazioQuandoUmLancamentoNaoExiste(){
        //cenario
        Lancamento lancamento = criarLancamento();
        Long id = 1l;
        lancamento.setId(id);

        // nao quero testar o metodo (findById)
        // apenas disse quando ele foi chamado para retornar o lancamento direto pois nao estou testando esse metodo
        Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());

        //execucao
        Optional<Lancamento> resultado = lancamentoServiceImpl.obterPorId(id);


        //verificacao
        Assertions.assertFalse(resultado.isPresent());

        //verificacao se chamou o metodo findById, se apenas chamou
        Mockito.verify(lancamentoRepository).findById(id);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidao(){

        Assertions.assertThrows(RegraNegocioException.class, () -> {

            Lancamento lancamentoASalvar = criarLancamento();
            Mockito.doThrow(RegraNegocioException.class).when(lancamentoServiceImpl).validar(lancamentoASalvar);

            lancamentoServiceImpl.salvar(lancamentoASalvar);

            // Ja que vai lancar a excessao espero que
            // nunca chame repository.save(usuario)
            Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
        });
    }


    // OS TESTES ABAIXO PDOERIA TER FEITOS EM APENAS UM TESTE CASO QUEIRA
    // APENAS SETANDO O LANCAMENTO A CADA TESTE DE EXCESAO
   @Test
   public void ErroDeValidaoPorDescricaoNula(){
       Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
           Lancamento lancamentoASalvar = criarLancamento();
           lancamentoASalvar.setDescricao(null);

           lancamentoServiceImpl.validar(lancamentoASalvar);

       });

       Assertions.assertEquals("Informe uma Descricao Valida", exception.getMessage());

    }
    @Test
    public void ErroDeValidaoPorDescricaoVazio(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();
            lancamentoASalvar.setDescricao("");

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe uma Descricao Valida", exception.getMessage());

    }

    @Test
    public void ErroDeValidaoPorMesVazio(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();
            lancamentoASalvar.setMes(null);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Mes Valida", exception.getMessage());

    }
    @Test
    public void ErroDeValidaoPorMesMaiorqueTreze(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();
            lancamentoASalvar.setMes(13);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Mes Valida", exception.getMessage());

    }

    @Test
    public void ErroDeValidaoPorAnoVazio(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();
            lancamentoASalvar.setAno(null);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Ano Valida", exception.getMessage());

    }

    @Test
    public void ErroDeValidaoPorAnoDiferenteDeQuatro(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();
            lancamentoASalvar.setAno(20233);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Ano Valida", exception.getMessage());

    }

    @Test
    public void ErroDeValidaoPorUsuarioVazio(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();
            lancamentoASalvar.setUsuario(null);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Usuario Valido", exception.getMessage());

    }
    @Test
    public void ErroDeValidaoPorUsuarioIDVazio(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();

            Usuario usuariosemId = criarUsuario(null);
            lancamentoASalvar.setUsuario(usuariosemId);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Usuario Valido", exception.getMessage());

    }

    @Test
    public void ErroDeValidaoPorValorNulo(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();

            lancamentoASalvar.setValor(null);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Valor Valido", exception.getMessage());

    }
    @Test
    public void ErroDeValidaoPorValorZERO(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();

            lancamentoASalvar.setValor(BigDecimal.ZERO);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Valor Valido", exception.getMessage());

    }
    @Test
    public void ErroDeValidaoPorTipoNulo(){
        Throwable exception = Assertions.assertThrows(RegraNegocioException.class, () -> {
            Lancamento lancamentoASalvar = criarLancamento();

            lancamentoASalvar.setTipo(null);

            lancamentoServiceImpl.validar(lancamentoASalvar);

        });

        Assertions.assertEquals("Informe um Tipo de Lancamento", exception.getMessage());

    }


    public static Usuario criarUsuario(Long id) {
        return Usuario.builder()
                .id(id)
                .nome("NOME")
                .email("EMAIL")
                .senha("SENHA").build();
    }
    public static Lancamento criarLancamento(){

        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .usuario(criarUsuario(1l))
                .descricao("lancamento Test")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }
}
