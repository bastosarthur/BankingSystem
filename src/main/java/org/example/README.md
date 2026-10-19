# Banking System

A console-based banking application developed in Java, using Hibernate ORM and PostgreSQL for data persistence. The project focuses on object-oriented programming, database integration, input validation, transaction management, and secure password storage.

This project was built as part of my journey toward becoming a backend developer, with an emphasis on understanding how application logic interacts with a relational database.

## Technologies

- Java 21
- Maven
- Hibernate ORM 7
- PostgreSQL
- Jakarta Persistence (JPA)
- jBCrypt
- IntelliJ IDEA

## Features

### Account Management
- Create bank accounts with CPF validation.
- Prevent duplicate CPF registrations.
- Store account credentials separately from account information.
- Authenticate users using their account number and password.
- Limit login attempts to three per login session.
- Automatically expire inactive sessions after five minutes.
- Log in and log out of accounts.

### Banking Operations
- Check account balance.
- Deposit money.
- Withdraw money.
- Transfer money between accounts.
- View transaction history.
- Search and filter transaction records by id and by date.

### Security and Data Integrity
- Hash passwords using BCrypt.
- Validate user input (Regex) and transaction amounts,
- Use `BigDecimal` for monetary values.
- Apply database constraints to protect data integrity.
- Use Hibernate transactions to manage database operations.
- Separate business logic from database access through service and DAO layers.

## Project Structure

The project follows a layered structure to separate user interaction, business logic, database operations, and entity mapping.

```text
BankingSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java
│       │           ├── InputValidation.java
│       │           ├── Account.java
│       │           ├── BankTransaction.java
│       │           ├── AccountService.java
│       │           ├── AccountDAO.java
│       │           ├── TransactionDAO.java
│       │           └── HibernateUtil.java
│       └── resources/
│           └── hibernate.cfg.xml
├── banking_schema.sql
├── pom.xml
└── README.md
```

### Architecture

- **Main:** Handles menus, user interaction, and application flow.
- **InputValidation:** Handles input validation.
- **AccountService:** Implements banking operations and business rules.
- **AccountDAO:** Handles account-related database operations.
- **TransactionDAO:** Handles transaction-related database operations.
- **Account and BankTransaction:** Represent the application's persistent entities.
- **HibernateUtil:** Configures and provides the Hibernate `SessionFactory`.
- **banking_schema.sql:** Contains the PostgreSQL database schema required to recreate the tables and constraints.

## Database Structure

The application uses PostgreSQL with a dedicated `banking` schema.

The supplied `banking_schema.sql` file contains the database structure, including tables, identity columns, foreign keys, and constraints. It does not contain account records, transaction records, or passwords.

Tables:

- `banking.accounts`
- `banking.account_password`
- `banking.transactions`

## Getting Started

### Prerequisites

Install the following:

- Java JDK 21
- Maven
- PostgreSQL
- Git

### 1. Clone the Repository

Replace `<your-repository-url>` with the URL of this repository.

```bash
git clone <your-repository-url>
cd BankingSystem
```

### 2. Create the Database

Create a PostgreSQL database named `projects`:

```sql
CREATE DATABASE projects;
```

You can use the PostgreSQL interface in IntelliJ IDEA or another PostgreSQL client to execute this command.

### 3. Import the Database Schema

From the project directory, run:

```bash
psql -U postgres -d projects -f banking_schema.sql
```

If `psql` is not available in your system's PATH, use its full installation path. For example, on Windows with PostgreSQL 17 installed in the default directory:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d projects -f banking_schema.sql
```

Replace `postgres` with your PostgreSQL username if necessary.

The schema import creates the required tables and constraints. No existing account data is imported.

### 4. Configure Environment Variables

The application reads its database connection settings from environment variables to avoid hardcoding database credentials in the Java source code.

Configure the following variables in your operating system or IDE run configuration:

| Variable | Example value |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/projects` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | Your local PostgreSQL password |

Use your own PostgreSQL username and password. If you created a database with a different name, update `DB_URL` accordingly.

