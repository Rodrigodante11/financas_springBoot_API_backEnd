![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)

<h1 aligh="center"> Financas com Spring Boot/React e PostgreeSQL <h2>
<strong>Financas com Java 17</strong>
  
Para baixar o projeto:

```
[https://github.com/Rodrigodante11/Ponto_Venda_Java_Web_Spring.git](https://github.com/Rodrigodante11/financas_springboot_react.git)
```

  Dependencias do projeto
 - Lombok
- Spring Web
- Spring Data JPA
- Postgree Driver
 
 ### :mag_right: Descricao de algumas Dependencias Usadas:
- Lombok = Atraves do Lombok foi possivel subistituir os Métodos getters and setters das classes do projeto apenas com uma annotation acima da classe (@Getter & @Setter)



-------
 - @Entity: Essa anotação  é utilizada para informar que uma classe também é uma entidade. A partir disso, a JPA estabelecerá a ligação entre a entidade e uma tabela de mesmo nome no banco de dados, onde os dados de objetos desse tipo poderão ser persistidos.
- @Id: Definindo com atributo sera o ID da tabela

 ### :mag_right: Pra test foi usado o Junit 5 , unsando mock e spy quando necessario 
 ### Segue abaixo a principal diferenca entre Mock e Spy
 
O Mock é uma classe cuja implementação é vazia e vc precisa ditar qual será o comportamento dos métodos, ou seja, TODOS os métodos são vazios e retornam valores padrões. Já o Spy é um mock cuja implementação é a original da classe, os métodos não são vazios e chamam a implementação original.

Caso a classe seja apenas uma dependência da classe que vc está testando, então use os mocks, assim vc consegue simular o comportamento dos métodos destas classes. Caso vc precise mockar um método da própria classe que vc está testando, então essa classe precisa ser um Spy , pois como expliquei acima, o Spy chama os métodos originais da classe, porém é possível mockar os métodos dela que são chamados dentro do contexto de teste que vc está realizando.
