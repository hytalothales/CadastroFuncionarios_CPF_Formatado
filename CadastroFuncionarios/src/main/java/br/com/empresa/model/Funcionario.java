package br.com.empresa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Funcionario {
    private final String matricula;
    private final String nome;
    private final String cpf;
    private final LocalDate dataNascimento;
    private final String cargo;
    private final BigDecimal salario;
    private final LocalDate dataContratacao;
    private final Endereco endereco;

    public Funcionario(String matricula, String nome, String cpf, LocalDate dataNascimento,
                       String cargo, BigDecimal salario, LocalDate dataContratacao, Endereco endereco) {
        this.matricula = Objects.requireNonNull(matricula);
        this.nome = Objects.requireNonNull(nome);
        this.cpf = Objects.requireNonNull(cpf);
        this.dataNascimento = Objects.requireNonNull(dataNascimento);
        this.cargo = Objects.requireNonNull(cargo);
        this.salario = Objects.requireNonNull(salario);
        this.dataContratacao = Objects.requireNonNull(dataContratacao);
        this.endereco = Objects.requireNonNull(endereco);
    }

    public String getMatricula() { return matricula; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCargo() { return cargo; }
    public BigDecimal getSalario() { return salario; }
    public LocalDate getDataContratacao() { return dataContratacao; }
    public Endereco getEndereco() { return endereco; }
}
