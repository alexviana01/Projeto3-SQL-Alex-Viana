package com.seuprojeto.dao.factory;

import com.seuprojeto.domain.ProdutoQuantidade;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoQuantidadeFactory {

    public static ProdutoQuantidade convert(ResultSet rs) throws SQLException {
        ProdutoQuantidade prodQ = new ProdutoQuantidade();
        prodQ.setId(rs.getLong("ID"));
        prodQ.setQuantidade(rs.getInt("QUANTIDADE"));
        prodQ.setValorTotal(rs.getBigDecimal("VALOR_TOTAL"));
        // O produto em si será setado pelo DAO que consulta ProdutoQuantidade
        // ou pelo VendaFactory que faz o join
        prodQ.setProduto(ProdutoFactory.convert(rs)); // Assume que o ResultSet também tem os dados do produto
        return prodQ;
    }
}