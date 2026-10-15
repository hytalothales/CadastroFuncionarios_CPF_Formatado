package br.com.empresa.controller;

import br.com.empresa.model.Endereco;
import br.com.empresa.model.Funcionario;
import br.com.empresa.service.FuncionarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FuncionarioController {
    private final FuncionarioService service;
    private final ObservableList<Funcionario> funcionarios = FXCollections.observableArrayList();

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
        atualizarLista();
    }

    public ObservableList<Funcionario> getFuncionarios() { return funcionarios; }

    public void cadastrar(String matricula, String nome, String cpf, LocalDate nascimento,
                          String cargo, String salario, LocalDate contratacao,
                          String logradouro, String numero, String complemento, String bairro,
                          String cidade, String estado, String cep) {
        BigDecimal valor;
        try {
            valor = new BigDecimal(salario.trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Salário inválido. Exemplo: 2500,00.");
        }
        Endereco endereco = new Endereco(logradouro, numero, complemento, bairro, cidade, estado, cep);
        Funcionario f = new Funcionario(matricula.trim(), nome.trim(), cpf.trim(), nascimento,
                cargo.trim(), valor, contratacao, endereco);
        service.cadastrar(f);
        atualizarLista();
    }

    public Funcionario buscar(String matricula) {
        return service.buscarPorMatricula(matricula.trim());
    }

    public void excluir(String matricula) {
        service.excluir(matricula.trim());
        atualizarLista();
    }

    public void atualizarLista() {
        funcionarios.setAll(service.listar());
    }

    public List<Funcionario> filtrarCargo(String cargo) { return service.filtrarPorCargo(cargo); }
    public List<Funcionario> filtrarSalario(BigDecimal min, BigDecimal max) {
        return service.filtrarPorFaixaSalarial(min, max);
    }
    public java.util.Map<String, BigDecimal> mediasPorCargo() { return service.mediaSalarialPorCargo(); }
    public java.util.Map<String, List<Funcionario>> porCidade() { return service.agruparPorCidade(); }
    public BigDecimal totalFolha() { return service.totalFolhaSalarial(); }
}
