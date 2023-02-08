package com.rodrigocompany.financas.service;

import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
class UsuarioServiceTests {

	public static String EMAIL = "usuario@email.com";
	public static String NOME = "usuario";

	@Autowired
	UsuarioService service;

	@Autowired
	UsuarioRepository repository;

	//@Test(expected = Test.None.class) Usado no Junit 4 para excessoes  no caso nao espera nenhuma excessao
	@Test
	public void deveValidarEmail() {

		Assertions.assertDoesNotThrow(() -> {

			// cenario
			repository.deleteAll();

			// acao
			service.validarEmail(EMAIL);
		});
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastratado(){
		Assertions.assertThrows(RegraNegocioException.class, () -> {

			// cenario
			Usuario usuario = Usuario.builder().nome(NOME).email(EMAIL).build();
			repository.save(usuario);

			// acao
			service.validarEmail(EMAIL);
		});
	}

	@Test
	void contextLoads() {
	}

}
