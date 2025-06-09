package com.seuprojeto.dao.factory;

import com.seuprojeto.domain.Estoque;
import com.seuprojeto.domain.Produto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EstoqueFactory {

    public static Estoque convert(ResultSet rs) throws SQLException {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getLong("ID_ESTOQUE"));
        estoque.setQuantidade(rs.getInt("QUANTIDADE"));
        estoque.setLocalizacao(rs.getString("LOCALIZACAO"));
        estoque.setDataUltimaAtualizacao(rs.getTimestamp("DATA_ULTIMA_ATUALIZACAO").toInstant());

        // O Produto associado é criado usando o ProdutoFactory
        Produto produto = ProdutoFactory.convert(rs); // Assume que o ResultSet também tem os dados do produto
        estoque.setProduto(produto);

        return estoque;
    }
}