# Documentação Técnica - HubSpot Integration API

## Introdução

Este projeto é uma API REST em Java desenvolvida com Spring Boot para integrar com a API do HubSpot utilizando o fluxo OAuth 2.0 (authorization code flow). O objetivo é demonstrar a capacidade de entender um desafio técnico, aplicar boas práticas de desenvolvimento (SOLID, Clean Code e segurança) e entregar uma aplicação completa e de fácil manutenção.

## Bibliotecas e Dependências Utilizadas

- **Spring Boot**  
  Utilizado como framework principal para criação da API REST, gerenciamento de injeção de dependências e configuração simplificada dos endpoints.

- **Spring Boot Starter Web**  
  Fornece o suporte para desenvolvimento de endpoints REST e integração com o ecossistema web do Spring.

- **Spring Boot Starter Security**  
  Adiciona recursos de segurança à aplicação, permitindo a proteção de endpoints e o uso de filtros customizados (como o de autenticação e rate limiting).

- **RestTemplate**  
  Escolhido para realizar chamadas HTTP à API do HubSpot, se destaca pela simplicidade, ampla utilização no mercado e compatibilidade com o fluxo atual do projeto.

- **Bucket4j**  
  Biblioteca utilizada para implementar o rate limiting. Foi escolhida por sua facilidade de configuração, performance e a capacidade de definir limites de requisições de forma precisa (100 requisições a cada 10 segundos globalmente e 250.000 por dia por conta).

- **Lombok**  
  Utilizado para reduzir o boilerplate de código, facilitando a criação de DTOs e outras classes por meio de anotações como `@Data`.

- **JUnit 5 e Mockito**  
  Utilizados para a implementação de testes unitários e de integração, fazendo a verificação do funcionamento dos fluxos críticos (autenticação, criação de contatos e rate limiting).

- **Docker (Multi-Stage Build)**  
  Implementação do build multi-stage para criar uma imagem Docker enxuta, separando a fase de build (usando uma imagem Maven) da fase de runtime (utilizando uma imagem baseada no OpenJDK). As variáveis sensíveis (como HUBSPOT_CLIENT_ID e HUBSPOT_CLIENT_SECRET) são passadas via arquivo `.env`.

## Decisões de Arquitetura e Design

- **Separação de Responsabilidades (SOLID e Clean Code)**  
  O projeto foi dividido em pacotes bem definidos:
    - `config`: para a configuração de beans (RestTemplate, SecurityConfig, etc.);
    - `controller`: para expor os endpoints REST de autenticação, criação de contatos e webhook;
    - `dto`: para definir objetos de transferência de dados (TokenResponseDTO, ContactDTO, etc.);
    - `enums`: para definir os enums utilizados pela aplicação;
    - `exception`: para definir exceções customizadas;
    - `filter`: para configurar os filtros aplicados em cada requisição;
    - `service`: que encapsula a lógica de negócio da aplicação;
    - `util`: que contém as classes com utilitários.  
    
      Essa organização permite alta coesão e facilita a manutenção e evolução da aplicação.

- **Fluxo OAuth 2.0**  
  Foram implementados dois endpoints principais para o fluxo OAuth:
    - `GET /auth/url`: Gera a URL de autorização, incluindo um parâmetro `state` assinado para prevenção de ataques CSRF;
    - `GET /auth/callback`: Processa o callback da autorização, validando o `state` recebido (por meio da assinatura) e trocando o código pelo token de acesso.  
      A escolha de assinar o `state` utilizando o próprio `clientSecret` do HubSpot (via HMAC) foi feita para evitar dependência de armazenamento de sessão e permitir que o fluxo seja validado mesmo se iniciado por clientes diferentes.

- **Rate Limiting**  
  Foi implementado usando Bucket4j, com dois níveis de controle:
    - Um limite global de 100 requisições a cada 10 segundos para o aplicativo;
    - Um limite por conta (identificado pelo token do usuário) de 250.000 requisições por dia.  
      Essa abordagem garante conformidade com as políticas de rate limit da API do HubSpot e protege o backend contra abusos.

- **Testes Unitários e de Integração**  
  A implementação de testes com JUnit 5 e Mockito permite a verificação dos serviços de autenticação e criação de contatos, além do filtro de rate limiting. Isso assegura que a aplicação se comporta conforme esperado, tanto em cenários de sucesso quanto de falha.

- **Docker e Configuração de Ambiente**  
  O uso de um arquivo `.env` e variáveis de ambiente para configurar valores sensíveis (como client secret) e dinâmicos (como o redirect URI) possibilita que a mesma base de código seja executada localmente e na nuvem com as configurações apropriadas para cada ambiente.

## Possíveis Melhorias Futuras

- **Armazenamento do State**  
  Em ambientes distribuídos, utilizar um armazenamento compartilhado (como Redis ou um banco de dados) para gerenciar o `state` pode ser uma melhoria para evitar problemas de sessão.

- **Implementação de atualização do token pelo Refresh Token**  
  Em uma aplicação real, é importante armazenar o refresh token de forma segura (em um banco de dados) para permitir a renovação do token de acesso sem intervenção do usuário.

- **Melhorias no Rate Limit**  
  Ao integrar com um resource server, o rate limit seria aplicado por conta, e não pelo token (que é redefinido a cada 30 minutos). Resolvi fazer deste modo apenas com o viés de demonstrar o funcionamento, mas a solução implementada não é definitiva e nem recomendada para um ambiente de produção. Isso poderia ser feito utilizando um banco de dados ou cache para armazenar o estado do rate limit.

- **Monitoramento e Logging Avançado**  
  Implementar frameworks de monitoramento (como Sonar e Grafana) e logging estruturado para facilitar a análise de desempenho e depuração em produção.

- **Segurança Adicional**  
  Integrar com um resource server e utilizar tokens JWT em uma aplicação real para garantir a segurança e integridade dos dados.

## Conclusão

A solução foi construída com foco em clareza, aderência às boas práticas e pragmatismo. Cada decisão – desde a escolha de bibliotecas até a organização dos pacotes – visa garantir uma aplicação completa, simples de entender e de fácil manutenção. As escolhas, como o uso de Spring Boot, Bucket4j, RestTemplate e Docker, foram fundamentadas na necessidade de atender aos requisitos do desafio, com ênfase em segurança, qualidade e facilidade em rodar o ambiente.

---