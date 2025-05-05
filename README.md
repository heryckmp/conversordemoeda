# ✨ Conversor de Moedas para Console ✨

[![Java Version](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Maven Build](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE) <!-- Opcional: Adicionar arquivo LICENSE -->

Projeto desenvolvido como parte do **Challenge ONE Java Back-End** da Oracle Next Education + Alura. Trata-se de uma aplicação Java de console que permite converter valores entre diferentes moedas utilizando taxas de câmbio em tempo real.

---

## 🚀 Funcionalidades

*   **Interface de Console Interativa:** Menu textual amigável com emojis e bordas para facilitar a navegação.
*   **Conversão de Moedas:** Suporta conversão bidirecional entre BRL (Real Brasileiro) e outras moedas populares (USD, EUR, GBP, ARS, CLP).
*   **Taxas de Câmbio Reais:** Integração com a API [ExchangeRate-API](https://www.exchangerate-api.com/) para obter taxas atualizadas.
*   **Configuração Flexível:** Carrega a chave da API de um arquivo externo (`config.properties`) para maior segurança.
*   **Menu Dinâmico:** As opções de conversão são geradas dinamicamente a partir de uma lista no código, facilitando a adição de novas moedas.

---

## 🛠️ Pré-requisitos

Antes de começar, garanta que você tenha os seguintes softwares instalados e configurados corretamente no seu sistema:

*   **Java Development Kit (JDK):** Versão 11 ou superior. (O projeto foi testado com JDK 21).
    *   Verifique com: `java -version` e `javac -version`
    *   A variável de ambiente `JAVA_HOME` deve estar configurada.
*   **Apache Maven:** Ferramenta de automação de build.
    *   Verifique com: `mvn -version`
    *   As variáveis de ambiente `MAVEN_HOME` (ou `M2_HOME`) e `PATH` devem estar configuradas.

---

## ⚙️ Configuração

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/heryckmp/conversordemoeda.git
    cd conversordemoeda
    ```
2.  **Obtenha uma API Key:**
    *   Cadastre-se gratuitamente no site [ExchangeRate-API](https://www.exchangerate-api.com/) para obter sua chave de API pessoal.
3.  **Configure a API Key:**
    *   Crie um arquivo chamado `config.properties` na raiz do projeto (no mesmo nível que o `pom.xml`).
    *   Adicione a seguinte linha ao arquivo, substituindo `SUA_CHAVE_API_AQUI` pela chave que você obteve:
      ```properties
      API_KEY=SUA_CHAVE_API_AQUI
      ```
    *   **Importante:** O arquivo `config.properties` está incluído no `.gitignore` e **não deve** ser commitado no repositório para manter sua chave segura.

---

## 🏗️ Como Construir (Build)

Com o Maven configurado, navegue até a pasta raiz do projeto no seu terminal e execute o seguinte comando para compilar o código e gerar um JAR executável com todas as dependências incluídas:

```bash
mvn clean package
```
Este comando limpará builds anteriores e criará o arquivo `conversordemoeda-1.0-SNAPSHOT-jar-with-dependencies.jar` dentro do diretório `target/`.

---

## ▶️ Como Executar

Após construir o projeto com sucesso, você pode executar a aplicação de console usando o comando:

```bash
java -jar target/conversordemoeda-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Siga as instruções apresentadas no menu interativo para realizar as conversões de moeda.

---

## 📸 Screenshots (Exemplo)

*(Sugestão: Adicione aqui screenshots do menu principal e do menu de conversão em execução no seu terminal)*

**Menu Principal:**
```
╔═══════════════════════════════════════════════════════╗
║         ✨ Bem-vindo ao Conversor de Moedas ✨         ║
╠═══════════════════════════════════════════════════════╣
║ Escolha uma opção:                                    ║
║                                                       ║
║ 1️⃣ - Realizar Conversão de Moedas                   ║
║                                                       ║
║ 0️⃣ - Sair do Programa                              ║
╚═══════════════════════════════════════════════════════╝
▶️ Digite a opção desejada:
```

**Menu de Conversão:**
```
╔═══════════════════════════════════════════════════════╗
║--- 💱 Conversor de Moedas (Base: BRL 🇧🇷) ---         ║
╠═══════════════════════════════════════════════════════╣
║  1) 🇧🇷 BRL ➡️ 🇺🇸 USD                                  ║
║  2) 🇺🇸 USD ➡️ 🇧🇷 BRL                                  ║
║  3) 🇧🇷 BRL ➡️ 🇪🇺 EUR                                  ║
║  4) 🇪🇺 EUR ➡️ 🇧🇷 BRL                                  ║
║  5) 🇧🇷 BRL ➡️ 🇬🇧 GBP                                  ║
║  6) 🇬🇧 GBP ➡️ 🇧🇷 BRL                                  ║
║  7) 🇧🇷 BRL ➡️ 🇦🇷 ARS                                  ║
║  8) 🇦🇷 ARS ➡️ 🇧🇷 BRL                                  ║
║  9) 🇧🇷 BRL ➡️ 🇨🇱 CLP                                  ║
║ 10) 🇨🇱 CLP ➡️ 🇧🇷 BRL                                  ║
╠═══════════════════════════════════════════════════════╣
║ 0) 🔙 Voltar ao Menu Principal                        ║
╚═══════════════════════════════════════════════════════╝
▶️ Digite a opção:
```

---

## 💡 Possíveis Melhorias Futuras

*   Adicionar mais moedas à lista `MOEDAS_ALVO`.
*   Implementar um cache de taxas com tempo de expiração.
*   Adicionar tratamento de erro mais específico para diferentes falhas da API.
*   Permitir que o usuário escolha a moeda base.
*   Criar testes unitários e de integração.
*   Refatorar para uma arquitetura mais robusta (ex: separar lógica de UI). 