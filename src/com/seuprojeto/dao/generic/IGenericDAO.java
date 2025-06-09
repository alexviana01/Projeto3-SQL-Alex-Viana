package com.seuprojeto.dao.generic;

import com.seuprojeto.dao.Persistente;
import com.seuprojeto.exceptions.DAOException;
import com.seuprojeto.exceptions.MaisDeUmRegistroException;
import com.seuprojeto.exceptions.TableException;
import com.seuprojeto.exceptions.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDAO <T extends Persistente, E extends Serializable> {

    Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    void excluir(E valor) throws DAOException;

    Boolean alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException; // Retorna Boolean para indicar sucesso

    T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException;

    Collection<T> buscarTodos() throws DAOException;
}