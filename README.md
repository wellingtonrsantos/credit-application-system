<h1>credit-application-system</h1>
<p align="center">API Rest para um Sistema de Analise de Solicitação de Crédito</p>
<p align="center">
     <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v17-blue.svg" />
    </a>
    <a alt="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-v1.9.21-purple.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v3.2.1-brightgreen.svg" />
    </a>
    <a alt="Gradle">
        <img src="https://img.shields.io/badge/Gradle-v7.6-lightgreen.svg" />
    </a>
    <a alt="H2 ">
        <img src="https://img.shields.io/badge/H2-v2.1.214-darkblue.svg" />
    </a>
    <a alt="Flyway">
        <img src="https://img.shields.io/badge/Flyway-v9.5.1-red.svg">
    </a>
</p>

<h3>Descrição do Projeto</h3>
<p><a href="https://gist.github.com/cami-la/560b455b901778391abd2c9edea81286">https://gist.github.com/cami-la/560b455b901778391abd2c9edea81286</a></p>
<p>Esse projeto foi desenvolvido atráves do curso "Criando uma Api Rest com Kotlin e Persistência de Dados" da <a href="https://web.dio.me/">DIO</a>, e o projeto base encontra-se em: <a href="https://github.com/cami-la/credit-application-system">https://github.com/cami-la/credit-application-system</a>.</p>

<figure>
<p align="center">
  <img src="https://i.imgur.com/7phya16.png" height="350" width="450" alt="API para Sistema de Avaliação de Créditos"/><br>
  Diagrama UML Simplificado de uma API para Sistema de Avaliação de Crédito
</p>
</figure>

<h3>Comentário do desafio proposto</h3>
<p>Eu fiz mais algumas alterações em relação a documentação como usar a anotação <code>@Operation</code> e nos testes automatizados eu segui usando o MockK e AssertJ, e na classe <code>CreditResourceIT</code> eu senti a necessidade
de "dropar" as tabelas e subir elas a cada teste, então usei um script junto com a anotação <code>@Sql</code>.</p>
