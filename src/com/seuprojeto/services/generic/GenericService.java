package com.seuprojeto.services.generic;

import com.seuprojeto.dao.Persistente;
import com.seuprojeto.dao.generic.IGenericDAO;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.MaisDeUmRegistroException;
import com.seuprojeto.exceptions.TableException;
import com.seuprojeto.exceptions.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public abstract class GenericService<T extends Persistente, E extends Serializable>
        implements IGenericService<T, E> {

    protected IGenericDAO<T,E> dao;

    public GenericService(IGenericDAO<T,E> dao) {
        this.dao = dao;
    }

    @Override
    public Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
        return this.dao.cadastrar(entity);
    }

    @Override
    public void excluir(E valor) throws DAOException {
        this.dao.excluir(valor);
    }

    @Override
    public Boolean alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException {
        return this.dao.alterar(entity);
    }

    @Override
    public T consultar(E valor) throws DAOException {
        try {
            return this.dao.consultar(valor);
        } catch (MaisDeUmRegistroException | TableException e) {
            // Logar o erro e relançar ou tratar de forma mais específica
            System.err.println("Erro ao consultar: " + e.getMessage());
            throw new DAOException("Erro ao consultar objeto", e);
        }
    }

    @Override
    public Collection<T> buscarTodos() throws DAOException {
        return this.dao.buscarTodos();
    }
}