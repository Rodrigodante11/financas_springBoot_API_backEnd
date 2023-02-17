package com.rodrigocompany.financas.api.resources;

import com.rodrigocompany.financas.api.dto.AtualizaStatusDTO;
import com.rodrigocompany.financas.api.dto.LancamentoDTO;
import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.enums.StatusLancamento;
import com.rodrigocompany.financas.model.enums.TipoLancamento;
import com.rodrigocompany.financas.model.repository.LancamentoRepository;
import com.rodrigocompany.financas.service.LancamentoService;
import com.rodrigocompany.financas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResources {
    // final : tem que ser obrigatoriamente preenchido pelo construtor
    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;
    private final LancamentoRepository lancamentoRepository;

    @PostMapping()
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO){ //@RequestBody : receber o json e converter para lancamentoDTO
        try{
            Lancamento lancamentoEntidade = converter(lancamentoDTO);
            lancamentoEntidade = lancamentoService.salvar(lancamentoEntidade);
            return new ResponseEntity(lancamentoEntidade, HttpStatus.CREATED);

        }catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    @GetMapping("{id}")
    public ResponseEntity obterLancamentoPorId(@PathVariable("id") Long id){
        return lancamentoService.obterPorId(id)
                .map( lancamneto -> new ResponseEntity(
                        converterParaDTO(lancamneto), HttpStatus.OK
                        )
                ).orElseGet( () -> new ResponseEntity((HttpStatus.NOT_FOUND) ));
    }

    private LancamentoDTO converterParaDTO(Lancamento lancamento){
        return LancamentoDTO.builder()
                .id(lancamento.getId())
                .descricao(lancamento.getDescricao())
                .valor(lancamento.getValor())
                .mes(lancamento.getMes())
                .ano(lancamento.getAno())
                .status(lancamento.getStatus().name())  // name() para converter enum em string
                .tipo(lancamento.getTipo().name()) // name() para converter enum em string
                .usuario(lancamento.getUsuario().getId()).build();
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody LancamentoDTO dto ) {
        return lancamentoService.obterPorId(id).map( entity -> {
            try {
                Lancamento lancamento = converter(dto);
                lancamento.setId(entity.getId());
                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO atualizaStatusDTO){

        return lancamentoService.obterPorId(id).map( entity -> {  //entity == lancamento

            //cast para o enum StatusLancamento
            StatusLancamento statusSelecionado =  StatusLancamento.valueOf(atualizaStatusDTO.getStatus());


            if(statusSelecionado == null){
                return ResponseEntity.badRequest().body("Nao foi possivel Atualizar o satstus do lancamento," +
                        "envie um Status Valido");
            }
            try{
                entity.setStatus(statusSelecionado);
                lancamentoService.atualizar(entity);
                return ResponseEntity.ok(entity);

            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () ->
                new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }

    @DeleteMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity deletar(@PathVariable("id") Long id ){
        return lancamentoService.obterPorId(id).map( entidade -> {
            lancamentoService.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () ->
                new ResponseEntity("Lancamento nao encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }
    // colocando os parametros do metodo buscar desse jeito abaixo , obriga a ter todos os parametros para busca
    // @RequestParam("descricao") String descricao,
    // @RequestParam("mes") Integer mes,
    // @RequestParam("ano") Integer ano,
    // @RequestParam("usuario") Long idUsuario
    @GetMapping
    public ResponseEntity buscar(
            // aparemtros de busca / para buscar por descricao, mes, ano, e tendo o usuario que criou esse Lancamento
            //  required = false : deixar opcional cada parametro da busca e nao obrigatorio
            @RequestParam(value="descricao", required = false) String descricao,
            @RequestParam(value="mes", required = false) Integer mes,
            @RequestParam(value="ano", required = false) Integer ano,
            @RequestParam(value="usuario", required = true ) Long idUsuario // parametro obrigatorio
    ) {
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if(!usuario.isPresent()){
            return ResponseEntity.badRequest().body(
                    "Nao foi possivel realizar a consulta, Usuario nao enocntrado para o ID informado");

            // faz a mesma coisa que o return acima
            // return new ResponseEntity(
            //       "Nao foi possivel realizar a consulta, Usuario nao enocntrado para o ID informado",
            //        HttpStatus.BAD_REQUEST);

        }else{
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentoList= lancamentoService.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentoList);

    }

    private Lancamento converter(LancamentoDTO lancamentoDTO){
        Usuario usuario = usuarioService
                .obterPorId(lancamentoDTO.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuario nao enocntrado para o ID informado."));

        Lancamento lancamento = new Lancamento();
        lancamento.setId(lancamentoDTO.getId());
        lancamento.setDescricao(lancamentoDTO.getDescricao());
        lancamento.setMes(lancamentoDTO.getMes());
        lancamento.setAno(lancamentoDTO.getAno());
        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setUsuario(usuario);

                                                                     //cast para o enum TipoLancamento
        if (lancamentoDTO.getTipo() != null) lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
                                                                    //cast para o enum TipoLancamento
        if (lancamentoDTO.getStatus() !=null) lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));

        return lancamento;
    }


    @GetMapping("/gg")
    public String teste(){
        return "teste gg editado";
    }
}
