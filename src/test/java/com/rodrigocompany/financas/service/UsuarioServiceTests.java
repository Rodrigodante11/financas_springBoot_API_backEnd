package com.rodrigocompany.financas.service;

import com.rodrigocompany.financas.exception.ErroAutenticacao;
import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.UsuarioRepository;
import com.rodrigocompany.financas.service.implementation.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
public class UsuarioServiceTests {

	public static String NOME = "usuario";
	public static String EMAIL = "usuario@email.com";
	public static String SENHA = "senha";

	// UsuarioRepository repository; == intancia Original
	// UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class); // instancia fake/mockada

	UsuarioService service;
	@MockBean
	UsuarioRepository repository;

	//@Before() // usado no Junit 5 spring boot -2.2
	@BeforeEach
	public void setUp(){
		// A linha abaixo foi tirada pois o annotation @MockBean faz a mesma coisa
		//repository= Mockito.mock(UsuarioRepository.class); // criando um instancia Mock do UsuarioRepository

		service = new UsuarioServiceImpl(repository); // falando pro service usar a instancia Mock de UsuarioRepository (Muito importante essa parte se nao eh um Mock
	}

	//@Test(expected = Test.None.class) Usado no Junit 4 para excessoes / nao espera nenhuma excessao
	@Test
	public void deveValidarEmail() {

		//verificacao
		Assertions.assertDoesNotThrow(() -> {

			// cenario
			//repository.existsByEmail(email);
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			// La em UsuarioService implementation no metodo validarEmail() tem um existsByEmail()
			// entao estou dizendo a ele quando tiver um existsByEmail no metodo usado abaixo validarEmail()
			// criar um instancia FAKE que retorna "true" para qualquer String
			// Pois nesse caso eu nao quero testes o existsByEmail()
			// Quero testar o retorno do validarEmail()

			// acao
			service.validarEmail(EMAIL);
		});
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastratado(){

		//verificacao
		Assertions.assertThrows(RegraNegocioException.class, () -> {

			// cenario
			// Usuario usuario = Usuario.builder().nome(NOME).email(EMAIL).build();
			// repository.save(usuario);

			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			// La em UsuarioService implementation no metodo validarEmail() tem um existsByEmail()
			// entao estou dizendo-lhe quando tiver um existsByEmail no metodo usado abaixo validarEmail()
			// criar uma instância FAKE que retorna "true" para qualquer String
			// Pois, nesse caso eu nao quero testes o existsByEmail()
			// Quero testar o retorno do validarEmail()


			// acao
			service.validarEmail(EMAIL);
		});
	}

	@Test
	public void deveAtenticarUmUsuarioComSucesso(){
		//verificacao
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).id(1L).build();
			Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

			// acao
			service.autenticar(EMAIL,SENHA);
		});
	}

	@Test
	public void emailErradodeAutenticacao(){
		//verificacao
		Throwable exception = Assertions.assertThrows(ErroAutenticacao.class, () -> {
			// cenario
			Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).id(1L).build();
			Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

			// acao
			service.autenticar("emailerrado@email.com",SENHA);
		});

		Assertions.assertEquals("Usuario nao encontrado pelo email informado", exception.getMessage());

	}

	@Test
	public void senhaErradodeAutenticacao(){
		//verificacao
		Throwable exception = Assertions.assertThrows(ErroAutenticacao.class, () -> {
			// cenario
			Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).id(1L).build();
			Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

			// acao
			service.autenticar(EMAIL,"senhaErrada");
		});

		Assertions.assertEquals("Senha invalida", exception.getMessage());

	}


}
