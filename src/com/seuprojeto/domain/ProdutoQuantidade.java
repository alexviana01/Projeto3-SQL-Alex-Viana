package com.seuprojeto.domain;

import com.seuprojeto.annotations.ColunaTabela;
import com.seuprojeto.annotations.Tabela;
import com.seuprojeto.annotations.TipoChave; // Importar TipoChave

import java.math.BigDecimal;

@Tabela("TB_PRODUTO_QUANTIDADE")
public class ProdutoQuantidade {

    @TipoChave("getId") // CORRIGIDO: Adicionado esta anotação
    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;

    private Produto produto; // Não é ColunaTabela, é um objeto complexo

    @ColunaTabela(dbName = "quantidade", setJavaName = "setQuantidade")
    private Integer quantidade;

    @ColunaTabela(dbName = "valor_total", setJavaName = "setValorTotal")
    private BigDecimal valorTotal;

    public ProdutoQuantidade() {
        this.quantidade = 0;
        this.valorTotal = BigDecimal.ZERO;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void adicionar(Integer quantidade) {
        this.quantidade += quantidade;
        BigDecimal novoValor = this.produto.getValor().multiply(BigDecimal.valueOf(quantidade));
        BigDecimal novoTotal = this.valorTotal.add(novoValor);
        this.valorTotal = novoTotal;
    }

    public void remover(Integer quantidade) {
        if (this.quantidade >= quantidade) {
            this.quantidade -= quantidade;
            BigDecimal novoValor = this.produto.getValor().multiply(BigDecimal.valueOf(quantidade));
            this.valorTotal = this.valorTotal.subtract(novoValor);
        } else {
            this.quantidade = 0;
            this.valorTotal = BigDecimal.ZERO;
        }
    }
}