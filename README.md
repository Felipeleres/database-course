# Conexões JDBC – Laboratório de Bancos de Dados em Java

Pequeno conjunto de projetos desenvolvidos para praticar **conexões entre Java e diversos bancos de dados** usando **JDBC**.  
Cada módulo representa um SGBD diferente e demonstra operações básicas como conexão, consultas e manipulação de dados.

## 🧠 Conceitos praticados
- Uso da API **JDBC**
- Conexão via `DriverManager`
- Execução de comandos SQL (`Statement`, `PreparedStatement`)
- Manipulação de resultados com `ResultSet`
- Tratamento de exceções (`SQLException`)
- Organização de classes utilitárias
- Parametrização de conexões
- Exemplos iniciais de integração com bancos **NoSQL**

## 🗂️ Bancos abordados nos módulos

### 🟦 SQL – Bancos Relacionais
- **jmysql** — conexão com *MySQL*
- **jpostgresql** — conexão com *PostgreSQL*
- **jsqlite** — uso do *SQLite* via JDBC
- **jbase** — módulo base com estrutura genérica de conexão

### 🟥 NoSQL – Bancos Não Relacionais
- **jmongodb** — conexão com *MongoDB*
- **jredis** — integração simples com *Redis*
- **jfirebase** — acesso inicial ao Firebase Realtime Database
- **jcouchdb** — conexão com *CouchDB* via HTTP

Laboratório para entender como cada tipo de banco se conecta e interage com Java sem frameworks.

## 🎯 Objetivo do laboratório
Criar uma base sólida para:
- compreender drivers e padrões de conexão,
- diferenciar bancos SQL e NoSQL,
- entender padrões de acesso a dados,
- preparar conhecimento para **JPA/Hibernate** e **Spring Data JPA**.

