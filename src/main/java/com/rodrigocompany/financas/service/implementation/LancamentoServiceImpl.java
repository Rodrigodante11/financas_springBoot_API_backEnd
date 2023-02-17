package com.rodrigocompany.financas.service.implementation;

import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.enums.StatusLancamento;
import com.rodrigocompany.financas.model.enums.TipoLancamento;
import com.rodrigocompany.financas.model.repository.LancamentoRepository;
import com.rodrigocompany.financas.service.LancamentoService;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository lancamentoRepository;

    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository){
        this.lancamentoRepository= lancamentoRepository;
    }



    @Override
    @Transactional //aquela logica do faz tudo ou nao faz nada no BD
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional //aquela logica do faz tudo ou nao faz nada no BD
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId()); // salvar e atualiza usa o mesmo metodo save() o que muda eh o envio do ID
        validar(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional //aquela logica do faz tudo ou nao faz nada no BD
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        lancamentoRepository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true) //logica faz tudo porem com apenas leitura
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {

        Example<Lancamento> example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()   // ignorar maisculo e minusculo
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

                        // nao precisa ter toda a inpofrmcao do nome, email
                        // para procurar exemplo(rod) iria achar (rodrigo)

                        //.withStringMatcher(ExampleMatcher.StringMatcher.STARTING)); // acha todas com Inicio (rod)
                        //.withStringMatcher(ExampleMatcher.StringMatcher.ENDING)); // acha todas com Fim (rod)
                        //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT)); // so acha (rodrigo) se escrever o nome inteiro

        return lancamentoRepository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) { // algumas obrigacoes como espaco, descricao em branco

        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Informe uma Descricao Valida");

        }
        if(lancamento.getMes() == null ||lancamento.getMes()<1 || lancamento.getMes() >12 ){
            throw new RegraNegocioException("Informe um Mes Valida");

        }
        if(lancamento.getAno() == null || lancamento.getAno().toString() .length() != 4){
            throw new RegraNegocioException("Informe um Ano Valida");

        }
        if(lancamento.getUsuario() ==null || lancamento.getUsuario().getId() == null){
            throw new RegraNegocioException("Informe um Usuario Valido");

        }
        // lancamento.getValor() < 1 nao foi usado pois eh BigDecimal mas abaixo eh equivalente
        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1){
            throw new RegraNegocioException("Informe um Valor Valido");

        }if(lancamento.getTipo() == null){
            throw new RegraNegocioException("Informe um Tipo de Lancamento");

        }
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {

        return lancamentoRepository.findById(id);
    }
    @Override
    @Transactional(readOnly = true) //logica faz tudo porem com apenas leitura
    public BigDecimal obterSaldoPorUsuario(Long id){
        // TipoLancamento.RECEITA.name() == Tranforma o RECEITA em String
        // Busca todos os do TIPO RECEITA e Status EFETIVADO com o ID passado
        BigDecimal receitas = lancamentoRepository.obterSaldoPorTipoLancamentodeUsuarioEStatus(id,
                TipoLancamento.RECEITA, StatusLancamento.EFETIVADO); //

        // Busca todos os do TIPO despesa e Status EFETIVADO com o ID passado
        BigDecimal despesas = lancamentoRepository.obterSaldoPorTipoLancamentodeUsuarioEStatus(id,
                TipoLancamento.DESPESA, StatusLancamento.EFETIVADO);

        if(receitas == null){
            receitas= BigDecimal.ZERO;
        }if(despesas == null){
            despesas= BigDecimal.ZERO;
        }

        return receitas.subtract(despesas);
    }
}
