# CRUD simples de geração de boleto - Spring Rest
Projeto de CRUD simples para geração de boleto utilizando Spring Rest e banco de dados H2 em memória.

## Como rodar
### Subindo o servidor e o banco
Para rodar o projeto utilize o gradle wrapper rodando este comando no projeto.

```cmd
gradlew bootRun
```
Observação: 
* É importante configurar o JAVA_HOME se não for encontrado o java pelo gradle;
* Este projeto é compatível com a versão 1.8 do java. 

### Acessar o servidor
Ao rodar o projeto subirá o servidor na porta 8080 e banco de dados H2 em memória que irá criar a tabela de boleto automaticamente.
É possível utilizar o [postman](https://www.getpostman.com/apps) para fazer os testes. 

##Exemplos de chamada

### Criar boleto
URL
```url
POST http://localhost:8080/rest/bankslips
```
Body
```json
{
  "due_date" : "2018-02-30" ,
  "total_in_cents" : "100000.00" ,
  "customer" : "Itau S.A" ,
  "status" : "PENDING"
}
```
###Listar boletos
URL
```url
GET http://localhost:8080/rest/bankslips
```
Retorno
```json
[
  {
    "id": "ff4f7e09-3744-4f7b-aef9-a3d8feb6cfcb",
    "due_date": "2018-02-28",
    "customer": "Itau S.A",
    "total_in_cents": 300000.12
  }
]
```
###Ver detalhes do boleto
URL
```url
GET http://localhost:8080/rest/bankslips/{id}
```
Retorno
```json
{
    "id": "76c902bf-21f8-4704-a4cb-cf9f7e270f42",
    "due_date": "2018-02-28",
    "total_in_cents": 300000.12,
    "customer": "Itau S.A",
    "status": "PENDING",
    "fine": 294000.12
}
```
###Pagar boleto
URL
```url
PUT http://localhost:8080/rest/bankslips/{id}
```
Body
```json
{
  "status" : "PAID"
}
```
###Cancelar boleto
URL
```url
PUT http://localhost:8080/rest/bankslips/{id}
```
Body
```json
{
  "status" : "CANCELED"
} 

