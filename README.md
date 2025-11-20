# Sistema de Gestão de Ativos - API REST

API REST desenvolvida em Spring Boot para gerenciamento de ativos, colaboradores, empréstimos, manutenções e dispositivos IoT.

## Tecnologias

- Java 17
- Spring Boot 3.5.4
- Spring Data JPA
- Spring Security com JWT
- Oracle Database
- Swagger/OpenAPI
- Maven
- Lombok

## Requisitos

- Java 17+
- Maven 3.6+
- Oracle Database

## Configuração

### Banco de Dados Oracle

As credenciais estão configuradas em `application.yml`:
- Host: 9.169.156.28
- Porta: 1521
- Serviço: GSDB
- Usuário: GSUSER
- Senha: gspassword

### Variáveis de Ambiente (Opcional)

```bash
export DATABASE_USER=GSUSER
export DATABASE_PASSWORD=gspassword
export JWT_SECRET=mySecretKeyForJWTTokenGenerationAndValidationMustBeAtLeast256BitsLong
```

## Executando a Aplicação

```bash
mvn spring-boot:run
```

A aplicação usa o profile `dev` por padrão, conectando-se automaticamente ao Oracle Database.

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

### Swagger UI
Acesse: `http://localhost:8080/swagger-ui.html`

### API Docs (JSON)
Acesse: `http://localhost:8080/api-docs`

## Autenticação

### 1. Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "joao.silva@empresa.com",
  "password": "admin123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer"
}
```

### 2. Usar Token
Inclua o token no header de todas as requisições:
```
Authorization: Bearer {token}
```

### 3. Criar Colaborador com Senha
Para criar um colaborador com senha e role ADMIN:
```bash
POST /api/colaboradores
Content-Type: application/json

{
  "nomeColab": "João Silva",
  "emailColab": "joao.silva@empresa.com",
  "senha": "admin123",
  "role": "ADMIN",
  "statusColab": "ATV"
}
```

**Observações:**
- A senha será automaticamente criptografada com BCrypt
- O campo `senha` no POST/PUT tem o mesmo comportamento da API `definir-senha-colaborador`
- O role padrão é "USER" se não especificado
- Para definir role ADMIN, envie explicitamente `"role": "ADMIN"`

### 4. Definir Senha de Colaborador
```bash
POST /api/colaboradores/{id}/definir-senha
Content-Type: application/json

