package com.rodrigocompany.financas.model.repository;

import com.rodrigocompany.financas.model.entity.Lancamento;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.enums.StatusLancamento;
import com.rodrigocompany.financas.model.enums.TipoLancamento;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;


@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memória para teste e nao o oficial
@DataJpaTest // Cria uma instância do banco de dados em memória e ao finalizar os testes ela deleta da memória
@AutoConfigureTestDatabase(replace = Replace.NONE) //o @DataJpaTest cria uma instância propria do (BD) , com essa anotation dessa linha ele nao sobrescreve a configuracao e vai usuar o (aplication-test.properties)
public class UsuarioRepositoryTest {

    public static String EMAIL = "usuario@email.com";
    public static String NOME = "usuario";
    public static String SENHA = "senha";

    @Autowired // injetar uma intancia
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Usuario criarUsuario(){
        return Usuario.builder().nome(NOME).email(EMAIL).build();
    }

    @Test
    public void deveVerificarAExistenciadeUmEmail(){

        //Cenario
        Usuario usuario = criarUsuario();

        //  repository.save(usuario); foi refeito pelo entityManager
        entityManager.persist(usuario);  // para usar o persist(Objeto) nao pode ter ID se nao gera excessao

        //Acao/execucao
        boolean result = repository.existsByEmail(EMAIL);

        //verificacao
        Assertions.assertThat(result).isTrue();

    }
    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastratadoComOEmail(){

        // repository.deleteAll(); nao precisa usar mais gracas ao @DataJpaTest

        //Acao/execucao
        boolean result = repository.existsByEmail(EMAIL);

        //verificacao
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBasedeDados(){
        //Cenario
        Usuario usuario = criarUsuario();

        //Acao/execucao
        Usuario usuarioSalvo = repository.save(usuario);

        //verificacao
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull(); // se existir um ID ja quer dizer que foi salvo
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){

        //Cenario
        Usuario usuario = criarUsuario();
        //  repository.save(usuario); foi refeito pelo entityManager
        entityManager.persist(usuario); // para usar o persist(Objeto) nao pode ter ID se nao gera excessao


        //Acao/execucao
        Optional<Usuario> result = repository.findByEmail(EMAIL);


        //verificacao
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEqualQuandoNaoExisteNaBase(){
        //Cenario
        Usuario usuario = criarUsuario();
        //  repository.save(usuario); foi refeito pelo entityManager
        entityManager.persist(usuario); // para usar o persist(Objeto) nao pode ter ID se nao gera excessao

        //Acao/execucao
        Optional<Usuario> result = repository.findByEmail(EMAIL);

        //verificacao
        Assertions.assertThat(result.isPresent()).isFalse();
    }
}


//@RunWith(SpringRunner.class) do Junit 4 foi subistituido pelo @ExtendWith( SpringExtension.class)
//import org.junit.runner.RunWith
//import org.springframework.test.context.junit4.SpringRunner;
//(import org.junit.Test) para (import org.junit.jupiter.api.Test);
