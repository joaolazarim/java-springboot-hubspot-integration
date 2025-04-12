# Java Spring Boot HubSpot Integration Project

Este projeto é uma API REST em Java com Spring Boot que integra com a API do HubSpot utilizando o fluxo OAuth 2.0. A aplicação fornece endpoints para autenticação, criação de contatos e para receber notificações via webhook.

---

## Documentação Técnica

Para mais detalhes sobre as escolhas de bibliotecas, decisões de arquitetura e possíveis melhorias futuras, consulte a [Documentação Técnica](DOCUMENTATION.md).

---

## API na Nuvem

A API já está disponível e rodando na nuvem, com a URL:

[https://java-springboot-hubspot-integration.onrender.com](https://java-springboot-hubspot-integration.onrender.com)

---

## Pré-Requisitos

- Conta de desenvolvedor no HubSpot com um aplicativo configurado.
- Docker instalado (caso deseje rodar via contêiner).
- (Opcional) Java e Maven instalados para rodar localmente fora do contêiner.

---

## Configuração (variáveis de ambiente)

1. Copie o arquivo `.env.example` para `.env`:

```
cp .env.example .env
```

2. Preencha as variáveis:
- `HUBSPOT_CLIENT_ID`: Seu Client ID do aplicativo HubSpot.
- `HUBSPOT_CLIENT_SECRET`: Seu Client Secret.
- `HUBSPOT_REDIRECT_URI`: Se estiver rodando localmente, pode deixar em branco ou usar `http://localhost:8080/auth/callback`.

Exemplo de `.env`:

```
HUBSPOT_CLIENT_ID=seuClientId
HUBSPOT_CLIENT_SECRET=seuClientSecret
HUBSPOT_REDIRECT_URI=http://localhost:8080/auth/callback
```

> **Observação:** Não versione o arquivo `.env` (já está adicionado no `.gitignore`).

---

## Rodando via Docker

1. **Build da Imagem Docker**  
   No diretório raiz, execute:

```
docker build -t hubspot-integration:latest .
```

2. **Execução do Contêiner**  
   Rode a aplicação com:

```
docker run --env-file .env -p 8080:8080 hubspot-integration:latest
```

Isso iniciará a aplicação na porta 8080.

---

## Rodando Localmente (fora do Docker)

1. **Variáveis de Ambiente**  
   Defina as variáveis `HUBSPOT_CLIENT_ID` e `HUBSPOT_CLIENT_SECRET` em seu ambiente:
- No IntelliJ, vá em **Run/Debug Configurations** > **Environment variables** e adicione:
  ```
  HUBSPOT_CLIENT_ID=<seuClientId>;HUBSPOT_CLIENT_SECRET=<seuClientSecret>
  ```
  
2. **Inicie a Aplicação**
- Execute a classe principal (ou `mvn spring-boot:run`) para iniciar a aplicação.
- A aplicação estará disponível em `http://localhost:8080`.

---

## Endpoints

### 1. `GET /auth/url`
Gera a URL de autenticação, incluindo o `state`. Redirecione o usuário para essa URL a fim de iniciar o fluxo OAuth.

### 2. `GET /auth/callback`
Endpoint de callback após o usuário autorizar a aplicação no HubSpot.
- Recebe o `code` e o `state`.
- Valida o `state`assinado para garantir que a resposta é legítima.
- Troca o `code` por um token de acesso e o retorna.

### 3. `POST /contacts`
Cria um contato no HubSpot.
- **Headers**: `Authorization: Bearer <seuAccessToken>`
- **Body (Exemplo)**:
```json
{
   "properties": {
       "email": "john@doe.com",
       "lastname": "Doe",
       "firstname": "John"
   }
}
```

### 4. `POST /webhook/contacts`

Endpoint que recebe notificações de criação de contatos (ou outros eventos configurados no HubSpot).
- **Exemplo de payload**:

```json
[
  {
    "appId": 10639157,
    "eventId": 100,
    "subscriptionId": 3442089,
    "portalId": 49648261,
    "occurredAt": 1744311835599,
    "subscriptionType": "contact.creation",
    "attemptNumber": 0,
    "objectId": 123,
    "changeSource": "CRM",
    "changeFlag": "NEW"
  }
]
```

- A aplicação registrará no log quando o evento for recebido.

---

## Testes
- Os testes de unidade se encontram no diretório src/test/java.
- Para executá-los via Maven: `mvn clean test`

## Observações

- O log de eventos do webhook em produção só pode ser visualizado no Render, através da minha conta. Ou seja, ao criar um novo contato rodando localmente, o webhook não será chamado, pois não há como registrar um webhook para localhost no HubSpot.
- Para subir a aplicação na nuvem, é necessário configurar corretamente as variáveis de ambiente e também configurar o webhook no HubSpot para apontar para a URL pública da aplicação, no endpoint `/webhook/contacts`.