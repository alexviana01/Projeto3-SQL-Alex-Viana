package com.seuprojeto.domain;

import com.seuprojeto.annotations.ColunaTabela;
import com.seuprojeto.annotations.Tabela;
import com.seuprojeto.annotations.TipoChave;
import com.seuprojeto.dao.Persistente;

import java.time.Instant;

@CTabela("tb_estoque")
public class Estoque implements Persistente {

    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;

    private Produto produto; // O produto associado a esta entrada de estoque

    @ColunaTabela(dbName = "quantidade", setJavaName = "setQuantidade")
    private Integer quantidade;

    @ColunaTabela(dbName = "localizacao", setJavaName = "setLocalizacao")
    private String localizacao;

    @ColunaTabela(dbName = "data_ultima_atualizacao", setJavaName = "setDataUltimaAtualizacao")
    private Instant dataUltimaAtualizacao;

    @TipoChave("getId") // A chave para consulta/exclusão será o ID do estoque
    private Long idChave; // Campo auxiliar para a anotação TipoChave

    public Estoque() {
        this.dataUltimaAtualizacao = Instant.now();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
        this.idChave = id; // Atualiza a chave auxiliar para TipoChave
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

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Instant getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Instant dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    // Métodos para manipular quantidade
    public void adicionar(Integer qtd) {
        this.quantidade += qtd;
        this.dataUltimaAtualizacao = Instant.now();
    }

    public void remover(Integer qtd) {
        if (this.quantidade >= qtd) {
            this.quantidade -= qtd;
            this.dataUltimaAtualizacao = Instant.now();
        } else {
            throw new IllegalArgumentException("Quantidade a remover é maior que a quantidade em estoque.");
        }
    }
}