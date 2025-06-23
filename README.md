# Desafio Técnico - API de Clientes e Contas

Este projeto foi desenvolvido como parte do desafio técnico para seleção de estágio Java + Spring.

---

## 💪 Tecnologias Utilizadas

* Java 17
* Spring Boot
  * Spring Web
  * Spring Data JPA
* PostgreSQL
* Lombok
* JUnit + Mockito
* Swagger

---

## 📊 Objetivo

Desenvolver uma API REST para gerenciamento de **Clientes** e suas **Contas**, com regras de negócio e boas práticas.

---

## 💼 Entidades e Regras

### 📅 Cliente

| Campo    | Tipo   | Regras                          |
| -------- | ------ | ------------------------------- |
| id       | Long   | Gerado automaticamente          |
| nome     | String | Obrigatório                     |
| cpf      | String | Obrigatório, único (11 dígitos) |
| telefone | String | Opcional                        |
| email    | String | Opcional, formato válido        |

### 💸 Conta

| Campo      | Tipo          | Regras                                 |
| ---------- | ------------- | -------------------------------------- |
| id         | Long          | Gerado automaticamente                 |
| referencia | String        | Obrigatório. Formato: MM-AAAA          |
| valor      | BigDecimal    | Obrigatório. ≥ 0                       |
| situacao   | Enum (String) | Obrigatório: PENDENTE, PAGA, CANCELADA |

**Relacionamento:** Uma conta está associada a um cliente.

**Regras de Negócio:**

* Não permitir conta com valor < 0
* Não permitir conta sem cliente existente
* Não permitir criar conta já com situação CANCELADA

---

## 🔢 Endpoints

### Clientes

* `POST /clientes` → Criar cliente
* `PUT /clientes/{id}` → Atualizar cliente
* `DELETE /clientes/{id}` → Excluir cliente
* `GET /clientes` → Listar todos os clientes

### Contas

* `POST /clientes/{idCliente}/contas` → Criar conta para cliente
* `PUT /contas/{id}` → Atualizar conta
* `DELETE /contas/{id}` → Cancelar conta (exclusão lógica)
* `GET /clientes/{idCliente}/contas` → Listar contas de um cliente

---

## 💡 Extras implementados

* DTOs para entrada e saída
* Validações com Bean Validation
* Swagger UI para documentação: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* Tratamento global de erros com `@RestControllerAdvice`
* Logs de operações com SLF4J
* Testes unitários de serviço com JUnit e Mockito

---

## 🚀 Como executar
1. Clone este repositório:

```bash
git clone https://github.com/Thiago-Brito/ras-desafio-tecnico.git
cd ras-desafio-tecnico
```

2. Configure o banco de dados PostgreSQL com as credenciais abaixo ou altere no application.propertiesL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/desafio
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. Execute o projeto:

```bash
./mvnw spring-boot:run
```

4. Acesse a documentação da API:
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📅 Testes

Testes unitários cobrem cenários de sucesso e erro para `ClienteService` e `ContaService`.

---

## 👍 Considerações finais

O projeto está estruturado em camadas:

* `controller` → exposição de endpoints
* `service` → regras de negócio
* `dto` → objetos de transferência
* `model` → entidades JPA
* `repository` → acesso a dados
