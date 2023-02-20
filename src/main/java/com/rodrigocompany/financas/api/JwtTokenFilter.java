package com.rodrigocompany.financas.api;

import com.rodrigocompany.financas.service.JwtService;
import com.rodrigocompany.financas.service.implementation.SecutiryUserDetailsService;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private SecutiryUserDetailsService userDetailsService;

    public JwtTokenFilter( JwtService jwtService,SecutiryUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // "Bearer","eyJhbGciOiJIUzUxMiJ9.eyJ..."

        if(authorization != null && authorization.startsWith("Bearer")) {

            String token = authorization.split(" ")[1];

            boolean isTokenValid = jwtService.isTokenValido(token);

            if(isTokenValid) {
                String login = jwtService.obterLoginUsuario(token);
                UserDetails usuarioAutenticado = userDetailsService.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken user =
                        new UsernamePasswordAuthenticationToken(
                                usuarioAutenticado, null, usuarioAutenticado.getAuthorities());

                user.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );

                SecurityContextHolder.getContext().setAuthentication(user);

            }
        }

        filterChain.doFilter(request, response);
    }
}
