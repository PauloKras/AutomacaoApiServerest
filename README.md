# Serverest Login Automation Project

Este projeto tem como objetivo automatizar os testes de login da API [Serverest](https://serverest.dev/?lang=pt-BR) utilizando Java, Maven e as melhores práticas de automação, incluindo o uso de Page Objects (adaptado para serviços de API), Payloads e gerenciamento de configuração.

## Tecnologias Utilizadas

*   **Java 11+**: Linguagem de programação.
*   **Maven**: Ferramenta de automação de build e gerenciamento de dependências.
*   **RestAssured**: Biblioteca para facilitar os testes de APIs REST.
*   **JUnit 5**: Framework de testes para Java.
*   **Jackson**: Biblioteca para serialização/desserialização de objetos Java para/de JSON.
*   **Git**: Sistema de controle de versão.
*   **GitLab CI/CD**: Para automação de pipeline de integração contínua e entrega contínua.

## Estrutura do Projeto

A estrutura do projeto segue as convenções Maven e padrões de automação de testes de API:

```
.
├── pom.xml
├── .gitignore
├── .gitlab-ci.yml
└── src
    └── main
        └── java
            └── com
                └── serverest
                    └── login
                        ├── entities      // Modelos de dados (POJOs) para requisições e respostas (e.g., LoginRequest, LoginResponse, User)
                        ├── services      // Camada de serviços da API (equivalente a Page Objects para APIs, e.g., AuthService, UserService)
                        └── utils         // Classes utilitárias (e.g., ConfigurationManager)
        └── resources
            └── config.properties // Arquivo de configuração para dados sensíveis ou de ambiente (ignorado pelo Git)
    └── test
        └── java
            └── com
                └── serverest
                    └── login
                        ├── tests         // Classes de teste (e.g., LoginTests)
                        └── setup         // Classes base para configuração de testes, se necessário
```

## Configuração do Ambiente

1.  **Pré-requisitos**:
    *   Java Development Kit (JDK) 11 ou superior.
    *   Apache Maven 3.x.x.
    *   Um ambiente de desenvolvimento integrado (IDE) como IntelliJ IDEA ou VS Code.
    *   Git instalado.

2.  **Clonar o Repositório**:
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO_GITLAB>
    cd ServerestLoginAutomation
    ```

3.  **Construir o Projeto Maven**:
    ```bash
    mvn clean install
    ```
    Este comando irá baixar todas as dependências do Maven e compilar o projeto.

## Como Executar os Testes

Os testes podem ser executados via Maven:

```bash
mvn test
```

Este comando executará todos os testes JUnit 5 localizados em `src/test/java`.

## Gerenciamento de Configuração (config.properties)

O arquivo `src/main/resources/config.properties` é usado para armazenar configurações específicas do ambiente, como a URL base da API e credenciais de teste. Este arquivo é **ignorado pelo Git (`.gitignore`)** e não deve ser commitado com informações sensíveis reais.

Exemplo de `config.properties`:

```properties
api.base.url=https://serverest.dev
test.user.email=fulano@qa.com
test.user.password=teste
test.admin.email=admin@qa.com
test.admin.password=admin
```

## Pipeline CI/CD com GitLab CI

O projeto inclui um arquivo `.gitlab-ci.yml` para configurar um pipeline de CI/CD no GitLab. Este pipeline automatizará a construção e execução dos testes a cada push para o repositório.

**Variáveis CI/CD**:
Para garantir a segurança e flexibilidade, as credenciais e URLs sensíveis devem ser configuradas como **variáveis de CI/CD** no GitLab, em vez de serem hardcoded ou expostas no repositório. O arquivo `.gitlab-ci.yml` está configurado para ler estas variáveis e criar dinamicamente o `config.properties` durante a execução do pipeline.

As seguintes variáveis devem ser definidas no GitLab (Settings > CI/CD > Variables):

*   `API_BASE_URL`
*   `TEST_USER_EMAIL`
*   `TEST_USER_PASSWORD`
*   `TEST_ADMIN_EMAIL`
*   `TEST_ADMIN_PASSWORD`

## Suposições

*   A API Serverest possui um endpoint `/login` que aceita `POST` com `email` e `password` e retorna um `authorization` token em caso de sucesso.
*   A API Serverest possui um endpoint `/usuarios` que aceita `POST` para registro de usuários com `nome`, `email`, `password`, `administrador`.
*   Para fins de teste, novos usuários são registrados dinamicamente antes dos testes de login para garantir isolamento. A exclusão de usuários não foi implementada nos testes para manter o foco na automação de login, mas em um cenário real seria uma boa prática incluir a limpeza de dados.

---
Desenvolvido por Roo.
