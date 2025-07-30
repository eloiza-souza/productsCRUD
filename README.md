# Product CRUD API

## Descrição do programa
Este repositório contém uma API RESTful para gerenciar produtos, implementada em Java utilizando o framework Spring Boot. A API permite criar, listar, buscar, atualizar e deletar produtos. É ideal para sistemas que necessitam de um CRUD (Create, Read, Update, Delete) básico para gerenciamento de produtos.
Foi adicionado um workflow para teste de atualização de KS no Stackspot.

-- UPDATE 11 --

## Explicação do funcionamento
A API é composta por um controlador principal (`ProductController`) que expõe endpoints para as operações CRUD. A lógica de negócios é delegada ao serviço (`ProductService`),
 que interage com a camada de persistência para realizar as operações necessárias.

### Endpoints disponíveis
1. **Criar Produto**: `POST /products`  
   Cria um novo produto com base nos dados fornecidos no corpo da requisição.
2. **Listar Todos os Produtos**: `GET /products`  
   Retorna uma lista de todos os produtos cadastrados.
3. **Buscar Produto por ID**: `GET /products/{id}`  
   Retorna os detalhes de um produto específico com base no ID fornecido.
4. **Buscar Produtos por Nome**: `GET /products/search?name={name}`  
   Retorna uma lista de produtos que correspondem ao nome fornecido.
5. **Atualizar Produto**: `PUT /products/{id}`  
   Atualiza os dados de um produto específico com base no ID fornecido.
6. **Deletar Produto**: `DELETE /products/{id}`  
   Remove um produto específico com base no ID fornecido.

### Validação
- A entrada dos dados é validada utilizando as anotações do `Jakarta Validation` (e.g., `@Valid`).
- Caso os dados fornecidos sejam inválidos, a API retorna um erro apropriado com detalhes.

## Instruções para executar o código

### Dependências necessárias
Certifique-se de ter as seguintes ferramentas instaladas:
- **Java 17** ou superior
- **Maven** para gerenciamento de dependências
- **Spring Boot** (configurado no projeto)
- Banco de dados (ex.: H2, MySQL, PostgreSQL) configurado no `application.properties`

### Passos para execução
1. **Clone o repositório**:
   ```bash
     git clone git@github.com:eloiza-souza/productsCRUD.git
     cd productsCRUD
   ```
2. **Configure o banco de dados**:n
3. 

No arquivo src/main/resources/application.properties, configure as credenciais do banco de dados. 

Exemplo para H2:
   ```
      spring.datasource.url=jdbc:h2:mem:testdb
      spring.datasource.driverClassName=org.h2.Driver
      spring.datasource.username=sa
      spring.datasource.password=
      spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   ```

3. **Instale as dependências**:

Execute o comando Maven para baixar as dependências:

````bash
mvn clean install
````

4. **Execute a aplicação**:

Inicie o servidor Spring Boot:
   ```bash
      mvn spring-boot:run
   ```

5. **Acesse a API**:

A API estará disponível em http://localhost:8080/products.

## Exemplos de entrada e saída
### Criar Produto
Requisição: POST /products

Corpo:

````json
{
    "name": "Produto A",
    "description": "Descrição do Produto A",
    "price": 100.0,
    "stockQuantity": 10,
    "category": "Roupas"
}
````
Resposta:
````json
{
    "id": 1,
    "name": "Produto A",
    "description": "Descrição do Produto A",
    "price": 100.0,
    "stockQuantity": 10,
    "category": "Roupas"
}
````

### Listar Todos os Produtos

Requisição: GET /products

Resposta:

````json
[
{
    "id": 1,
    "name": "Produto A",
    "description": "Descrição do Produto A",
    "price": 100.0,
    "stockQuantity": 10,
    "category": "Roupas"
},
{
    "id": 2,
    "name": "Produto B",
    "description": "Descrição do Produto B",
    "price": 200.0,
    "stockQuantity": 10,
    "category": "Alimentos"
}
]
````

### Encontrar Produto pelo Id
Requisição: GET /products/1

Resposta:

````json
{
   "id": 1,
   "name": "Produto A",
   "description": "Descrição do Produto A",
   "price": 100.0,
   "stockQuantity": 10,
   "category": "Roupas"
}
````
Requisição: GET /products/3

Resposta:

````
   Produto com id '3' não encontrado
````
### Atualizar Produto
Requisição: PUT /products/1

Corpo:

````json

{
   "name": "Produto A Atualizado",
   "description": "Nova descrição do Produto A",
   "price": 350.00,
   "stockQuantity": 10,
   "category": "Roupas"
}
````
Resposta:

````json
{
   "id": 1,
   "name": "Produto A Atualizado",
   "description": "Nova descrição do Produto A", 
   "price": 350.00, 
   "stockQuantity": 10,
   "category": "Roupas"
}
````

### Deletar Produto
Requisição: DELETE /products/1

Resposta:
````declarative
Status HTTP 204 No Content.
````

## Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests.

## Licença
Este projeto está licenciado sob a MIT License.
