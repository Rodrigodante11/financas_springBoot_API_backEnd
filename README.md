![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)
<h1 aligh="center"> Financas FrontEnd com React e BackEnd SpringBootAPI e Persistencia dos dados com e PostgreeSQL<h2>
<strong>Repositorio do BackEnd SpringBootAPI</strong> </br>
<strong>Java 17 </strong> </br>
<strong>Spring-boot 2.7.8 </strong> </br>
<strong>Junit 5</strong></br></br>
  
Para baixar o projeto BACKEND SpringBootAPI:

```
https://github.com/Rodrigodante11/Ponto_Venda_Java_Web_Spring.git
```

Para baixar o projeto FRONTEND React.js:

```
https://github.com/Rodrigodante11/financas_React_frontEnd.git
```

 Dependencias do projeto
- Lombok
- Spring Web
- Spring Data JPA
- Postgree Driver
- devtools
 

 ### :mag_right: Para test unitarios foi usado o Junit 5 , usando mock e spy 
 ### Segue abaixo a principal diferenca entre Mock e Spy
 
O Mock é uma classe cuja implementação é vazia e vc precisa ditar qual será o comportamento dos métodos, ou seja, TODOS os métodos são vazios e retornam valores padrões. Já o Spy é um mock cuja implementação é a original da classe, os métodos não são vazios e chamam a implementação original.

Caso a classe seja apenas uma dependência da classe que vc está testando, então use os mocks, assim vc consegue simular o comportamento dos métodos destas classes. Caso vc precise mockar um método da própria classe que vc está testando, então essa classe precisa ser um Spy , pois como expliquei acima, o Spy chama os métodos originais da classe, porém é possível mockar os métodos dela que são chamados dentro do contexto de teste que vc está realizando.
