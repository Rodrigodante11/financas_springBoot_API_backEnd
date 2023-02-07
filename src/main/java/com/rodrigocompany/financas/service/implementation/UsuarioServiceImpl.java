package com.rodrigocompany.financas.service.implementation;

import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.UsuarioRepository;
import com.rodrigocompany.financas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    //@Autowired
    private UsuarioRepository repository;

    //@Autowired
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUusuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {

    }
}
