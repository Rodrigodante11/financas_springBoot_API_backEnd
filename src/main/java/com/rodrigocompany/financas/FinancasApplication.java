package com.rodrigocompany.financas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc // aceitar requisicao para consumir a API
public class FinancasApplication implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/**") //url das API que podem ser consultada
				.allowedMethods("GET","POST","PUT","DELETE", "OPTIONS"); //
	}
	public static void main(String[] args) {
		SpringApplication.run(FinancasApplication.class, args);
	}

}
// "email": "usuariotestNovoaa@email.com",
//    "senha": "senhatestNovo"