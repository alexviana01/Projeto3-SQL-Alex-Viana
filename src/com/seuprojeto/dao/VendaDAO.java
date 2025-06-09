package com.seuprojeto.dao;

import com.seuprojeto.dao.factory.ClienteFactory;
import com.seuprojeto.dao.factory.ProdutoQuantidadeFactory;
import com.seuprojeto.dao.factory.VendaFactory;
import com.seuprojeto.dao.generic.GenericDAO;
import com.seuprojeto.domain.ProdutoQuantidade;
import com.seuprojeto.domain.Venda;
import com.seuprojeto.domain.Venda.Status;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.MaisDeUmRegistroException;
import com.seuprojeto.exceptions.TableException;
import com.seuprojeto.exceptions.TipoChaveNaoEncontradaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VendaDAO extends GenericDAO<Venda, String> implements IVendaDAO {

    @Override
    public Class<Venda> getTipoClasse() {
        return Venda.class;
    }

    @Override
    public void atualiarDados(Venda entity, Venda entityCadastrado) {
        entityCadastrado.setCodigo(entity.getCodigo());
        entityCadastrado.setStatus(entity.getStatus());
        entityCadastrado.setValorTotal(entity.getValorTotal());
        entityCadastrado.setProdutos(entity.getProdutos()); // Pode ser problemático se não for um merge
        // Para vendas, a atualização é mais complexa, geralmente se atualiza status e não itens.
    }

    @Override
    public void excluir(String valor) {
        throw new UnsupportedOperationException("OPERAÇÃO DE EXCLUSÃO NÃO PERMITIDA PARA VENDAS");
    }

    @Override
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE TB_VENDA SET STATUS_VENDA = ? WHERE ID = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Status.CONCLUIDA.name());
            stm.setLong(2, venda.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO ATUALIZANDO STATUS DA VENDA ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE TB_VENDA SET STATUS_VENDA = ? WHERE ID = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Status.CANCELADA.name());
            stm.setLong(2, venda.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO ATUALIZANDO STATUS DA VENDA ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    protected String getQueryInsercao() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_VENDA ");
        sb.append("(ID, CODIGO, ID_CLIENTE_FK, VALOR_TOTAL, DATA_VENDA, STATUS_VENDA)");
        sb.append("VALUES (nextval('sq_venda'),?,?,?,?,?)");
        return sb.toString();
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Venda entity) throws SQLException {
        stmInsert.setString(1, entity.getCodigo());
        stmInsert.setLong(2, entity.getCliente().getId());
        stmInsert.setBigDecimal(3, entity.getValorTotal());
        stmInsert.setTimestamp(4, Timestamp.from(entity.getDataVenda()));
        stmInsert.setString(5, entity.getStatus().name());
    }

    @Override
    protected String getQueryExclusao() {
        throw new UnsupportedOperationException("OPERAÇÃO DE EXCLUSÃO NÃO PERMITIDA PARA VENDAS");
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmInsert, String valor) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO DE EXCLUSÃO NÃO PERMITIDA PARA VENDAS");
    }

    @Override
    protected String getQueryAtualizacao() {
        // A atualização de vendas é feita por métodos específicos (finalizar/cancelar)
        throw new UnsupportedOperationException("OPERAÇÃO DE ATUALIZAÇÃO GENÉRICA NÃO PERMITIDA PARA VENDAS");
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Venda entity) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO DE ATUALIZAÇÃO GENÉRICA NÃO PERMITIDA PARA VENDAS");
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stm, String valor) throws SQLException {
        stm.setString(1, valor);
    }

    @Override
    public Venda consultar(String valor) throws MaisDeUmRegistroException, TableException, DAOException {
        StringBuilder sb = sqlBaseSelect();
        sb.append("WHERE V.CODIGO = ? ");
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            validarMaisDeUmRegistro(valor); // Valida se há mais de um registro com o mesmo código
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            setParametrosQuerySelect(stm, valor);
            rs = stm.executeQuery();
            if (rs.next()) {
                Venda venda = VendaFactory.convert(rs);
                buscarAssociacaoVendaProdutos(connection, venda);
                return venda;
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO CONSULTANDO OBJETO VENDA ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return null;
    }

    private void buscarAssociacaoVendaProdutos(Connection connection, Venda venda) throws DAOException {
        PreparedStatement stmProd = null;
        ResultSet rsProd = null;
        try {
            StringBuilder sbProd = new StringBuilder();
            sbProd.append("SELECT PQ.ID, PQ.QUANTIDADE, PQ.VALOR_TOTAL, ");
            sbProd.append("P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR, P.CATEGORIA "); // Incluir CATEGORIA
            sbProd.append("FROM TB_PRODUTO_QUANTIDADE PQ ");
            sbProd.append("INNER JOIN TB_PRODUTO P ON P.ID = PQ.ID_PRODUTO_FK ");
            sbProd.append("WHERE PQ.ID_VENDA_FK = ?");
            stmProd = connection.prepareStatement(sbProd.toString());
            stmProd.setLong(1, venda.getId());
            rsProd = stmProd.executeQuery();
            Set<ProdutoQuantidade> produtos = new HashSet<>();
            while(rsProd.next()) {
                ProdutoQuantidade prodQ = ProdutoQuantidadeFactory.convert(rsProd);
                produtos.add(prodQ);
            }
            venda.setProdutos(produtos);
            venda.recalcularValorTotalVenda();
        } catch (SQLException e) {
            throw new DAOException("ERRO BUSCANDO PRODUTOS DA VENDA ", e);
        } finally {
            closeConnection(null, stmProd, rsProd); // Não fechar a conexão principal aqui
        }
    }

    @Override
    public Collection<Venda> buscarTodos() throws DAOException {
        List<Venda> lista = new ArrayList<>();
        StringBuilder sb = sqlBaseSelect();
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                Venda venda = VendaFactory.convert(rs);
                buscarAssociacaoVendaProdutos(connection, venda);
                lista.add(venda);
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO LISTANDO OBJETOS VENDA ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return lista;
    }

    private StringBuilder sqlBaseSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT V.ID AS ID_VENDA, V.CODIGO, V.VALOR_TOTAL, V.DATA_VENDA, V.STATUS_VENDA, ");
        sb.append("C.ID AS ID_CLIENTE, C.NOME, C.CPF, C.TEL, C.ENDERECO, C.NUMERO, C.CIDADE, C.ESTADO, C.EMAIL "); // Incluir EMAIL
        sb.append("FROM TB_VENDA V ");
        sb.append("INNER JOIN TB_CLIENTE C ON V.ID_CLIENTE_FK = C.ID ");
        return sb;
    }

    @Override
    public Boolean cadastrar(Venda entity) throws TipoChaveNaoEncontradaException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Inicia transação

            // 1. Inserir Venda
            stm = connection.prepareStatement(getQueryInsercao(), Statement.RETURN_GENERATED_KEYS);
            setParametrosQueryInsercao(stm, entity);
            int rowsAffectedVenda = stm.executeUpdate();

            if(rowsAffectedVenda > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()){
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }

                // 2. Inserir Produtos da Venda (TB_PRODUTO_QUANTIDADE)
                String queryInsercaoProdQuant = getQueryInsercaoProdQuant();
                for (ProdutoQuantidade prod : entity.getProdutos()) {
                    stm = connection.prepareStatement(queryInsercaoProdQuant);
                    setParametrosQueryInsercaoProdQuant(stm, entity, prod);
                    stm.executeUpdate();
                }
                connection.commit(); // Confirma transação
                return true;
            }
            connection.rollback(); // Em caso de falha na venda
            return false;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback em caso de erro
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            throw new DAOException("ERRO CADASTRANDO VENDA E PRODUTOS ", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Volta ao modo auto-commit
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao resetar auto-commit: " + ex.getMessage());
            }
            closeConnection(connection, stm, null);
        }
    }

    private String getQueryInsercaoProdQuant() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_PRODUTO_QUANTIDADE ");
        sb.append("(ID, ID_PRODUTO_FK, ID_VENDA_FK, QUANTIDADE, VALOR_TOTAL)");
        sb.append("VALUES (nextval('sq_produto_quantidade'),?,?,?,?)");
        return sb.toString();
    }

    private void setParametrosQueryInsercaoProdQuant(PreparedStatement stm, Venda venda, ProdutoQuantidade prod) throws SQLException {
        stm.setLong(1, prod.getProduto().getId());
        stm.setLong(2, venda.getId());
        stm.setInt(3, prod.getQuantidade());
        stm.setBigDecimal(4, prod.getValorTotal());
    }
}