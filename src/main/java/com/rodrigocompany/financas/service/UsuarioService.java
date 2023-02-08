package com.rodrigocompany.financas.service;

import com.rodrigocompany.financas.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email); // verificar se o email ja tem na base de dados

}
