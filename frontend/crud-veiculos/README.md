# 🚗 CRUD Veículos - Frontend React

Aplicação React com TypeScript para gerenciamento de anúncios de veículos, incluindo autenticação, filtros avançados e paginação.

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Node.js** (versão 16 ou superior) - [Download](https://nodejs.org/)
- **npm** ou **yarn** (gerenciador de pacotes)
- **Git** (opcional, para clonar o repositório)

## 🔧 Instalação

### 1. Clonar o repositório (opcional)

```bash
git clone https://github.com/seu-usuario/crud-veiculos.git
cd crud-veiculos
```

### 2. Instalar dependências

```bash
npm install
```

ou se usar yarn:

```bash
yarn install
```

## 🚀 Rodando o projeto

### Modo Desenvolvimento

Inicia o servidor de desenvolvimento com hot reload:

```bash
npm run dev
```

ou com yarn:

```bash
yarn dev
```

A aplicação estará disponível em: **http://localhost:5173**

### Modo Build (Produção)

Para gerar uma build otimizada para produção:

```bash
npm run build
```

ou com yarn:

```bash
yarn build
```

Os arquivos compilados estarão na pasta `dist/`.

### Preview da Build

Para visualizar a build gerada localmente:

```bash
npm run preview
```

ou com yarn:

```bash
yarn preview
```

## 📦 Dependências Principais

| Dependência | Versão | Descrição |
|-------------|--------|-----------|
| **React** | 19.2.0 | Biblioteca UI |
| **React Router** | 7.9.6 | Roteamento |
| **React Hook Form** | 7.66.1 | Gerenciamento de formulários |
| **React Select** | 5.10.2 | Componente select customizado |
| **Axios** | 1.13.2 | Cliente HTTP |
| **React Toastify** | 11.0.5 | Notificações toast |
| **TypeScript** | 5.6.0 | Tipagem estática |
| **Vite** | 7.2.4 | Build tool e dev server |

## 🛠️ Scripts Disponíveis

```bash
# Desenvolvimento
npm run dev        # Inicia servidor dev com hot reload

# Build
npm run build      # Compila TypeScript e gera build otimizada

# Linting
npm run lint       # Verifica código com ESLint

# Preview
npm run preview    # Visualiza build de produção localmente
```

## 📁 Estrutura do Projeto

```
crud-veiculos/
├── src/
│   ├── components/       # Componentes React reutilizáveis
│   ├── pages/           # Páginas da aplicação
│   ├── services/        # Serviços (API, etc)
│   ├── hooks/           # Custom hooks
│   ├── App.tsx          # Componente raiz
│   └── main.tsx         # Ponto de entrada
├── dist/                # Build final (gerada após `npm run build`)
├── package.json         # Dependências e scripts
├── tsconfig.json        # Configuração TypeScript
├── vite.config.ts       # Configuração Vite
└── README.md            # Este arquivo
```

## ⚙️ Configuração da API

A aplicação se conecta a um backend Spring Boot. Certifique-se de que o servidor está rodando.

## 🔍 Verificação de Código

Para verificar problemas de linting:

```bash
npm run lint
```

Para corrigir automaticamente alguns problemas:

```bash
npm run lint -- --fix
```

## 📱 Funcionalidades

- ✅ Autenticação de usuários
- ✅ Listagem de anúncios de veículos
- ✅ Filtros avançados (preço, quilometragem, ano, marca, modelo)
- ✅ Paginação com "Mostrar Mais"
- ✅ Ordenação de resultados
- ✅ Responsivo e mobile-friendly

## 🌐 URLs Importantes

- **Dev Server**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Swagger API Docs**: http://localhost:8080/swagger-ui.html

## 📚 Tecnologias Utilizadas

- **React 19** - Biblioteca UI
- **TypeScript** - Tipagem estática
- **Vite** - Build tool ultrarrápido
- **React Router** - Roteamento SPA
- **React Hook Form** - Gerenciamento de formulários
- **Axios** - Client HTTP
- **ESLint** - Linting de código

## 🐛 Troubleshooting

### Erro: "Port 5173 is already in use"

A porta 5173 já está em uso. Você pode:

1. Matar o processo na porta:
```bash
# Windows
netstat -ano | findstr :5173
taskkill /PID <PID> /F

# macOS/Linux
lsof -i :5173
kill -9 <PID>
```

### Erro: "Cannot find module"

Limpe a cache e reinstale as dependências:

```bash
rm -rf node_modules package-lock.json
npm install
npm run dev
```

### Erro de CORS

Se receber erros de CORS, configure o backend no arquivo SecurityConfig para permitir requisições do frontend:

```java
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

```