**Important:** Do not commit your actual database password or other credentials to the repository.

### 5. Build the Project

From the project directory, run:

```bash
mvn clean package
```

### 6. Run the Application

Run `Main.java` from IntelliJ IDEA after configuring the environment variables and importing the database schema.

## Learning Outcomes

Through this project, I practiced:

- Applying object-oriented programming principles in a multi-class Java application.
- Designing and implementing a relational database schema.
- Mapping Java entities to PostgreSQL tables with Hibernate ORM.
- Managing persistence contexts, sessions, and database transactions.
- Separating business logic from database access using service and DAO layers.
- Implementing validation and handling invalid operations.
- Working with monetary values using `BigDecimal`.
- Implementing password hashing with BCrypt.
- Using Maven to manage project dependencies.
- Using Git and GitHub for version control and portfolio development.

## Future Improvements

Potential improvements as I continue learning include:

- Unit and integration tests.
- A REST API using Spring Boot.
- More advanced authentication and authorization.
- Structured application logging.
- Docker-based deployment.
- Cloud deployment and database management.

## Disclaimer

This project is intended for educational and portfolio purposes. It is not designed or audited for use in a real banking environment.

---

# Sistema Bancário

Uma aplicação bancária executada pelo console, desenvolvida em Java, utilizando Hibernate ORM e PostgreSQL para persistência de dados. O projeto tem como foco programação orientada a objetos, integração com banco de dados, validação de entradas, gerenciamento de transações e armazenamento seguro de senhas.

Este projeto faz parte da minha jornada para me tornar desenvolvedor backend, com ênfase em entender como a lógica de uma aplicação interage com um banco de dados relacional.

## Tecnologias

- Java 21
- Maven
- Hibernate ORM 7
- PostgreSQL
- Jakarta Persistence (JPA)
- jBCrypt
- IntelliJ IDEA

## Funcionalidades

### Gerenciamento de Contas
- Criar contas bancárias com validação de CPF.
- Impedir cadastros duplicados de CPF.
- Armazenar as credenciais separadamente das informações da conta.
- Autenticar usuários utilizando número da conta e senha.
- Limitar as tentativas de login a três por sessão de autenticação.
- Encerrar automaticamente sessões inativas após cinco minutos.
- Realizar login e logout.

### Operações Bancárias
- Consultar saldo.
- Depositar dinheiro.
- Sacar dinheiro.
- Transferir dinheiro entre contas.
- Consultar histórico de transações.
- Pesquisar e filtrar registros de transações.

### Segurança e Integridade dos Dados
- Armazenar senhas utilizando hashes BCrypt.
- Validar entradas do usuário e valores das transações.
- Utilizar `BigDecimal` para valores monetários.
- Aplicar restrições no banco de dados para proteger a integridade dos dados.
- Utilizar transações do Hibernate para gerenciar operações no banco.
- Separar a lógica de negócio do acesso ao banco por meio de camadas de serviço e DAO.

## Estrutura do Projeto

O projeto utiliza uma estrutura em camadas para separar a interação com o usuário, a lógica de negócio, as operações de banco de dados e o mapeamento das entidades.

```text
BankingSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java
│       │           ├── InputValidation.java
│       │           ├── Account.java
│       │           ├── BankTransaction.java
│       │           ├── AccountService.java
│       │           ├── AccountDAO.java
│       │           ├── TransactionDAO.java
│       │           └── HibernateUtil.java
│       └── resources/
│           └── hibernate.cfg.xml
├── banking_schema.sql
├── pom.xml
└── README.md
```

### Arquitetura

- **Main:** Gerencia menus, interação com o usuário e fluxo da aplicação.
- **InputValidation:** Gerencia a validação das entradas.
- **AccountService:** Implementa as operações bancárias e as regras de negócio.
- **AccountDAO:** Gerencia as operações de banco de dados relacionadas às contas.
- **TransactionDAO:** Gerencia as operações de banco de dados relacionadas às transações.
- **Account e BankTransaction:** Representam as entidades persistidas pela aplicação.
- **HibernateUtil:** Configura e disponibiliza a `SessionFactory` do Hibernate.
- **banking_schema.sql:** Contém o esquema PostgreSQL necessário para recriar as tabelas e restrições.

