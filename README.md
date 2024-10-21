# Agendamento de Folga


## Descrição

O **Agendamento de Folga** é uma aplicação web desenvolvida em Java utilizando o framework Spring Boot, que permite que empresas gerenciem o agendamento de folgas de seus funcionários de forma simples e eficiente. O sistema possibilita que os operadores consultem datas disponíveis e agendem folgas, evitando conflitos de agendamento.

## Funcionalidades

- **Busca de Funcionários via Banco**: Adicione e gerencie os funcionários da empresa.
- **Agendamento de Folgas**: Permite que os operadores agendem suas folgas em datas disponíveis.
- **Verificação de Disponibilidade**: O sistema verifica se já existe uma folga agendada para um operador em uma determinada data, evitando agendamentos duplicados.
- **Interface Intuitiva**: Interface amigável e de fácil utilização para os usuários.
- **Sistema de Modal**: Notificações em tempo real sobre o sucesso ou falha no agendamento de folgas.

## Tecnologias Utilizadas

- **Backend**: 
  - Java
  - Spring Boot
  - Spring MVC
  - Spring Data JPA
  - Hibernate
  - Banco de Dados (SQL Server)

- **Frontend**:
  - HTML
  - CSS
  - JavaScript
  - Thymeleaf

## Como Executar o Projeto

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:

- [Java JDK 11+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- Um banco de dados (SQL Server)

### Passos

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/Felipe007983/Agendamento_de_Folga.git
   cd Agendamento_de_Folga

2 . **Configure o Banco de Dados: Altere as configurações do banco de dados no arquivo application.properties (ou application.yml), se necessário.**

3. **Compile o projeto:**

mvn clean install

4. **Execute a aplicação:**
mvn spring-boot:run

6. **Acesse a aplicação: Abra o navegador e acesse http://localhost:8080/funcionarios.**

Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.

Fork o repositório.
Crie sua feature branch (git checkout -b feature/nome-da-sua-feature).
Faça commit de suas alterações (git commit -m 'Adicionando uma nova feature').
Faça push para a branch (git push origin feature/nome-da-sua-feature).
Abra um pull request.
Licença
Este projeto está licenciado sob a MIT License.
