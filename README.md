# Projeto-BD
# Sistema de Locadora de Filmes - Terminal Java

Projeto final para a cadeira de BD

  Este é um sistema simples de gerenciamento de locadora de filmes, implementado como uma aplicação de terminal em Java. 
Ele usa Spring Boot e Spring Data JPA para persistência de dados com PostgreSQL. 
O objetivo é demonstrar operações CRUD (Criar, Ler, Atualizar, Deletar) para Clientes, Filmes e Locações.

  # Funcionalidades
  
Gerenciamento completo (CRUD) de Clientes.
Gerenciamento completo (CRUD) de Filmes (incluindo busca por gênero e controle de estoque).
Gerenciamento completo (CRUD) de Locações (registro, devolução, listagem de pendentes).

  # Tecnologias Principais
Java 17+
Spring Boot
Spring Data JPA
PostgreSQL
Hibernate (usado pelo Spring Data JPA)
Lombok (para código mais limpo)
  
  # Configuração e Como Rodar
  
1. Banco de Dados (PostgreSQL)
Crie um banco de dados chamado locadora_filmes no PostgreSQL.
Execute os scripts SQL abaixo para criar as tabelas.
Você pode usar o pgAdmin ou qualquer cliente SQL.
-------------------------------------------------------------------------------

-- Tabela Clientes
CREATE TABLE Clientes (
    cliente_id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    endereco VARCHAR(255)
);

-- Tabela Filmes
CREATE TABLE Filmes (
    filme_id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    genero VARCHAR(50),
    ano_lancamento INT,
    diretor VARCHAR(100),
    quantidade_estoque INT NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0)
);

-- Tabela Locacoes
CREATE TABLE Locacoes (
    locacao_id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    filme_id INT NOT NULL,
    data_locacao DATE NOT NULL DEFAULT CURRENT_DATE,
    data_devolucao DATE,
    valor_total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Clientes(cliente_id) ON DELETE CASCADE,
    FOREIGN KEY (filme_id) REFERENCES Filmes(filme_id) ON DELETE RESTRICT
);

-------------------------------------------------------------------------------

2. Configuração da Aplicação

Abra o projeto na sua IDE favorita (IntelliJ IDEA, VS Code, Eclipse).
Vá para src/main/resources/application.properties.
Atualize as credenciais do seu banco de dados:

-----------------------------------------------------------------------------------------

spring.datasource.url=jdbc:postgresql://localhost:5432/locadora_filmes
spring.datasource.username=seu_usuario_postgres
spring.datasource.password=sua_senha_postgres
spring.jpa.hibernate.ddl-auto=update

##Para não mostrar o SQL do Hibernate no console, mantenha as linhas abaixo comentadas:

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

-----------------------------------------------------------------------------------------

3. Rodar

Execute a classe principal LocadoraAppApplication.java diretamente da sua IDE.
Um menu interativo aparecerá no terminal para você gerenciar os dados da locadora.
