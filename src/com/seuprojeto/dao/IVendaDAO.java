package com.seuprojeto.dao;

import com.seuprojeto.dao.generic.IGenericDAO;
import com.seuprojeto.domain.Venda;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.TipoChaveNaoEncontradaException;

public interface IVendaDAO extends IGenericDAO<Venda, String> {
    void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
    void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
}