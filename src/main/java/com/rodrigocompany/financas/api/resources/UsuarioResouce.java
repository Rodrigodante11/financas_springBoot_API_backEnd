package com.rodrigocompany.financas.api.resources;

import com.rodrigocompany.financas.api.dto.UsuarioDTO;
import com.rodrigocompany.financas.exception.ErroAutenticacao;
import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResouce {

   private UsuarioService usuarioService ;

   public UsuarioResouce(UsuarioService usuarioService) {
      this.usuarioService = usuarioService;
   }

   @PostMapping("/autenticar")
   public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){
      try{
         Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());

         // return new ResponseEntity(body, status);
         return ResponseEntity.ok(usuarioAutenticado);

      }catch(ErroAutenticacao e){
         return ResponseEntity.badRequest().body(e.getMessage());

      }
   }

   @PostMapping
   public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){// @RequestBody: os dados json vindo do request sera o objeto usuarioDTO tem todos os atributos do objeto

      Usuario usuario = Usuario.builder()  // tranformando o DTO em um objeto usuario
              .nome(usuarioDTO.getNome())
              .email(usuarioDTO.getEmail())
              .senha(usuarioDTO.getSenha()).build();

      try{
         Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);

         // return new ResponseEntity(body, status);
         return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
      }catch (RegraNegocioException e){ // se o email ja tiver no BD vai lancar um RegraNegocioException
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }
}
