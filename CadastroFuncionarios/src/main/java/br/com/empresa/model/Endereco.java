package br.com.empresa.model;

import java.util.Objects;

public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco(String logradouro, String numero, String complemento, String bairro,
                    String cidade, String estado, String cep) {
        this.logradouro = Objects.requireNonNullElse(logradouro, "").trim();
        this.numero = Objects.requireNonNullElse(numero, "").trim();
        this.complemento = Objects.requireNonNullElse(complemento, "").trim();
        this.bairro = Objects.requireNonNullElse(bairro, "").trim();
        this.cidade = Objects.requireNonNullElse(cidade, "").trim();
        this.estado = Objects.requireNonNullElse(estado, "").trim();
        this.cep = Objects.requireNonNullElse(cep, "").trim();
    }

    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getCep() { return cep; }

    public String getEnderecoCompleto() {
        String parte = logradouro + ", " + numero;
        if (!complemento.isBlank()) parte += " - " + complemento;
        return parte + " - " + bairro + ", " + cidade + "/" + estado + " - CEP " + cep;
    }
}