{
  "senha": "novaSenha123"
}
```

## Endpoints Principais

### Categorias
- `GET /api/categorias` - Listar categorias
- `POST /api/categorias` - Criar categoria
- `GET /api/categorias/{id}` - Buscar por ID
- `PUT /api/categorias/{id}` - Atualizar
- `DELETE /api/categorias/{id}` - Deletar

**Exemplo POST:**
```json
{
  "nomeCateg": "Notebooks",
  "descCateg": "Categoria para notebooks e laptops corporativos"
}
```

**Exemplo PUT:**
```json
{
  "nomeCateg": "Notebooks e Laptops",
  "descCateg": "Categoria atualizada"
}
```

### Ativos
- `GET /api/ativos` - Listar ativos (com filtros: marca, modelo, status, categoriaId)
- `POST /api/ativos` - Criar ativo
- `GET /api/ativos/{id}` - Buscar por ID
- `PUT /api/ativos/{id}` - Atualizar
- `DELETE /api/ativos/{id}` - Deletar

**Exemplo POST:**
```json
{
  "marca": "Dell",
  "modelo": "Latitude 5520",
  "numeroSerie": "DL5520-2024-001",
  "status": "ATV",
  "dataAquisicao": "2024-01-15",
  "categoriaIdCateg": 10
}
```

**Exemplo PUT:**
```json
{
  "marca": "Dell",
  "modelo": "Latitude 5520 Pro",
  "numeroSerie": "DL5520-2024-001",
  "status": "MAN",
  "dataAquisicao": "2024-01-15",
  "dataUltAtualizacao": "2024-11-20",
  "categoriaIdCateg": 10
}
```

**Valores válidos para Status:**
- `"ATV"` - Ativo
- `"INA"` - Inativo
- `"MAN"` - Em Manutenção

### Colaboradores
- `GET /api/colaboradores` - Listar colaboradores (com filtros: nome, email, status, area)
- `POST /api/colaboradores` - Criar colaborador (endpoint público, não requer autenticação)
- `GET /api/colaboradores/{id}` - Buscar por ID
- `PUT /api/colaboradores/{id}` - Atualizar
- `DELETE /api/colaboradores/{id}` - Deletar
- `POST /api/colaboradores/{id}/definir-senha` - Definir/atualizar senha (endpoint público)

**Exemplo POST (com role ADMIN):**
```json
{
  "nomeColab": "João Silva",
  "cpfColab": "12345678901",
  "emailColab": "joao.silva@empresa.com",
  "telColab": "(11) 98765-4321",
  "statusColab": "ATV",
  "funcaoColab": "Gerente de TI",
  "areaColab": "Tecnologia",
  "empresa": "Empresa XYZ",
  "ramalInterno": "1234",
  "senha": "admin123",
  "role": "ADMIN"
}
```

**Observações:**
- A senha será automaticamente criptografada com BCrypt
- O role padrão é "USER" se não especificado
- Para criar colaborador ADMIN, envie `"role": "ADMIN"` explicitamente

### Empréstimos
- `GET /api/emprestimos` - Listar empréstimos (com filtros: status, ativoId, colaboradorId, dataInicio, dataFim)
- `POST /api/emprestimos` - Criar empréstimo
- `GET /api/emprestimos/{id}` - Buscar por ID
- `PUT /api/emprestimos/{id}` - Atualizar
- `DELETE /api/emprestimos/{id}` - Deletar

### Históricos
- `GET /api/historicos` - Listar históricos (com filtros: tipoMovimentacao, ativoId, colaboradorId, dataInicio, dataFim)
- `POST /api/historicos` - Criar histórico
- `GET /api/historicos/{id}` - Buscar por ID
- `PUT /api/historicos/{id}` - Atualizar
- `DELETE /api/historicos/{id}` - Deletar

### Manutenções
- `GET /api/manutencoes` - Listar manutenções (com filtros: tipoManutencao, ativoId, dataInicio, dataFim)
- `POST /api/manutencoes` - Criar manutenção
- `GET /api/manutencoes/{id}` - Buscar por ID
- `PUT /api/manutencoes/{id}` - Atualizar
- `DELETE /api/manutencoes/{id}` - Deletar

### Dispositivos IoT
- `GET /api/dispositivos-iot` - Listar dispositivos (com filtros: identificadorHw, status, ativoId)
- `POST /api/dispositivos-iot` - Criar dispositivo
- `GET /api/dispositivos-iot/{id}` - Buscar por ID
- `PUT /api/dispositivos-iot/{id}` - Atualizar
- `DELETE /api/dispositivos-iot/{id}` - Deletar

## Paginação e Ordenação

Todos os endpoints de listagem suportam paginação e ordenação:

```
GET /api/ativos?page=0&size=10&sort=marca,asc
GET /api/ativos?page=0&size=20&sort=dataAquisicao,desc
```

### Campos Válidos para Ordenação

- **Ativos**: `marca`, `modelo`, `numeroSerie`, `status`, `dataAquisicao`
- **Colaboradores**: `nomeColab`, `emailColab`, `statusColab`, `areaColab`, `funcaoColab`
- **Categorias**: `nomeCateg`, `descCateg`
- **Empréstimos**: `dataEmprestimo`, `dataDevolucao`, `statusEmprestimo`
- **Históricos**: `dataMovimentacao`, `tipoMovimentacao`
- **Manutenções**: `dataInicio`, `dataFim`, `tipoManutencao`, `custo`
- **Dispositivos IoT**: `identificadorHw`, `statusDisp`, `dataCadastro`

## Filtros

Os endpoints de listagem suportam filtros via query parameters:

```
GET /api/ativos?marca=Dell&status=ATV
GET /api/colaboradores?nome=Ana&area=Tecnologia
GET /api/emprestimos?status=EM_USO&dataInicio=2024-01-01
```

## Estrutura do Projeto

```
src/main/java/com/gs/gestaoativos/
├── configurations/      # Configurações (JWT, Security, Swagger)
├── domains/            # Entidades JPA
├── gateways/
│   ├── controllers/    # Controllers REST
│   ├── repositories/   # Interfaces Repository
│   └── dtos/          # Data Transfer Objects
└── services/          # Lógica de negócio
```

## Características Implementadas

### Segurança
- Autenticação JWT completa (geração e validação)
- Criptografia de senhas com BCrypt
- Endpoints públicos: `/api/auth/**`, `/api/admin/**`, `/api/colaboradores/**`
- Endpoints protegidos: todos os demais `/api/**`

### Persistência
- Spring Data JPA com Oracle Database
- Mapeamento de relacionamentos entre entidades
- Geração automática de IDs
- Uso de `@EntityGraph` para evitar LazyInitializationException
- Transações gerenciadas com `@Transactional`

### Validação
- Bean Validation em todos os DTOs
- Validação de campos obrigatórios e tamanhos
- Mensagens de erro personalizadas

### Documentação
- Swagger/OpenAPI com documentação completa
- Exemplos de requisições e respostas
- Documentação de parâmetros de paginação e filtros

### Boas Práticas
- Separação de responsabilidades (Controller, Service, Repository)
- DTOs para transferência de dados
- Tratamento global de exceções
- Logging configurável por profile
