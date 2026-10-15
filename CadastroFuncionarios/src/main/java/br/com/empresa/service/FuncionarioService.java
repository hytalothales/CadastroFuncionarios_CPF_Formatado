package br.com.empresa.service;

import br.com.empresa.model.Funcionario;
import br.com.empresa.repository.FuncionarioRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FuncionarioService {
    private final FuncionarioRepository repository;
    private final java.util.ArrayList<Funcionario> funcionarios = new java.util.ArrayList<>();

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
        try {
            funcionarios.addAll(repository.carregar());
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar o arquivo CSV: " + e.getMessage(), e);
        }
    }

    public List<Funcionario> listar() {
        return funcionarios.stream().sorted(Comparator.comparing(Funcionario::getNome)).toList();
    }

    public Funcionario buscarPorMatricula(String matricula) {
        return funcionarios.stream().filter(f -> f.getMatricula().equals(matricula)).findFirst().orElse(null);
    }

    public void cadastrar(Funcionario funcionario) {
        Validador.validarFuncionario(funcionario);
        if (buscarPorMatricula(funcionario.getMatricula()) != null)
            throw new IllegalArgumentException("Já existe funcionário com essa matrícula.");
        if (funcionarios.stream().anyMatch(f -> f.getCpf().replaceAll("\\D", "")
                .equals(funcionario.getCpf().replaceAll("\\D", ""))))
            throw new IllegalArgumentException("Já existe funcionário com esse CPF.");
        funcionarios.add(funcionario);
        persistirOuReverter(funcionario);
    }

    public void excluir(String matricula) {
        Funcionario existente = buscarPorMatricula(matricula);
        if (existente == null) throw new IllegalArgumentException("Matrícula não encontrada.");
        funcionarios.remove(existente);
        try {
            repository.salvar(funcionarios);
        } catch (IOException e) {
            funcionarios.add(existente);
            throw new IllegalStateException("Falha ao salvar CSV: " + e.getMessage(), e);
        }
    }

    private void persistirOuReverter(Funcionario funcionario) {
        try {
            repository.salvar(funcionarios);
        } catch (IOException e) {
            funcionarios.remove(funcionario);
            throw new IllegalStateException("Falha ao salvar CSV: " + e.getMessage(), e);
        }
    }

    public List<Funcionario> filtrarPorCargo(String cargo) {
        return funcionarios.stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo.trim()))
                .sorted(Comparator.comparing(Funcionario::getNome)).toList();
    }

    public List<Funcionario> filtrarPorFaixaSalarial(BigDecimal minimo, BigDecimal maximo) {
        return funcionarios.stream()
                .filter(f -> f.getSalario().compareTo(minimo) >= 0 && f.getSalario().compareTo(maximo) <= 0)
                .sorted(Comparator.comparing(Funcionario::getSalario)).toList();
    }

    public Map<String, BigDecimal> mediaSalarialPorCargo() {
        return funcionarios.stream().collect(Collectors.groupingBy(Funcionario::getCargo,
                Collectors.averagingDouble(f -> f.getSalario().doubleValue())))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue()).setScale(2, java.math.RoundingMode.HALF_UP)));
    }

    public Map<String, List<Funcionario>> agruparPorCidade() {
        return funcionarios.stream().collect(Collectors.groupingBy(f -> f.getEndereco().getCidade(),
                Collectors.toList()));
    }

    public BigDecimal totalFolhaSalarial() {
        return funcionarios.stream().map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
