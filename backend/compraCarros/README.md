# 🚗 CRUD Veículos - Backend Spring Boot

API REST para gerenciamento de anúncios de veículos com autenticação JWT, filtros avançados e integração com tabela FIPE.

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 21** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/) (recomendado)
- **Git** (opcional, para clonar o repositório)

### Verificar Instalação

```bash
java -version
mvn -version
psql --version
```

## 🗄️ Configuração do Banco de Dados

### Opção 1: PostgreSQL (Recomendado)

#### 1. Criar banco de dados e usuário

Abra o psql ou uma ferramenta como DBeaver e execute:

```sql
CREATE DATABASE sysveiculo;
CREATE USER sysveiculo_admin WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE sysveiculo TO sysveiculo_admin;
```

#### 2. Configurar connection string

Edite o arquivo `application.properties` ou `application.yml`:

**application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sysveiculo
spring.datasource.username=sysveiculo_admin
spring.datasource.password=1234
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Opção 2: H2 (Banco em Memória)

#### 1. Editar pom.xml

Localize a seção de dependências H2:

```xml
<!-- H2 Database (descomente para usar) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

E comente a dependência PostgreSQL:

```xml
<!-- PostgreSQL (comente se usar H2) -->
<!--
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
-->
```

#### 2. Configurar application.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

#### 3. Acessar H2 Console

A aplicação estará em:
```
http://localhost:8080/h2-console
```

**Credenciais padrão:**
- JDBC URL: `jdbc:h2:mem:testdb`
- User Name: `sa`
- Password: (deixe em branco)

## 🚀 Rodando o Projeto

### 1. Clonar o repositório (opcional)

```bash
git clone https://github.com/seu-usuario/compra-carros.git
cd compra-carros
```

### 2. Compilar o projeto

```bash
mvn clean compile
```

### 3. Instalar dependências

```bash
mvn install
```

### 4. Rodar a aplicação

```bash
mvn spring-boot:run
```

Ou execute direto a classe main:

```bash
mvn exec:java -Dexec.mainClass="com.support.compracarros.CompraCarrosApplication"
```

A aplicação estará disponível em: **http://localhost:8080**

## 📚 Documentação da API

### Swagger UI

Acesse a documentação interativa da API:

```
http://localhost:8080/swagger-ui.html
```

Aqui você pode:
- ✅ Visualizar todos os endpoints
- ✅ Ver modelos de requisição/resposta
- ✅ Testar os endpoints diretamente


## 🔐 Autenticação

A API usa **JWT (JSON Web Token)** para autenticação.

### Fluxo de Autenticação

1. **Sign Up** - Criar nova conta
```bash
POST /auth/signup
Content-Type: application/json

{
  "fullName": "João Silva",
  "email": "joao@example.com",
  "password": "SenhaForte123!"
}
```

2. **Sign In** - Fazer login
```bash
POST /auth/signin
Content-Type: application/json

{
  "email": "joao@example.com",
  "password": "SenhaForte123!"
}
```

Resposta:
```json
{
  "status": "success",
  "message": "logado com sucesso",
  "content": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userDetails": {
      "id": 1,
      "email": "joao@example.com",
      "fullName": "João Silva"
    }
  }
}
```

3. **Usar Token** - Incluir em requisições
```bash
GET /api/vendas
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📦 Dependências Principais

| Dependência | Versão | Descrição |
|-------------|--------|-----------|
| **Spring Boot** | 3.5.8 | Framework web |
| **Spring Data JPA** | - | ORM e persistência |
| **Spring Security** | - | Autenticação e autorização |
| **PostgreSQL Driver** | Latest | Driver JDBC para PostgreSQL |
| **H2 Database** | Latest | Banco em memória (opcional) |
| **JWT (java-jwt)** | 4.4.0 | Tokens JWT - Auth0 |
| **JJWT Jackson** | 0.12.3 | Alternativa JWT |
| **Lombok** | Latest | Reduz boilerplate |
| **Jsoup** | 1.21.1 | Parser HTML/XML |
| **Springdoc OpenAPI** | 2.8.12 | Swagger/OpenAPI UI |

## 🛠️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/support/compracarros/
│   │   ├── controllers/          # REST Controllers
│   │   ├── services/             # Lógica de negócio
│   │   ├── repositories/         # Acesso a dados
│   │   ├── entities/             # Modelos JPA
│   │   ├── entities/             # Modelos
│   │   ├── dtos/                 # Data Transfer Objects
│   │   ├── config/               # Configurações (Security, CORS, Mail)
│   │   ├── utils/                # Utils
│   │   ├── exceptions/           # Tratamento de erros
│   │   ├── filters/              # Filtros HTTP
│   │   └── CompraCarrosApplication.java
│   └── resources/
│       ├── application.properties
│       ├── tabelafipemarcamodelo.sql
└── test/                         # Testes unitários
    └── java/com/support/compracarros/
```

## 🔍 Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/signup` | Criar novo usuário |
| POST | `/auth/signin` | Fazer login |
| POST | `/auth/logout` | Fazer logout |

### Anúncios de Veículos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/vendas` | Listar anúncios (com paginação) |
| GET | `/api/vendas/filtrar` | Filtrar anúncios avançado |
| GET | `/api/vendas/{id}` | Obter anúncio por ID |
| GET | `/api/vendas/getByCreatedUser/{idUser}` | Listar anúncios do usuário |
| POST | `/api/vendas/anunciar` | Criar novo anúncio |
| PUT | `/api/vendas/alterar-infomações` | Atualizar anúncio (PUT) |
| PATCH | `/api/vendas` | Atualizar anúncio (PATCH) |
| PATCH | `/api/vendas/{id}/cliente/{clienteId}` | Finalizar anúncio com cliente |
| DELETE | `/api/vendas/invalidar-anuncio/{id}` | Deletar anúncio |

### Pedidos de Compra

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/compras` | Listar todos os pedidos |
| GET | `/api/compras/{id}` | Obter pedido por ID |
| GET | `/api/compras/anuncio/{id}` | Listar pedidos de um anúncio |
| POST | `/api/compras` | Criar novo pedido |
| DELETE | `/api/compras/{id}` | Desistir da compra |

### FIPE (Tabela de Preços)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/fipe/marca/{id}/modelos` | Listar modelos por marca |
| GET | `/api/fipe/marca/{marcaId}/modelo/{modeloId}/anos` | Listar anos por modelo |


## ⚙️ Configurações Importantes

### application.properties

```properties
# Servidor
server.port=8080
server.servlet.context-path=/

# Database
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
app.jwt.secret=sua-chave-secreta-aqui-minimo-32-caracteres
app.jwt.expiration=86400000

# OpenAPI/Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🐛 Troubleshooting

### Erro: "Database 'sysveiculo' does not exist"

Certifique-se de que criou o banco:

```sql
CREATE DATABASE sysveiculo;
```

### Erro: "Connection refused" na porta 5432

PostgreSQL não está rodando. Inicie o serviço:

**Windows:**
```bash
pg_ctl -D "C:\Program Files\PostgreSQL\data" start
```

**macOS (Homebrew):**
```bash
brew services start postgresql
```

**Linux:**
```bash
sudo systemctl start postgresql
```

### Erro: "Invalid JWT token"

O token JWT expirou ou é inválido. Faça login novamente para obter um novo token.

### Erro de CORS

Configure CORS no `SecurityConfig`:

```java
@Configuration
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        var allowedOrigins = new ArrayList<>(List.of("http://127.0.0.1:5173", "http://localhost:5173"));

        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
```


Para dúvidas ou problemas:
1. Verifique a documentação Swagger: http://localhost:8080/swagger-ui.html


