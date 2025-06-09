package com.seuprojeto.dao;

import com.seuprojeto.dao.factory.EstoqueFactory;
import com.seuprojeto.dao.factory.ProdutoFactory;
import com.seuprojeto.dao.generic.GenericDAO;
import com.seuprojeto.domain.Estoque;
import com.seuprojeto.domain.Produto;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.MaisDeUmRegistroException;
import com.seuprojeto.exceptions.TableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EstoqueDAO extends GenericDAO<Estoque, Long> implements IEstoqueDAO {

    @Override
    public Class<Estoque> getTipoClasse() {
        return Estoque.class;
    }

    @Override
    public void atualiarDados(Estoque entity, Estoque entityCadastrado) {
        entityCadastrado.setQuantidade(entity.getQuantidade());
        entityCadastrado.setLocalizacao(entity.getLocalizacao());
        entityCadastrado.setDataUltimaAtualizacao(entity.getDataUltimaAtualizacao());
        // O produto não é atualizado diretamente aqui, pois é uma FK.
        // Se o produto associado mudar, seria uma nova entrada de estoque ou uma exclusão/inserção.
    }

    @Override
    protected String getQueryInsercao() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_ESTOQUE ");
        sb.append("(ID, ID_PRODUTO_FK, QUANTIDADE, LOCALIZACAO, DATA_ULTIMA_ATUALIZACAO)");
        sb.append("VALUES (nextval('sq_estoque'),?,?,?,?)");
        return sb.toString();
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Estoque entity) throws SQLException {
        stmInsert.setLong(1, entity.getProduto().getId()); // ID do produto
        stmInsert.setInt(2, entity.getQuantidade());
        stmInsert.setString(3, entity.getLocalizacao());
        stmInsert.setTimestamp(4, Timestamp.from(entity.getDataUltimaAtualizacao()));
    }

    @Override
    protected String getQueryExclusao() {
        return "DELETE FROM TB_ESTOQUE WHERE ID = ?";
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmExclusao, Long valor) throws SQLException {
        stmExclusao.setLong(1, valor);
    }

    @Override
    protected String getQueryAtualizacao() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_ESTOQUE ");
        sb.append("SET QUANTIDADE = ?,");
        sb.append("LOCALIZACAO = ?,");
        sb.append("DATA_ULTIMA_ATUALIZACAO = ?");
        sb.append(" WHERE ID = ?");
        return sb.toString();
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Estoque entity) throws SQLException {
        stmUpdate.setInt(1, entity.getQuantidade());
        stmUpdate.setString(2, entity.getLocalizacao());
        stmUpdate.setTimestamp(3, Timestamp.from(entity.getDataUltimaAtualizacao()));
        stmUpdate.setLong(4, entity.getId());
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stmSelect, Long valor) throws SQLException {
        stmSelect.setLong(1, valor);
    }

    // Sobrescrevendo consultar para buscar o Produto associado
    @Override
    public Estoque consultar(Long valor) throws MaisDeUmRegistroException, TableException, DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT E.ID AS ID_ESTOQUE, E.QUANTIDADE, E.LOCALIZACAO, E.DATA_ULTIMA_ATUALIZACAO, ");
        sb.append("P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR, P.CATEGORIA "); // Incluir CATEGORIA
        sb.append("FROM TB_ESTOQUE E ");
        sb.append("INNER JOIN TB_PRODUTO P ON E.ID_PRODUTO_FK = P.ID ");
        sb.append("WHERE E.ID = ?");

        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            setParametrosQuerySelect(stm, valor);
            rs = stm.executeQuery();
            if (rs.next()) {
                return EstoqueFactory.convert(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO CONSULTANDO OBJETO ESTOQUE ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return null;
    }

    // Sobrescrevendo buscarTodos para buscar os Produtos associados
    @Override
    public Collection<Estoque> buscarTodos() throws DAOException {
        List<Estoque> lista = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT E.ID AS ID_ESTOQUE, E.QUANTIDADE, E.LOCALIZACAO, E.DATA_ULTIMA_ATUALIZACAO, ");
        sb.append("P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR, P.CATEGORIA "); // Incluir CATEGORIA
        sb.append("FROM TB_ESTOQUE E ");
        sb.append("INNER JOIN TB_PRODUTO P ON E.ID_PRODUTO_FK = P.ID ");

        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                lista.add(EstoqueFactory.convert(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO LISTANDO OBJETOS ESTOQUE ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return lista;
    }
}