## Estrutura do Banco de Dados

A aplicação utiliza PostgreSQL com um esquema dedicado chamado `banking`.

O arquivo `banking_schema.sql` contém a estrutura do banco, incluindo tabelas, colunas de identidade, chaves estrangeiras e restrições. Ele não contém registros de contas, transações ou senhas.

Tabelas:

- `banking.accounts`
- `banking.account_password`
- `banking.transactions`

## Como Executar

### Pré-requisitos

Instale os seguintes programas:

- Java JDK 21
- Maven
- PostgreSQL
- Git

### 1. Clonar o Repositório

Substitua `<your-repository-url>` pela URL deste repositório.

```bash
git clone <your-repository-url>
cd BankingSystem
```

### 2. Criar o Banco de Dados

Crie um banco PostgreSQL chamado `projects`:

```sql
CREATE DATABASE projects;
```

Você pode executar esse comando pela interface PostgreSQL do IntelliJ IDEA ou por outro cliente PostgreSQL.

### 3. Importar o Esquema do Banco

No diretório do projeto, execute:

```bash
psql -U postgres -d projects -f banking_schema.sql
```

Se o comando `psql` não estiver disponível no PATH do sistema, utilize o caminho completo da instalação. Por exemplo, no Windows com PostgreSQL 17 instalado no diretório padrão:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d projects -f banking_schema.sql
```

Substitua `postgres` pelo seu usuário PostgreSQL, se necessário.

A importação cria as tabelas e restrições necessárias. Nenhum dado de conta existente é importado.

### 4. Configurar as Variáveis de Ambiente

A aplicação lê as configurações de conexão com o banco por meio de variáveis de ambiente, evitando deixar credenciais diretamente no código Java.

Configure as seguintes variáveis no sistema operacional ou na configuração de execução da IDE:

| Variável | Valor de exemplo |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/projects` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | Sua senha local do PostgreSQL |

Utilize seu próprio usuário e senha do PostgreSQL. Caso tenha criado um banco com outro nome, atualize o valor de `DB_URL`.

**Importante:** Não envie sua senha real do banco nem outras credenciais para o repositório.

### 5. Compilar o Projeto

No diretório do projeto, execute:

```bash
mvn clean package
```

### 6. Executar a Aplicação

Execute `Main.java` pelo IntelliJ IDEA após configurar as variáveis de ambiente e importar o esquema do banco de dados.

## Aprendizados

Durante o desenvolvimento deste projeto, pratiquei:

- Aplicação dos princípios de programação orientada a objetos em uma aplicação Java com múltiplas classes.
- Modelagem e implementação de um esquema de banco de dados relacional.
- Mapeamento de entidades Java para tabelas PostgreSQL com Hibernate ORM.
- Gerenciamento de contextos de persistência, sessões e transações de banco de dados.
- Separação da lógica de negócio do acesso ao banco utilizando camadas de serviço e DAO.
- Implementação de validações e tratamento de operações inválidas.
- Utilização de `BigDecimal` para valores monetários.
- Implementação de hash de senhas com BCrypt.
- Utilização do Maven para gerenciamento de dependências.
- Utilização do Git e GitHub para controle de versão e desenvolvimento de portfólio.

## Melhorias Futuras

Possíveis melhorias conforme avanço nos estudos:

- Testes unitários e de integração.
- Uma API REST utilizando Spring Boot.
- Autenticação e autorização mais avançadas.
- Logs estruturados da aplicação.
- Implantação utilizando Docker.
- Implantação em nuvem e gerenciamento de banco de dados.

## Aviso

Este projeto foi desenvolvido para fins educacionais e de portfólio. Ele não foi projetado nem auditado para utilização em um ambiente bancário real.