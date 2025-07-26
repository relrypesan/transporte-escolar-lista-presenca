# 🚌 TELP - Transporte Escolar Lista Presença

Sistema completo para controle de presença no transporte escolar, desenvolvido com Spring Boot e MongoDB.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [API Documentation](#api-documentation)
- [Testes](#testes)
- [Deploy](#deploy)
- [Monitoramento](#monitoramento)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## 🎯 Sobre o Projeto

O TELP (Transporte Escolar Lista Presença) é uma aplicação web desenvolvida para facilitar o controle de presença de alunos no transporte escolar. O sistema permite o cadastro de escolas, alunos e o registro de marcações de entrada e saída, proporcionando maior segurança e controle para gestores educacionais.

### Arquitetura

O projeto segue os princípios da **Clean Architecture** e **Hexagonal Architecture**, organizando o código em camadas bem definidas:

- **Core Domain**: Entidades de negócio, Use Cases e Services
- **Adapters**: Controllers REST, Repositories, DTOs e Mappers
- **Infrastructure**: Configurações, persistência MongoDB

## ✨ Funcionalidades

### 🏫 Gestão de Escolas
- ✅ Cadastro de escolas
- ✅ Listagem de todas as escolas
- ✅ Consulta individual por ID
- ✅ Atualização de dados
- ✅ Exclusão de escolas

### 👨‍🎓 Gestão de Alunos
- ✅ Cadastro completo de alunos com:
  - Dados pessoais (nome, sexo, data de nascimento)
  - Endereço completo
  - Vinculação com escola
  - Lista de responsáveis
  - QR Code para identificação
  - Campo de observações
- ✅ Listagem paginada com filtros
- ✅ Consulta individual por ID
- ✅ Atualização de dados
- ✅ Exclusão de alunos
- ✅ Filtro por escola

### 📝 Sistema de Marcação de Presença
- ✅ Registro de presença com:
  - Data e hora da marcação
  - Tipo de marcação (ENTRADA/SAÍDA)
  - Vinculação com aluno
  - Observações opcionais
- ✅ Listagem paginada de marcações por aluno
- ✅ Filtro por data de marcação
- ✅ Consulta individual de marcações
- ✅ Atualização e exclusão de marcações

## 🛠 Tecnologias

### Backend
- **Java 11**
- **Spring Boot 2.7.11**
- **Spring Data MongoDB**
- **Spring Web**
- **Spring Actuator**
- **MapStruct 1.5.3** - Mapeamento de DTOs
- **Lombok** - Redução de boilerplate

### Banco de Dados
- **MongoDB** (MongoDB Atlas)

### Observabilidade
- **Micrometer**
- **Prometheus**
- **Spring Actuator**

### Deploy e CI/CD
- **AWS Elastic Beanstalk**
- **GitHub Actions**

### Testes
- **JUnit 5**
- **Mockito**
- **Spring Boot Test**

## 📋 Pré-requisitos

- **Java 11** ou superior
- **Maven 3.6+**
- **MongoDB** (local ou MongoDB Atlas)
- **Git**

## 🚀 Instalação

### 1. Clone o repositório
```bash
git clone https://github.com/relrypesan/transporte-escolar-lista-presenca.git
cd transporte-escolar-lista-presenca
```

### 2. Configure as variáveis de ambiente
Crie um arquivo `.env` ou configure as seguintes variáveis:

```bash
# Servidor
SERVER_PORT=8080

# MongoDB
MONGODB_USER=seu_usuario
MONGODB_PASS=sua_senha
MONGODB_HOST=seu_host_mongodb
MONGODB_DATABASE=telp_database
```

### 3. Instale as dependências
```bash
mvn clean install
```

## ⚙️ Configuração

### Variáveis de Ambiente Obrigatórias

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SERVER_PORT` | Porta do servidor | `8080` |
| `MONGODB_USER` | Usuário do MongoDB | `admin` |
| `MONGODB_PASS` | Senha do MongoDB | `password123` |
| `MONGODB_HOST` | Host do MongoDB | `cluster0.mongodb.net` |
| `MONGODB_DATABASE` | Nome da database | `telp_production` |

### Configuração do MongoDB

A aplicação está configurada para usar MongoDB Atlas por padrão. Para usar uma instância local:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/telp_local
```

## 🏃‍♂️ Executando a Aplicação

### Desenvolvimento
```bash
mvn spring-boot:run
```

### Produção
```bash
java -jar target/transporte-escolar-lista-presenca-0.2.1.jar
```

A aplicação estará disponível em: `http://localhost:8080/telp`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/telp
```

### 🏫 Endpoints - Escolas

#### Listar todas as escolas
```http
GET /escolas
```

#### Cadastrar nova escola
```http
POST /escolas
Content-Type: application/json

{
  "nome": "Escola Municipal João Silva",
  "endereco": {
    "logradouro": "Rua das Flores, 123",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567"
  }
}
```

#### Consultar escola por ID
```http
GET /escolas/{id_escola}
```

#### Atualizar escola
```http
PATCH /escolas/{id_escola}
Content-Type: application/json

{
  "nome": "Escola Municipal João Silva - Atualizada"
}
```

#### Deletar escola
```http
DELETE /escolas/{id_escola}
```

### 👨‍🎓 Endpoints - Alunos

#### Listar alunos (paginado)
```http
GET /alunos?page=0&size=10&escola_id=123
```

#### Cadastrar novo aluno
```http
POST /alunos
Content-Type: application/json

{
  "nome": "Maria Silva Santos",
  "sexo": "FEMININO",
  "data_nascimento": "2010-05-15",
  "endereco": {
    "logradouro": "Rua das Palmeiras, 456",
    "bairro": "Jardim das Flores",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "12345-678"
  },
  "escola": {
    "id": "escola_id_123"
  },
  "responsaveis": [
    {
      "nome": "João Santos",
      "parentesco": "PAI",
      "telefones": [
        {
          "numero": "(11) 99999-9999",
          "tipo": "CELULAR"
        }
      ]
    }
  ],
  "observacao": "Aluna com necessidades especiais"
}
```

#### Consultar aluno por ID
```http
GET /alunos/{id_aluno}
```

#### Atualizar aluno
```http
PATCH /alunos/{id_aluno}
Content-Type: application/json

{
  "nome": "Maria Silva Santos - Atualizada"
}
```

#### Deletar aluno
```http
DELETE /alunos/{id_aluno}
```

### 📝 Endpoints - Marcações

#### Listar marcações do aluno (paginado)
```http
GET /alunos/{id_aluno}/marcacoes?page=0&size=10&data_marcacao=2023-12-25
```

#### Registrar nova marcação
```http
POST /alunos/{id_aluno}/marcacoes
Content-Type: application/json

{
  "data_marcacao": "2023-12-25",
  "hora_marcacao": "08:30:00",
  "tipo_marcacao": "ENTRADA",
  "observacao": "Embarque normal"
}
```

#### Consultar marcação específica
```http
GET /alunos/{id_aluno}/marcacoes/{id_marcacao}
```

#### Atualizar marcação
```http
PATCH /alunos/{id_aluno}/marcacoes/{id_marcacao}
Content-Type: application/json

{
  "tipo_marcacao": "SAIDA",
  "observacao": "Desembarque na escola"
}
```

#### Deletar marcação
```http
DELETE /alunos/{id_aluno}/marcacoes/{id_marcacao}
```

### 📊 Tipos de Dados

#### Sexo
- `MASCULINO`
- `FEMININO`

#### Tipo de Marcação
- `ENTRADA`
- `SAIDA`

#### Parentesco
- `PAI`
- `MAE`
- `RESPONSAVEL_LEGAL`
- `OUTRO`

#### Tipo de Telefone
- `CELULAR`
- `RESIDENCIAL`
- `COMERCIAL`

## 🧪 Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes específicos
```bash
# Testes de Use Cases
mvn test -Dtest="*UseCaseTest"

# Testes de Services
mvn test -Dtest="*ServiceTest"

# Testes de Controllers
mvn test -Dtest="*ControllerTest"
```

### Cobertura de Testes
O projeto possui **61 testes unitários** cobrindo:
- ✅ **Use Cases** (24 testes)
- ✅ **Services** (33 testes)
- ✅ **Mappers** (6 testes)
- ✅ **Exception Handling** (4 testes)

## 🚀 Deploy

### AWS Elastic Beanstalk

O projeto está configurado para deploy automático no AWS Elastic Beanstalk através do GitHub Actions.

#### Configuração do CI/CD

O pipeline é executado automaticamente quando há push na branch `master`:

1. **Build**: Compilação e testes com Maven
2. **Security**: Verificação de segurança com GitGuardian
3. **Deploy**: Deploy automático no AWS Elastic Beanstalk

#### Variáveis de Ambiente no AWS

Configure as seguintes variáveis no Elastic Beanstalk:

```
SERVER_PORT=5000
MONGODB_USER=production_user
MONGODB_PASS=production_password
MONGODB_HOST=production-cluster.mongodb.net
MONGODB_DATABASE=telp_production
```

### Deploy Manual

```bash
# 1. Gerar o JAR
mvn clean package -DskipTests

# 2. Upload do arquivo target/transporte-escolar-lista-presenca-0.2.1.jar
# para o AWS Elastic Beanstalk
```

## 📊 Monitoramento

### Endpoints do Actuator

A aplicação expõe endpoints de monitoramento via Spring Actuator:

- **Health Check**: `GET /telp/actuator/health`
- **Métricas**: `GET /telp/actuator/metrics`
- **Prometheus**: `GET /telp/actuator/prometheus`

### Exemplo de Health Check
```json
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP",
      "details": {
        "version": "5.0.0"
      }
    }
  }
}
```

## 🤝 Contribuindo

### Fluxo de Contribuição

1. **Fork** o projeto
2. **Clone** seu fork
3. **Crie** uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
4. **Commit** suas mudanças (`git commit -m 'feat: adiciona nova funcionalidade'`)
5. **Push** para a branch (`git push origin feature/nova-funcionalidade`)
6. **Abra** um Pull Request

### Padrões de Commit

Utilizamos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `style:` Formatação
- `refactor:` Refatoração
- `test:` Testes
- `chore:` Tarefas de build/configuração

### Executando Testes Localmente

Antes de enviar um PR, certifique-se de que todos os testes passam:

```bash
mvn clean test
```

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📞 Contato

**Desenvolvedor**: Relry Pereira  
**GitHub**: [@relrypesan](https://github.com/relrypesan)  
**Email**: relrypesan@gmail.com

---

<div align="center">
  <p>Feito com ❤️ para melhorar a segurança no transporte escolar</p>
</div>
