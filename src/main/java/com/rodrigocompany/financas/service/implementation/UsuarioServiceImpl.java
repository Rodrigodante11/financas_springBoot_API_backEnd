package com.rodrigocompany.financas.service.implementation;

import com.rodrigocompany.financas.exception.ErroAutenticacao;
import com.rodrigocompany.financas.exception.RegraNegocioException;
import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.UsuarioRepository;
import com.rodrigocompany.financas.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    //@Autowired
    private UsuarioRepository repository;
    private PasswordEncoder passwordEncoder;

    //@Autowired
    public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario autenticar(String email, String senha) {

        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()){ // se volto vazio quer dizer que nao achou o email
            throw new ErroAutenticacao("Usuario nao encontrado pelo email informado");

        }

        boolean senhaBatem = passwordEncoder.matches(senha, usuario.get().getSenha());

        if(!senhaBatem) { // apos verificar o email faz a verificacao se a senha eh igual
            throw new ErroAutenticacao("Senha invalida");
        }

        return usuario.get();

    }

    private void cripografarSenha(Usuario usuario){

        // criptografando a senha
        String senha = usuario.getSenha();
        String senhaCripto = passwordEncoder.encode(senha);
        usuario.setSenha(senhaCripto);

    }

    @Override
    @Transactional  //aquela logica do faz tudo ou nao faz nada no BD
    public Usuario salvarUsuario(Usuario usuario) {

        validarEmail(usuario.getEmail());
        cripografarSenha(usuario);
        return repository.save(usuario);

    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe){
            throw new RegraNegocioException("Ja existe um usuario cadastrado com este email");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(long id) {
        return repository.findById(id);
    }


}
