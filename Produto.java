package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Produto {
    private int id;
    private String descricao;
    private float preco;
    private int quantidade;
    private LocalDateTime dataFabricacao;
    private LocalDate dataValidade;

    // Default constructor
    public Produto() {
        this.id = -1;
        this.descricao = "";
        this.preco = 0.00F;
        this.quantidade = 0;
        this.dataFabricacao = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.dataValidade = LocalDate.now().plusMonths(6);
    }

    // Parameterized constructor
    public Produto(int id, String descricao, float preco, int quantidade, 
                   LocalDateTime dataFabricacao, LocalDate dataValidade) {
        this.setId(id);
        this.setDescricao(descricao);
        this.setPreco(preco);
        this.setQuantidade(quantidade);
        this.setDataFabricacao(dataFabricacao);
        this.setDataValidade(dataValidade);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDateTime dataFabricacao) {
        LocalDateTime agora = LocalDateTime.now();
        // Ensure that the fabrication date is not in the future
        if (agora.isAfter(dataFabricacao) || agora.isEqual(dataFabricacao)) {
            this.dataFabricacao = dataFabricacao;
        }
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        // Ensure that the expiration date is after the fabrication date
        if (dataFabricacao.isBefore(dataValidade.atStartOfDay())) {
            this.dataValidade = dataValidade;
        }
    }

    // Utility methods
    public boolean emValidade() {
        return LocalDateTime.now().isBefore(this.dataValidade.atTime(23, 59));
    }

    // Override toString method
    @Override
    public String toString() {
        return String.format("Produto: %s   Preço: R$%.2f   Quantidade: %d   Fabricação: %s   Data de Validade: %s",
                descricao, preco, quantidade, dataFabricacao, dataValidade);
    }

    // Override equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return id == produto.id;
    }
}
