package com.rodrigocompany.financas.model.repository;

import com.rodrigocompany.financas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // public interface UsuarioRepository extends JpaRepository<Objeto, Tipo da chave primaria>
    // com o JpaRepository os metodos padroes ja estao criados
    // Inserir , consulatr , editar , deletar ...
}
