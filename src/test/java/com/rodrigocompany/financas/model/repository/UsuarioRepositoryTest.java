package com.rodrigocompany.financas.model.repository;

import com.rodrigocompany.financas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ExtendWith( SpringExtension.class)
public class UsuarioRepositoryTest {


    @Autowired // injetar uma intancia
    UsuarioRepository repository;

    @Test
    public void deveVerificarAExistenciadeUmEmail(){

        //Cenario
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        repository.save(usuario);

        //Acao/execucao
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(result).isTrue();

    }
}


//@RunWith(SpringRunner.class) do Junit 4 foi subistituido pelo @ExtendWith( SpringExtension.class)
//import org.junit.runner.RunWith
//import org.springframework.test.context.junit4.SpringRunner;
//(import org.junit.Test) para (import org.junit.jupiter.api.Test);
