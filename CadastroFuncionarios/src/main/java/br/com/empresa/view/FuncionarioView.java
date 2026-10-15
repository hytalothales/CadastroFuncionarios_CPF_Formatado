package br.com.empresa.view;

import br.com.empresa.controller.FuncionarioController;
import br.com.empresa.model.Funcionario;
import br.com.empresa.service.FuncionarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FuncionarioView extends BorderPane {
    private final FuncionarioController controller;
    private final TableView<Funcionario> tabela = new TableView<>();
    private final TextField matricula = campo("6 dígitos");
    private final TextField nome = campo("Nome completo");
    private final TextField cpf = campo("000.000.000-00");
    private final DatePicker nascimento = new DatePicker(LocalDate.of(1995, 1, 1));
    private final TextField cargo = campo("Cargo");
    private final TextField salario = campo("Ex.: 2500,00");
    private final DatePicker contratacao = new DatePicker(LocalDate.now());
    private final TextField logradouro = campo("Logradouro");
    private final TextField numero = campo("Número");
    private final TextField complemento = campo("Complemento (opcional)");
    private final TextField bairro = campo("Bairro");
    private final TextField cidade = campo("Cidade");
    private final TextField estado = campo("UF");
    private final TextField cep = campo("00000-000");
    private final TextField busca = campo("Matrícula");
    private final Label status = new Label("Pronto.");

    public FuncionarioView(FuncionarioService service) {
        controller = new FuncionarioController(service);
        configurarMascaraCpf();
        setPadding(new Insets(18));
        setTop(criarCabecalho());
        TabPane abas = new TabPane(criarAbaCadastro(), criarAbaFuncionarios(), criarAbaRelatorios());
        abas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        setCenter(abas);
        setBottom(status);
        BorderPane.setMargin(status, new Insets(12, 0, 0, 0));
        configurarTabela();
    }

    // Aplica a pontuação automaticamente, aceitando apenas 11 dígitos.
    private void configurarMascaraCpf() {
        cpf.setTextFormatter(new TextFormatter<String>(alteracao -> {
            String texto = alteracao.getControlNewText();
            String digitos = texto.replaceAll("\\D", "");
            if (digitos.length() > 11) return null;

            int cursorOriginal = Math.min(alteracao.getCaretPosition(), texto.length());
            int digitosAntesCursor = texto.substring(0, cursorOriginal).replaceAll("\\D", "").length();
            String formatado = formatarCpf(digitos);

            int novoCursor = 0;
            int contagem = 0;
            while (novoCursor < formatado.length() && contagem < digitosAntesCursor) {
                if (Character.isDigit(formatado.charAt(novoCursor))) contagem++;
                novoCursor++;
            }
            if (digitosAntesCursor > 0) {
                while (novoCursor < formatado.length() && !Character.isDigit(formatado.charAt(novoCursor)))
                    novoCursor++;
            }

            alteracao.setRange(0, alteracao.getControlText().length());
            alteracao.setText(formatado);
            alteracao.setCaretPosition(novoCursor);
            alteracao.setAnchor(novoCursor);
            return alteracao;
        }));
    }

    private static String formatarCpf(String digitos) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < digitos.length(); i++) {
            if (i == 3 || i == 6) resultado.append('.');
            if (i == 9) resultado.append('-');
            resultado.append(digitos.charAt(i));
        }
        return resultado.toString();
    }

    private VBox criarCabecalho() {
        Label titulo = new Label("Gestão de Funcionários");
        titulo.setFont(Font.font(24));
        Label subtitulo = new Label("Cadastro, consulta, persistência CSV e relatórios");
        VBox box = new VBox(4, titulo, subtitulo);
        box.setPadding(new Insets(0, 0, 16, 0));
        return box;
    }

    private Tab criarAbaCadastro() {
        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10); grid.setPadding(new Insets(18));
        int r = 0;
        adicionar(grid, r++, "Matrícula", matricula, "Nome", nome);
        adicionar(grid, r++, "CPF", cpf, "Cargo", cargo);
        adicionar(grid, r++, "Data de nascimento", nascimento, "Data de contratação", contratacao);
        adicionar(grid, r++, "Salário (R$)", salario, "CEP", cep);
        adicionar(grid, r++, "Logradouro", logradouro, "Número", numero);
        adicionar(grid, r++, "Complemento", complemento, "Bairro", bairro);
        adicionar(grid, r++, "Cidade", cidade, "Estado (UF)", estado);

        Button salvar = new Button("Cadastrar funcionário");
        salvar.getStyleClass().add("primary-button");
        salvar.setOnAction(e -> cadastrar());
        Button limpar = new Button("Limpar formulário");
        limpar.setOnAction(e -> limparFormulario());
        HBox botoes = new HBox(10, salvar, limpar);
        grid.add(botoes, 0, r, 4, 1);
        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(15);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(35);
        ColumnConstraints c3 = new ColumnConstraints(); c3.setPercentWidth(15);
        ColumnConstraints c4 = new ColumnConstraints(); c4.setPercentWidth(35);
        grid.getColumnConstraints().addAll(c1, c2, c3, c4);
        return new Tab("Cadastro", new ScrollPane(grid));
    }

    private void adicionar(GridPane g, int row, String l1, Control c1, String l2, Control c2) {
        g.add(new Label(l1), 0, row); g.add(c1, 1, row);
        g.add(new Label(l2), 2, row); g.add(c2, 3, row);
        c1.setMaxWidth(Double.MAX_VALUE); c2.setMaxWidth(Double.MAX_VALUE);
    }

    private Tab criarAbaFuncionarios() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        HBox barra = new HBox(8);
        Button consultar = new Button("Consultar matrícula");
        consultar.setOnAction(e -> consultar());
        Button excluir = new Button("Excluir selecionado");
        excluir.setOnAction(e -> excluirSelecionado());
        Button atualizar = new Button("Atualizar lista");
        atualizar.setOnAction(e -> controller.atualizarLista());
        Button exportar = new Button("Exportar relatório CSV");
        exportar.setOnAction(e -> exportarRelatorio());
        barra.getChildren().addAll(new Label("Matrícula:"), busca, consultar, excluir, atualizar, exportar);
        HBox.setHgrow(busca, Priority.NEVER);
        box.getChildren().addAll(barra, tabela);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        return new Tab("Funcionários", box);
    }

    private Tab criarAbaRelatorios() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(18));
        Label intro = new Label("Relatórios calculados com Stream API");
        intro.setFont(Font.font(16));
        HBox filtroCargo = new HBox(8);
        TextField cargoFiltro = campo("Cargo exato");
        Button filtrarCargo = new Button("Filtrar por cargo");
        filtrarCargo.setOnAction(e -> mostrarFuncionarios("Filtro por cargo: " + cargoFiltro.getText(),
                controller.filtrarCargo(cargoFiltro.getText())));
        filtroCargo.getChildren().addAll(new Label("Cargo:"), cargoFiltro, filtrarCargo);

        HBox filtroSalario = new HBox(8);
        TextField minimo = campo("Mínimo");
        TextField maximo = campo("Máximo");
        Button filtrarFaixa = new Button("Filtrar faixa salarial");
        filtrarFaixa.setOnAction(e -> {
            try {
                BigDecimal min = new BigDecimal(minimo.getText().trim().replace(",", "."));
                BigDecimal max = new BigDecimal(maximo.getText().trim().replace(",", "."));
                if (min.compareTo(max) > 0) throw new IllegalArgumentException("O mínimo não pode superar o máximo.");
                mostrarFuncionarios("Faixa salarial", controller.filtrarSalario(min, max));
            } catch (NumberFormatException ex) {
                erro("Informe valores salariais válidos.");
            } catch (IllegalArgumentException ex) {
                erro(ex.getMessage());
            }
        });
        filtroSalario.getChildren().addAll(new Label("Salário de:"), minimo, new Label("até:"), maximo, filtrarFaixa);

        Button medias = new Button("Média salarial por cargo");
        medias.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("MÉDIA SALARIAL POR CARGO\n\n");
            controller.mediasPorCargo().forEach((c, v) -> sb.append(c).append(": R$ ")
                    .append(v.toPlainString()).append("\n"));
            exibirTexto("Média salarial por cargo", sb.toString());
        });
        Button cidades = new Button("Agrupar funcionários por cidade");
        cidades.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("FUNCIONÁRIOS AGRUPADOS POR CIDADE\n\n");
            controller.porCidade().forEach((cidadeNome, lista) -> {
                sb.append(cidadeNome).append(" (").append(lista.size()).append(")\n");
                lista.forEach(f -> sb.append("  • ").append(f.getNome()).append(" — ").append(f.getCargo()).append("\n"));
                sb.append("\n");
            });
            exibirTexto("Agrupamento por cidade", sb.toString());
        });
        Button folha = new Button("Total da folha salarial");
        folha.setOnAction(e -> exibirTexto("Folha salarial", "Total da folha salarial: R$ "
                + controller.totalFolha().toPlainString()));
        box.getChildren().addAll(intro, new Separator(), filtroCargo, filtroSalario,
                new HBox(10, medias, cidades, folha));
        return new Tab("Relatórios", box);
    }

    private void configurarTabela() {
        tabela.setItems(controller.getFuncionarios());
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tabela.getColumns().add(coluna("Matrícula", f -> f.getMatricula()));
        tabela.getColumns().add(coluna("Nome", f -> f.getNome()));
        tabela.getColumns().add(coluna("CPF", f -> f.getCpf()));
        tabela.getColumns().add(coluna("Cargo", f -> f.getCargo()));
        tabela.getColumns().add(coluna("Salário", f -> "R$ " + f.getSalario().toPlainString()));
        tabela.getColumns().add(coluna("Admissão", f -> f.getDataContratacao().toString()));
        tabela.getColumns().add(coluna("Cidade/UF", f -> f.getEndereco().getCidade() + "/" + f.getEndereco().getEstado()));
    }

    private TableColumn<Funcionario, String> coluna(String titulo, java.util.function.Function<Funcionario, String> getter) {
        TableColumn<Funcionario, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(data -> new SimpleStringProperty(getter.apply(data.getValue())));
        return col;
    }

    private void cadastrar() {
        try {
            if (nascimento.getValue() == null || contratacao.getValue() == null)
                throw new IllegalArgumentException("Informe as duas datas.");
            controller.cadastrar(matricula.getText(), nome.getText(), cpf.getText(), nascimento.getValue(),
                    cargo.getText(), salario.getText(), contratacao.getValue(), logradouro.getText(),
                    numero.getText(), complemento.getText(), bairro.getText(), cidade.getText(),
                    estado.getText().toUpperCase().trim(), cep.getText());
            status.setText("Funcionário cadastrado e salvo no CSV.");
            alerta("Sucesso", "Funcionário cadastrado com sucesso.");
            limparFormulario();
        } catch (Exception ex) {
            erro(ex.getMessage() == null ? "Não foi possível cadastrar." : ex.getMessage());
        }
    }

    private void consultar() {
        if (busca.getText().isBlank()) { erro("Informe a matrícula para consultar."); return; }
        Funcionario f = controller.buscar(busca.getText().trim());
        if (f == null) { erro("Nenhum funcionário encontrado com essa matrícula."); return; }
        tabela.getSelectionModel().select(f);
        tabela.scrollTo(f);
        exibirTexto("Funcionário encontrado", detalhes(f));
    }

    private String detalhes(Funcionario f) {
        return "Matrícula: " + f.getMatricula() + "\nNome: " + f.getNome() + "\nCPF: " + f.getCpf()
                + "\nNascimento: " + f.getDataNascimento() + "\nCargo: " + f.getCargo()
                + "\nSalário: R$ " + f.getSalario().toPlainString() + "\nAdmissão: " + f.getDataContratacao()
                + "\nEndereço: " + f.getEndereco().getEnderecoCompleto();
    }

    private void excluirSelecionado() {
        Funcionario f = tabela.getSelectionModel().getSelectedItem();
        String mat = f != null ? f.getMatricula() : busca.getText().trim();
        if (mat.isBlank()) { erro("Selecione um funcionário na tabela ou informe uma matrícula."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir a matrícula " + mat + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirmar exclusão");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                controller.excluir(mat);
                status.setText("Funcionário excluído.");
            } catch (Exception ex) { erro(ex.getMessage()); }
        }
    }

    private void mostrarFuncionarios(String titulo, List<Funcionario> lista) {
        StringBuilder sb = new StringBuilder(titulo + "\n\n");
        if (lista.isEmpty()) sb.append("Nenhum resultado encontrado.\n");
        else lista.forEach(f -> sb.append(f.getMatricula()).append(" | ").append(f.getNome())
                .append(" | ").append(f.getCargo()).append(" | R$ ")
                .append(f.getSalario().toPlainString()).append("\n"));
        exibirTexto(titulo, sb.toString());
    }

    private void exportarRelatorio() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exportar lista de funcionários");
        chooser.setInitialFileName("relatorio-funcionarios.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = chooser.showSaveDialog(getScene().getWindow());
        if (file == null) return;
        try (var writer = java.nio.file.Files.newBufferedWriter(file.toPath(), java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write("matricula;nome;cpf;cargo;salario;cidade;estado");
            writer.newLine();
            for (Funcionario f : controller.getFuncionarios()) {
                writer.write(String.join(";", f.getMatricula(), f.getNome(), f.getCpf(), f.getCargo(),
                        f.getSalario().toPlainString(), f.getEndereco().getCidade(), f.getEndereco().getEstado()));
                writer.newLine();
            }
            status.setText("Relatório exportado: " + file.getAbsolutePath());
        } catch (Exception ex) { erro("Erro ao exportar: " + ex.getMessage()); }
    }

    private void limparFormulario() {
        matricula.clear(); nome.clear(); cpf.clear(); cargo.clear(); salario.clear();
        logradouro.clear(); numero.clear(); complemento.clear(); bairro.clear(); cidade.clear();
        estado.clear(); cep.clear(); nascimento.setValue(LocalDate.of(1995, 1, 1)); contratacao.setValue(LocalDate.now());
    }

    private void exibirTexto(String titulo, String texto) {
        TextArea area = new TextArea(texto);
        area.setEditable(false); area.setWrapText(true); area.setPrefColumnCount(65); area.setPrefRowCount(20);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo); a.setHeaderText(null); a.getDialogPane().setContent(area); a.showAndWait();
    }

    private void alerta(String titulo, String mensagem) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, mensagem, ButtonType.OK);
        a.setTitle(titulo); a.setHeaderText(null); a.showAndWait();
    }

    private void erro(String mensagem) {
        Alert a = new Alert(Alert.AlertType.ERROR, mensagem == null ? "Erro inesperado." : mensagem, ButtonType.OK);
        a.setTitle("Atenção"); a.setHeaderText(null); a.showAndWait();
    }

    private static TextField campo(String dica) {
        TextField f = new TextField(); f.setPromptText(dica); return f;
    }
}
