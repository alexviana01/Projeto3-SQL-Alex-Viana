package com.seuprojeto.dao;

import com.seuprojeto.dao.factory.ProdutoFactory; // Importar ProdutoFactory
import com.seuprojeto.dao.factory.ProdutoQuantidadeFactory; // Importar ProdutoQuantidadeFactory
import com.seuprojeto.dao.generic.GenericDAO;
import com.seuprojeto.domain.ProdutoQuantidade;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.MaisDeUmRegistroException;
import com.seuprojeto.exceptions.TableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProdutoQuantidadeDAO extends GenericDAO<ProdutoQuantidade, Long> implements IProdutoQuantidadeDAO {

    @Override
    public Class<ProdutoQuantidade> getTipoClasse() {
        return ProdutoQuantidade.class;
    }

    @Override
    public void atualiarDados(ProdutoQuantidade entity, ProdutoQuantidade entityCadastrado) {
        entityCadastrado.setQuantidade(entity.getQuantidade());
        entityCadastrado.setValorTotal(entity.getValorTotal());
        // Produto não é atualizado diretamente aqui, pois é uma FK
    }

    @Override
    protected String getQueryInsercao() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_PRODUTO_QUANTIDADE ");
        sb.append("(ID, ID_PRODUTO_FK, ID_VENDA_FK, QUANTIDADE, VALOR_TOTAL)");
        sb.append("VALUES (nextval('sq_produto_quantidade'),?,?,?,?)");
        return sb.toString();
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, ProdutoQuantidade entity) throws SQLException {
        // Este método é intencionalmente deixado para lançar uma exceção
        // porque a inserção de ProdutoQuantidade é gerenciada dentro do VendaDAO
        // para garantir a integridade transacional.
        throw new UnsupportedOperationException("Este método de inserção não deve ser chamado diretamente. A inserção de ProdutoQuantidade é gerenciada pelo VendaDAO.");
    }

    @Override
    protected String getQueryExclusao() {
        return "DELETE FROM TB_PRODUTO_QUANTIDADE WHERE ID = ?";
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmExclusao, Long valor) throws SQLException {
        stmExclusao.setLong(1, valor);
    }

    @Override
    protected String getQueryAtualizacao() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_PRODUTO_QUANTIDADE ");
        sb.append("SET QUANTIDADE = ?,");
        sb.append("VALOR_TOTAL = ?");
        sb.append(" WHERE ID = ?");
        return sb.toString();
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, ProdutoQuantidade entity) throws SQLException {
        stmUpdate.setInt(1, entity.getQuantidade());
        stmUpdate.setBigDecimal(2, entity.getValorTotal());
        stmUpdate.setLong(3, entity.getId());
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stmSelect, Long valor) throws SQLException {
        stmSelect.setLong(1, valor);
    }

    // SOBRESCRITA DO MÉTODO CONSULTAR PARA CARREGAR O PRODUTO ASSOCIADO
    @Override
    public ProdutoQuantidade consultar(Long valor) throws MaisDeUmRegistroException, TableException, DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PQ.ID, PQ.QUANTIDADE, PQ.VALOR_TOTAL, ");
        sb.append("P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR, P.CATEGORIA "); // Incluir CATEGORIA
        sb.append("FROM TB_PRODUTO_QUANTIDADE PQ ");
        sb.append("INNER JOIN TB_PRODUTO P ON PQ.ID_PRODUTO_FK = P.ID ");
        sb.append("WHERE PQ.ID = ?"); // Consulta pelo ID da ProdutoQuantidade

        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            validarMaisDeUmRegistro(valor); // Valida se há mais de um registro com o mesmo ID
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            setParametrosQuerySelect(stm, valor); // Usa o ID passado como parâmetro
            rs = stm.executeQuery();
            if (rs.next()) {
                // Usa o ProdutoQuantidadeFactory para converter o ResultSet em objeto
                return ProdutoQuantidadeFactory.convert(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO CONSULTANDO OBJETO PRODUTO_QUANTIDADE ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return null;
    }

    // SOBRESCRITA DO MÉTODO BUSCAR TODOS PARA CARREGAR OS PRODUTOS ASSOCIADOS
    @Override
    public Collection<ProdutoQuantidade> buscarTodos() throws DAOException {
        List<ProdutoQuantidade> lista = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PQ.ID, PQ.QUANTIDADE, PQ.VALOR_TOTAL, ");
        sb.append("P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR, P.CATEGORIA "); // Incluir CATEGORIA
        sb.append("FROM TB_PRODUTO_QUANTIDADE PQ ");
        sb.append("INNER JOIN TB_PRODUTO P ON PQ.ID_PRODUTO_FK = P.ID ");

        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                // Usa o ProdutoQuantidadeFactory para converter o ResultSet em objeto
                lista.add(ProdutoQuantidadeFactory.convert(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO LISTANDO OBJETOS PRODUTO_QUANTIDADE ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return lista;
    }
}