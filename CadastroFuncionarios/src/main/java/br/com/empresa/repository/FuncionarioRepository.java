package br.com.empresa.repository;

import br.com.empresa.model.Endereco;
import br.com.empresa.model.Funcionario;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FuncionarioRepository {
    private final Path arquivo;

    public FuncionarioRepository(Path arquivo) {
        this.arquivo = arquivo;
    }

    public List<Funcionario> carregar() throws IOException {
        List<Funcionario> lista = new ArrayList<>();
        if (!Files.exists(arquivo)) {
            if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
            Files.writeString(arquivo, cabecalho() + System.lineSeparator(), StandardCharsets.UTF_8);
            return lista;
        }
        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (linha.isBlank() || (i == 0 && linha.startsWith("matricula;"))) continue;
            try {
                List<String> c = decodificarLinha(linha);
                if (c.size() != 15) continue;
                Endereco e = new Endereco(c.get(8), c.get(9), c.get(10), c.get(11),
                        c.get(12), c.get(13), c.get(14));
                lista.add(new Funcionario(c.get(0), c.get(1), c.get(2), LocalDate.parse(c.get(3)),
                        c.get(4), new BigDecimal(c.get(5)), LocalDate.parse(c.get(6)), e));
            } catch (RuntimeException ex) {
                System.err.println("Linha CSV ignorada (" + (i + 1) + "): " + ex.getMessage());
            }
        }
        return lista;
    }

    public void salvar(List<Funcionario> funcionarios) throws IOException {
        if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
        List<String> linhas = new ArrayList<>();
        linhas.add(cabecalho());
        for (Funcionario f : funcionarios) {
            Endereco e = f.getEndereco();
            linhas.add(List.of(f.getMatricula(), f.getNome(), f.getCpf(),
                    f.getDataNascimento().toString(), f.getCargo(), f.getSalario().toPlainString(),
                    f.getDataContratacao().toString(), "", e.getLogradouro(), e.getNumero(),
                    e.getComplemento(), e.getBairro(), e.getCidade(), e.getEstado(), e.getCep())
                    .stream().map(this::escapar).collect(Collectors.joining(";")));
        }
        Files.write(arquivo, linhas, StandardCharsets.UTF_8);
    }

    private String cabecalho() {
        return "matricula;nome;cpf;dataNascimento;cargo;salario;dataContratacao;reservado;logradouro;numero;complemento;bairro;cidade;estado;cep";
    }

    private String escapar(String valor) {
        String v = valor == null ? "" : valor;
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }

    private List<String> decodificarLinha(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean aspas = false;
        for (int i = 0; i < linha.length(); i++) {
            char ch = linha.charAt(i);
            if (ch == '"') {
                if (aspas && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    atual.append('"'); i++;
                } else aspas = !aspas;
            } else if (ch == ';' && !aspas) {
                campos.add(atual.toString()); atual.setLength(0);
            } else atual.append(ch);
        }
        campos.add(atual.toString());
        return campos;
    }
}
