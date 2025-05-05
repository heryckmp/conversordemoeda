# Challenge ONE - Conversor de Moeda

Projeto desenvolvido como parte do Challenge ONE Java Back-End da Oracle Next Education + Alura.

## Funcionalidades

- Conversor de Moedas utilizando a API ExchangeRate-API.

## Como Executar (usando Maven)

1. Certifique-se de ter o [Java JDK](https://www.oracle.com/java/technologies/downloads/) (versão 11 ou superior) e o [Maven](https://maven.apache.org/download.cgi) instalados e configurados.
2. Clone este repositório ou baixe o código.
3. Abra um terminal na pasta raiz do projeto (`conversordemoeda_c`).
4. Compile o projeto: `mvn compile`
5. Execute a aplicação: `mvn exec:java -Dexec.mainClass="com.heryckmp.conversor.ConversorApp"`

Alternativamente, você pode gerar um JAR executável:
1. Gere o JAR: `mvn package`
2. Execute o JAR: `java -jar target/conversordemoeda-1.0-SNAPSHOT-jar-with-dependencies.jar` 