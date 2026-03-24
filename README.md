# 🏦 Resilient Payment Gateway

Este projecto é uma API de processamento de pagamentos construída para garantir alta disponibilidade, idempotência e resiliência em sistemas distribuídos.

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Resilience4j](https://img.shields.io/badge/Resilience4j-Circuit%20Breaker-ff6f61?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-Containers-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit%205-Testing-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-Mocking-78A641?style=for-the-badge)
</div>

## 📖 Sobre o Projeto
O foco central desta aplicação é a robustez. O sistema lida com o desafio crítico de evitar cobranças duplicadas em pagamentos e garantir que o serviço permaneça funcional mesmo sob falhas de rede em gateways externos.

## ✨ Principais Funcionalidades

### 💳 Processamento de Pagamentos
- Criação e processamento de pagamentos de forma segura
- Suporte a estados: `PENDING`, `SUCCESS`, `FAILED`
- Validação de dados de entrada (valor, moeda)

### 🔁 Idempotência de Transações
- Prevenção de cobranças duplicadas usando **X-Idempotency-Key**
- Armazenamento de requisições de pagamento em cache (Redis)

### ⚡ Resiliência com Circuit Breaker
- Implementação com **Resilience4j**
- Proteção contra falhas de gateways externos
- Fallback automático em caso de indisponibilidade
- Retry com backoff exponencial


### 📦 Persistência e Consistência
- Banco relacional com **PostgreSQL**
- Versionamento de schema com **Flyway**

### 🚀 Cache e Performance
- Uso de **Redis** para:
  - Idempotência
- Redução de latência e carga no banco

### 📡 Integração com Gateways Externos
- Abstração de  provedores de pagamento
- Timeout configurável
- Tratamento de falhas de rede

### 🧪 Testes Automatizados
- Testes unitários com **JUnit 5**
- Mock de dependências com **Mockito**
- Cobertura de regras críticas de negócio

### 📄 Documentação de API
- Documentação interativa com **Swagger / OpenAPI**
- Exemplos de request/response
- Testes diretos via browser

## 🛠️ Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.11+**
- **Spring Data JPA + Hibernate**
- **Resilience4j**
- **PostgreSQL**
- **Redis**
- **Flyway**
- **Lombok**
- **JUnit 5 + Mockito**
- **Maven**
- **OpenAPI / Swagger**
- **Docker + Docker Compose**



## 🚀 Como Começar (Quick Start)

### Pré-requisitos

- Java 21
- Maven 3.8+
- Docker (opcional, mas recomendado)
- PostgreSQL (ou container via Docker)
- Redis (ou container via Docker)

### 1. Clone o repositório

```bash
git clone https://github.com/alfredobaptista/banking-application.git
cd banking-application
```

### 2. Configure o ambiente (dev)
- Crie/ edite o arquivo src/main/resources/application-dev.yml:

```json
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banking_db
    username: postgres
    password: sua_senha_forte
  flyway:
    enabled: true
    locations: classpath:db/migration
```
- Ou use Docker Compose (recomendado):

```bash
docker-compose up -d
```

### 3. Execute a aplicação

```bash
# Opção 1 - Maven
mvn spring-boot:run -Dspring.profiles.active=dev

# Opção 2 - Jar
mvn clean package
java -jar target/banking-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### 4. Acesse a documentação Swagger

```json
http://localhost:8080/swagger-ui.html
# ou
http://localhost:8080/swagger-ui/index.html
```

## 🔐 Endpoints Principais

| Método | Endpoint                | Descrição           |
|--------|-------------------------|---------------------|
| POST   | `/api/v1/payments`      | Efectuar pagamento  |
| GET    | `/api/v1/payments/:id`  | Consultar Pagamento |

*(Consulta a interface Swagger para a lista completa, exemplos de request/response e schemas detalhados)*

## 🐳 Docker

```bash
# Build da imagem
docker build -t banking-api:latest .

# Executar
docker-compose up --build
```

## 🧪 Testes

### Executar todos os testes (unitários)

```bash
mvn test
```

## 📸 Capturas de Ecrã

![Swagger UI](/docs/images/swegger.PNG)

![Exemplo de Pagamento](/docs//images/transaction.PNG)

## 👤 Autor

**Alfredo Fernando Baptista**

- GitHub: [@alfredobaptista](https://github.com/alfredobaptista)
- LinkedIn: [linkedin.com/in/alfredobaptista](https://www.linkedin.com/in/alfredobaptista)
- Email: baptistaalfredo81@gmail.com

## 🙌 Contribuições
Contribuições são super bem-vindas!  
Podes abrir *issues* para sugestões/bugs ou *pull requests* com melhorias.
Gostaste? Dá uma ⭐ no repositório para apoiar o projeto! 🚀







Recentemente, mergulhei fundo no desafio de construir um Payment Gateway que fosse, acima de tudo, resiliente. 
Sabemos que, no mundo das fintechs e sistemas de alta performance, não basta apenas "fazer o pedido chegar ao destino". 
O segredo está em como o sistema se comporta quando as coisas dão errado.

Desenvolvi um serviço de pagamentos onde o foco total foi a confiabilidade:

✅ Idempotência: Garanti que pagamentos não sejam processados em duplicidade, mesmo em casos de falhas de rede.
✅ Resiliência: Implementei o Circuit Breaker (com Resilience4j) para evitar falhas em cascata quando o gateway externo apresenta instabilidade.
✅ Arquitetura Moderna: Estruturei o projeto seguindo os padrões de mercado, utilizando FeignClient para integração declarativa e garantindo o desacoplamento entre domínio e infraestrutura.
✅ Observabilidade & Documentação: API documentada com Swagger e monitorada via Spring Boot Actuator.
✅ Infraestrutura como Código: O ambiente completo (App + Postgres + Redis) sobe com um único comando docker-compose up.

Este projeto foi um exercício intenso de boas práticas e, acima de tudo, de pensamento crítico sobre como construir sistemas que suportam carga e garantem a integridade dos dados.

Convido os desenvolvedores e entusiastas da área a conferirem o código no GitHub. Feedbacks são sempre bem-vindos!

🔗 Link para o repositório: [COLE O LINK DO SEU GITHUB AQUI]

#Java #SpringBoot #Fintech #SoftwareEngineering #Resiliencia #DesenvolvimentoSoftware #JavaDeveloper  #CleanArchitecture #AngolaTech