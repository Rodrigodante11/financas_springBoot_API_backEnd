package com.rodrigocompany.financas.model.repository;
import com.rodrigocompany.financas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // public interface UsuarioRepository extends JpaRepository<Objeto, Tipo da chave primaria>
    // com o JpaRepository os metodos padroes ja estao criados
    // Inserir , consulatr , editar , deletar ...

    // Querry method
    // optional pois pode existir email ou nao
    // findByEmail eh um querry method (SELECT * FROM USUARIO WHERE EMAIL = EMAIL)
    // ele ja faz isso automaticamente escrevendo a sintaze (findByParametro)
    // Optional<Usuario> findByEmailAndNome(String email, String nome); outro exemplo com 2 parametros
    //Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
