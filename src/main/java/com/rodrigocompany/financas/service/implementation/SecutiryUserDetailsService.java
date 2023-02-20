package com.rodrigocompany.financas.service.implementation;

import com.rodrigocompany.financas.model.entity.Usuario;
import com.rodrigocompany.financas.model.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecutiryUserDetailsService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;
    public SecutiryUserDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuarioEncontrado =
                usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException ("Email nao cadastrada"));

        User user = (User) User.builder()
                            .username(usuarioEncontrado.getEmail())
                            .password(usuarioEncontrado.getSenha())
                            .roles("USER")
                            .build();

        return user;
    }
}
