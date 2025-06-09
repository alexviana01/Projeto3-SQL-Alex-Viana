package com.seuprojeto.service;

import com.seuprojeto.dao.IProdutoDAO;
import com.seuprojeto.domain.Produto;
import com.seuprojeto.services.generic.GenericService;

public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {

    public ProdutoService(IProdutoDAO dao) {
        super(dao);
    }
}