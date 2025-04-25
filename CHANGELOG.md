# Changelog

Todas as mudanças relevantes do projeto serão adicionadas à este documento.

## [1.0.1] - 25/04/2025
### Adicionado
- Implementada a validação de assinatura para o Webhook do HubSpot:
  - **v1**: Hash SHA-256 do `clientSecret + body`
  - **v2**: Hash SHA-256 do `clientSecret + método HTTP + URI + body`
  - **v3**: HMAC-SHA256 + Base64 do `método HTTP + URI decodificada + body + timestamp`, rejeitando timestamps acima de 5 minutos
- Adicionado `WebhookSignatureValidator` tratando todas as versões internamente
- Atualizado `WebhookController` para aceitar uma String com o JSON bruto e delegar a validação da assinatura

## [1.0.0] - 12/04/2025
### Adicionado
- Início da implementação do projeto
-  Endpoints principais:
   - `GET /auth/url`
   - `GET /auth/callback`
   - `POST /contacts`
   - `POST /webhook`
- Adicionado link para a [Documentação Técnica](DOCUMENTATION.md)
