package com.rodrigocompany.financas.api.resources;

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
@RequestMapping("/api/lancamentods")
@RequiredArgsConstructor
public class LancamentoResources {
    // final : tem que ser obrigatoriamente preenchido pelo construtor
    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

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
    @PutMapping("{id}") // para atualizar @PutMapping("{id}") com o ID do Objeto a ser atualizado
    public ResponseEntity atualizar(@PathVariable("id") Long id , @RequestBody LancamentoDTO lancamentoDTO){

        return lancamentoService.obterPorId(id).map( entity -> {

            try{
                Lancamento lancamentoEntidade = converter(lancamentoDTO);
                lancamentoDTO.setId(entity.getId());
                lancamentoService.atualizar(lancamentoEntidade);
                return ResponseEntity.ok(lancamentoEntidade);
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
        if(usuario.isPresent()){
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

        Lancamento lancamento = Lancamento.builder()
                .id(lancamentoDTO.getId())
                .descricao(lancamentoDTO.getDescricao())
                .mes(lancamentoDTO.getMes())
                .ano(lancamentoDTO.getAno())
                .valor(lancamentoDTO.getValor())
                .usuario(usuario)
                .tipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()))
                .status(StatusLancamento.valueOf(lancamentoDTO.getStatus()))
                .build();

        return lancamento;
    }
}
