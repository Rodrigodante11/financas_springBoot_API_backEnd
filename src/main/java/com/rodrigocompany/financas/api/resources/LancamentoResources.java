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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentods")
public class LancamentoResources {

    private LancamentoService service;
    private UsuarioService usuarioService;
    private final LancamentoRepository lancamentoRepository;

    public LancamentoResources(LancamentoService service,
                               LancamentoRepository lancamentoRepository){
        this.service = service;
        this.lancamentoRepository = lancamentoRepository;
    }

    @PostMapping()
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO){ //@RequestBody : receber o json e converter para lancamentoDTO


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
