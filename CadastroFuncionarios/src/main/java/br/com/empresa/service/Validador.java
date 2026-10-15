package br.com.empresa.service;

import br.com.empresa.model.Endereco;
import br.com.empresa.model.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public final class Validador {
    private Validador() {}

    public static void validarFuncionario(Funcionario f) {
        if (f.getMatricula() == null || !f.getMatricula().matches("\\d{6}"))
            throw new IllegalArgumentException("A matrícula deve conter exatamente 6 dígitos numéricos.");
        if (f.getNome() == null || f.getNome().trim().length() < 3)
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        if (!cpfValido(f.getCpf()))
            throw new IllegalArgumentException("CPF inválido. Confira os 11 dígitos.");
        if (f.getDataNascimento().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("A data de nascimento não pode estar no futuro.");
        if (Period.between(f.getDataNascimento(), LocalDate.now()).getYears() < 18)
            throw new IllegalArgumentException("O funcionário deve ter pelo menos 18 anos.");
        if (f.getCargo() == null || f.getCargo().isBlank())
            throw new IllegalArgumentException("Informe o cargo.");
        if (f.getSalario().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O salário deve ser maior que zero.");
        if (f.getDataContratacao().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("A data de contratação não pode estar no futuro.");
        Endereco e = f.getEndereco();
        if (e.getLogradouro().isBlank() || e.getNumero().isBlank() || e.getBairro().isBlank()
                || e.getCidade().isBlank() || e.getEstado().isBlank())
            throw new IllegalArgumentException("Preencha logradouro, número, bairro, cidade e estado.");
        if (!e.getCep().matches("\\d{5}-?\\d{3}"))
            throw new IllegalArgumentException("CEP inválido. Use 00000-000 ou 00000000.");
        if (e.getEstado().length() != 2)
            throw new IllegalArgumentException("O estado deve ser informado pela sigla de 2 letras.");
    }

    public static boolean cpfValido(String cpfInformado) {
        if (cpfInformado == null) return false;
        String cpf = cpfInformado.replaceAll("\\D", "");
        if (!cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) return false;
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
            int resto = (soma * 10) % 11;
            int digito1 = resto == 10 ? 0 : resto;
            if (digito1 != cpf.charAt(9) - '0') return false;
            soma = 0;
            for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
            resto = (soma * 10) % 11;
            int digito2 = resto == 10 ? 0 : resto;
            return digito2 == cpf.charAt(10) - '0';
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
