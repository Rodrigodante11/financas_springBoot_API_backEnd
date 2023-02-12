package com.rodrigocompany.financas.api.resources;


import com.rodrigocompany.financas.api.dto.UsuarioDTO;
import com.rodrigocompany.financas.exception.ErroAutenticacao;
import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.LancamentoRepository;
import com.rodrigocompany.financas.service.LancamentoService;
import com.rodrigocompany.financas.service.UsuarioService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
@WebMvcTest(controllers = UsuarioResource.class) // controle que ira ser testado
@AutoConfigureMockMvc
public class UsuarioResourceTest {

    static final String API= "/api/usuario";

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService usuarioService; // classe que nao vou testar apenas amockar
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @MockBean
    LancamentoService lancamentoService;
    @MockBean
    LancamentoRepository lancamentoRepository;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        //Cenario
        String email ="usuario@email.com";
        String senha = "123";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder() // vai representa o json que sera enviado
                .email(email)
                .senha(senha).build();

        Usuario usuario = Usuario.builder() // objeto que sera retornado
                .id(1l)
                .email(email)
                .senha(senha).build();

        // mockito para o metodo autenticar de usuario pois isso ja foi testado em usuarioServiceTest
        Mockito.when(usuarioService.autenticar(email,senha)).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                                                    .post(API.concat("/autenticar")) // QUANDO EU FIZER ESSE POST
                                                    .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                                                    .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                                                    .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isOk()) // ESPERO UM STATUS OK e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void deveDeveRetornaBadReuqestAoObeterErroDeAutenticacao() throws Exception {
        //Cenario
        String email ="usuario@email.com";
        String senha = "123";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder() // vai representa o json que sera enviado
                .email(email)
                .senha(senha).build();


        // mockito para o metodo autenticar de usuario pois isso ja foi testado em usuarioServiceTest
        Mockito.when(usuarioService.autenticar(email,senha)).thenThrow(ErroAutenticacao.class);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API.concat("/autenticar")) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // ESPERO UM STATUS BADREQUEST

    }

    @Test
    public void deveCriarUmNovoUsuario() throws Exception {
        //Cenario
        String email ="usuario@email.com";
        String senha = "123";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder() // vai representa o json que sera enviado
                .email(email)
                .senha(senha).build();

        Usuario usuario = Usuario.builder() // objeto que sera retornado
                .id(1l)
                .email(email)
                .senha(senha).build();

        // salvar Usuario com qualquer usuario enviado pelo mockito entao retorna o usuario criado acima
        Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isCreated()) // ESPERO UM STATUS OK e abaixo o json com cahve Valor
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }

    @Test
    public void deveRetornarUBadRequestAoTentarCriarUmNovoUsuarioInvalido() throws Exception {
        //Cenario
        String email ="usuario@email.com";
        String senha = "123";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder() // vai representa o json que sera enviado
                .email(email)
                .senha(senha).build();

        Usuario usuario = Usuario.builder() // objeto que sera retornado
                .id(1l)
                .email(email)
                .senha(senha).build();

        // salvar Usuario com qualquer usuario enviado pelo mockito entao retorna o usuario criado acima
        Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);

        String json = new ObjectMapper().writeValueAsString(usuarioDTO); // cast para Json

        //execucao e verificacao SERA TESTANDO UM METODO POST
        MockHttpServletRequestBuilder request= MockMvcRequestBuilders
                .post(API) // QUANDO EU FIZER ESSE POST
                .accept(JSON) // VOU ACEITAR/RECEBER CONTEUDO JSON
                .contentType(JSON) // E ESTOU ENVIADO OBJETO DO TIPO  JSON
                .content(json); // E objetoJson Enviado

        mvc.perform(request) // PERDORM == EXECUTA A REQUISICAO
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // ESPERO UM STATUS OK e abaixo o json com cahve Valor

    }
}
