# Sistema de Cadastro de Funcionários

Aplicação desktop desenvolvida em Java 21 com JavaFX para demonstrar
programação orientada a objetos, validação de dados, persistência em CSV
e processamento de coleções com a Stream API.

## Funcionalidades

- Cadastro de funcionários e endereços.
- Consulta de funcionários pela matrícula.
- Exclusão de funcionários.
- Validação de CPF, matrícula, idade, salário e CEP.
- Persistência dos dados em arquivo CSV separado por ponto e vírgula.
- Exportação de dados.
- Relatórios por cargo e faixa salarial.
- Cálculo da média salarial por cargo.
- Agrupamento de funcionários por cidade.
- Cálculo do total da folha salarial.

## Tecnologias utilizadas

- Java 21
- JavaFX
- Stream API
- CSV
- IntelliJ IDEA

## Organização do programa

- Main: inicializa a aplicação.
- Model: representa os funcionários e seus endereços.
- View: apresenta a interface gráfica.
- Controller: recebe as ações do usuário.
- Service: executa regras de negócio e validações.
- Repository: realiza a leitura e a gravação dos dados CSV.

## Interface gráfica

### Aba Cadastro
Permite preencher os dados pessoais, profissionais e de endereço
do funcionário e realizar o cadastro.

### Aba Funcionários
Apresenta os funcionários cadastrados e permite consultar,
excluir, atualizar a listagem e exportar os dados.

### Aba Relatórios
Apresenta filtros por cargo e faixa salarial, média salarial,
agrupamento por cidade e total da folha salarial.

## Como executar

1. Instale o JDK 21.
2. Configure o JavaFX compatível com o JDK.
3. Abra o projeto no IntelliJ IDEA.
4. Configure a classe principal br.com.empresa.Main.
5. Configure as VM options com o caminho da pasta lib do JavaFX:

--module-path "CAMINHO_DO_JAVAFX/lib" --add-modules javafx.controls,javafx.fxml

6. Execute a aplicação pela opção Run.

## Persistência dos dados

Os funcionários são armazenados em arquivo CSV, utilizando ponto e
vírgula (;) como separador dos campos.

## Stream API

A Stream API é utilizada para filtrar, agrupar e processar os dados
dos funcionários, permitindo gerar relatórios e calcular valores
como a média salarial e o total da folha de pagamento.

## Objetivo acadêmico

O projeto demonstra a integração entre interface gráfica, orientação
a objetos, validação de dados, manipulação de arquivos e processamento
de coleções em Java.