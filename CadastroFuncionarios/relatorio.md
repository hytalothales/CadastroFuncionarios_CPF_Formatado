# Relatório do Projeto — Cadastro de Funcionários

## Objetivo
Desenvolver uma aplicação desktop em Java para gerenciar funcionários e seus endereços, usando interface JavaFX, orientação a objetos, persistência em CSV e processamento com Stream API.

## Funcionalidades implementadas
- Cadastro com matrícula, nome, CPF, datas, cargo, salário e endereço.
- Consulta por matrícula.
- Listagem dos funcionários em tabela.
- Exclusão com confirmação.
- Persistência CSV automática após cadastro e exclusão e carregamento na inicialização.
- Exportação da listagem para CSV.
- Validação de matrícula, CPF, idade mínima, salário, datas, CEP e campos obrigatórios.
- Relatórios de filtro por cargo, filtro por faixa salarial, média salarial por cargo, agrupamento por cidade e total da folha salarial.

## Decisões de design
- `Funcionario` representa os dados do funcionário e contém uma instância de `Endereco`, caracterizando composição.
- `FuncionarioRepository` isola operações de leitura e escrita do CSV.
- `FuncionarioService` centraliza validações, regras de negócio e relatórios.
- `FuncionarioController` conecta a interface às regras de negócio.
- `FuncionarioView` contém as telas JavaFX organizadas em abas.
- O salário usa `BigDecimal` para evitar imprecisão típica de ponto flutuante em valores monetários.
- Datas são representadas por `LocalDate`.
- A Stream API é usada para filtrar, mapear, somar e agrupar os registros.

## Persistência e tratamento de erros
O CSV é carregado quando a aplicação inicia e atualizado após cada cadastro ou exclusão. Erros de leitura, escrita e dados inválidos são tratados e comunicados ao usuário ou ao terminal. Linhas corrompidas não impedem a leitura das demais linhas.

## Limitações conhecidas
- Não há autenticação de usuários nem banco de dados, pois o escopo solicitado utiliza arquivo CSV.
- A validação do CPF verifica os dígitos verificadores, mas não consulta bases oficiais.
- O arquivo CSV deve ser mantido no diretório de execução da aplicação.

## Execução
Com JDK 21 e Maven instalados, executar `mvn clean javafx:run` na pasta do projeto.
