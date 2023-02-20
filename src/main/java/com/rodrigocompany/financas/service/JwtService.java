package com.rodrigocompany.financas.service;

import com.rodrigocompany.financas.model.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {

    String gerarToken(Usuario usuario);

    // Claims seu as informacoes que tem no tokem
    Claims obterClaims(String token) throws ExpiredJwtException;

    boolean isTokenValido(String token);

    String obterLoginUsuario(String token);

}
