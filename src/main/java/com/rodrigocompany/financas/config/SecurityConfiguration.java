package com.rodrigocompany.financas.config;

import com.rodrigocompany.financas.api.JwtTokenFilter;
import com.rodrigocompany.financas.service.JwtService;
import com.rodrigocompany.financas.service.implementation.JwtServiceImp;
import com.rodrigocompany.financas.service.implementation.SecutiryUserDetailsService;
import org.springframework.web.filter.CorsFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import javax.servlet.FilterRegistration;
import javax.sql.DataSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecutiryUserDetailsService userDetailsService;

    @Autowired
    private JwtService  jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtService, userDetailsService);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                        .antMatchers(HttpMethod.POST,"/api/usuario/autenticar").permitAll()  // nao esta funcionando entao se criou a funcao acima (webSecurityCustomizer)
                        .anyRequest().authenticated()
        .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
               ; // para acesso a API pelo front(nesse caso sem seguranca pois a seguranca ja esta toda na API)




        return http.build();
    }
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){

        List<String> all = Arrays.asList("*");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(all);
        config.setAllowedOriginPatterns(all);
        config.setAllowedHeaders(all);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        CorsFilter corFilter = new CorsFilter(source);

        FilterRegistrationBean<CorsFilter> filter =
                new FilterRegistrationBean<CorsFilter>(corFilter);
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }


}
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers().antMatchers("/api/usuario/autenticar", "/api/usuario");
//    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {  // permitindo essas urls sem autenticacao
//        return (web) -> web.ignoring().antMatchers("/api/usuario/autenticar", "/api/usuario");
//    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers().antMatchers("/api/usuario/autenticar", "/api/usuario");
//    }


//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        PasswordEncoder encoder= new BCryptPasswordEncoder(); // algoritimo de autenticacao para criptoghrafica sempre gera um hash diferente
//        // diferente do MD5 que sempre gera o mesmo Hash
//
//        return encoder;
//    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) { // usar usuario e senha em memoria
//        String senhaCodificada = passwordEncoder.encode("qwe123Rodrigo");
//        String senhaCodificadaAdimin = passwordEncoder.encode("qwe123Admin");
//
//        UserDetails user = User.withUsername("userRodrigo")
//                .password(senhaCodificada)
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withUsername("adminRodrigo")
//                .password(senhaCodificadaAdimin)
//                .roles("USER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }