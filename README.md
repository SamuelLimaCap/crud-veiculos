# 🚗 Compra Carros - Plataforma de Anúncios de Veículos

Plataforma completa de compra e venda de veículos com autenticação JWT, filtros avançados, integração com tabela FIPE e sistema de pedidos de compra.

## 📋 Sobre o Projeto

O **Compra Carros** é uma aplicação full-stack que permite:

- ✅ Cadastro e autenticação de usuários
- ✅ Anúncio de veículos com upload de imagens
- ✅ Filtros avançados (preço, quilometragem, ano, marca, modelo)
- ✅ Ordenação de resultados
- ✅ Integração com tabela FIPE para consultar marcas, modelos e anos
- ✅ Sistema de pedidos de compra
- ✅ Paginação com "Mostrar Mais"
- ✅ Interface responsiva e mobile-friendly
- ✅ Documentação interativa com Swagger

## 🏗️ Arquitetura

```
compra-carros/
├── backend/                    # API REST Spring Boot
│   ├── src/
│   ├── pom.xml
│   ├── README.md
│   └── ...
├── frontend/                   # Aplicação React + TypeScript
│   ├── src/
│   ├── package.json
│   ├── README.md
│   └── ...
└── README.md                   # Este arquivo
```

## 🚀 Início Rápido

### Pré-requisitos Globais

- **Java 21** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 16+** - [Download](https://nodejs.org/)
- **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/)
- **Git** - [Download](https://git-scm.com/)

### Verificar Instalações

```bash
java -version      # Java 21
mvn -version       # Maven
node -v            # Node.js
npm -v             # NPM
psql --version     # PostgreSQL
```

## 📦 Configuração do Banco de Dados

### 1. Criar banco PostgreSQL

Abra o psql ou uma ferramenta como DBeaver:

```sql
CREATE DATABASE sysveiculo;
CREATE USER sysveiculo_admin WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE sysveiculo TO sysveiculo_admin;
```

## 🔧 Instalação e Execução

### Backend (Spring Boot)

Acesse a pasta `backend/`:

```bash
cd backend

# Instalar dependências
mvn install

# Rodar a aplicação
mvn spring-boot:run
```

A API estará disponível em: **http://localhost:8080**

Documentação Swagger: **http://localhost:8080/swagger-ui.html**

Mais detalhes em: [backend/README.md](./backend/README.md)

### Frontend (React)

Abra outra aba/terminal e acesse a pasta `frontend/`:

```bash
cd frontend

# Instalar dependências
npm install

# Rodar a aplicação
npm run dev
```

A aplicação estará disponível em: **http://localhost:5173**

Mais detalhes em: [frontend/README.md](./frontend/README.md)

## 📚 Documentação

- **[Backend README](./backend/README.md)** - Configuração, dependências, troubleshooting
- **[Frontend README](./frontend/README.md)** - Scripts, dependências, estrutura
- **Swagger UI** - http://localhost:8080/swagger-ui.html (quando backend está rodando)

## 🔐 Autenticação

A plataforma usa **JWT (JSON Web Token)** para autenticação segura.

### Fluxo de Autenticação

1. **Registrar** - Criar nova conta
```bash
POST http://localhost:8080/auth/signup
Content-Type: application/json

{
  "fullname": "João Silva",
  "email": "joao@example.com",
  "password": "SenhaForte123!"
}
```

2. **Login** - Obter token JWT
```bash
POST http://localhost:8080/auth/signin
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
      "fullname": "João Silva"
    }
  }
}
```

3. **Usar Token** - Incluir em requisições autenticadas
```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📱 Funcionalidades Principais

### 1. Autenticação
- Registro de novo usuário
- Login com JWT
- Logout seguro

### 2. Anúncios
- Criar anúncio de veículo
- Listar anúncios com paginação
- Filtros avançados (preço, km, ano, marca, modelo)
- Ordenar por preço, km, ano
- Atualizar anúncio
- Deletar anúncio
- Finalizar venda (marcar cliente)

### 3. Pedidos de Compra
- Fazer pedido de compra
- Listar pedidos do usuário
- Desistir de pedido

### 4. FIPE
- Consultar marcas de veículos
- Listar modelos por marca
- Listar anos disponíveis por modelo

## 🌐 URLs Úteis

| Serviço | URL |
|---------|-----|
| **Frontend** | http://localhost:5173 |
| **Backend API** | http://localhost:8080 |
| **Swagger Docs** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |

## 🛠️ Stack Tecnológico

### Frontend
- React 19
- TypeScript
- Vite (build tool)
- React Router
- React Hook Form
- React Select
- Axios
- React Toastify

### Backend
- Spring Boot 3.5.8
- Spring Data JPA
- Spring Security
- JWT (java-jwt 4.4.0)
- PostgreSQL
- Lombok
- Springdoc OpenAPI (Swagger)
- Jsoup

## 🔍 Endpoints API Principais

### Autenticação
- `POST /auth/signup` - Registrar
- `POST /auth/signin` - Login
- `POST /auth/logout` - Logout

### Anúncios
- `GET /api/vendas` - Listar com paginação
- `GET /api/vendas/filtrar` - Filtrar avançado
- `GET /api/vendas/{id}` - Obter anúncio
- `POST /api/vendas/anunciar` - Criar anúncio
- `PUT /api/vendas/alterar-infomações` - Atualizar
- `DELETE /api/vendas/invalidar-anuncio/{id}` - Deletar

### Pedidos
- `GET /api/compras` - Listar pedidos
- `POST /api/compras` - Criar pedido
- `DELETE /api/compras/{id}` - Cancelar pedido

### FIPE
- `GET /api/fipe/marca/{id}/modelos` - Modelos por marca
- `GET /api/fipe/marca/{marcaId}/modelo/{modeloId}/anos` - Anos

Para dúvidas ou problemas:

1. Consulte o [Backend README](./backend/compraCarros/README.md)
2. Consulte o [Frontend README](./frontend/crud-veiculos/README.md)
3. Acesse a documentação Swagger: http://localhost:8080/swagger-ui.html


**Desenvolvido com Spring Boot + React**
