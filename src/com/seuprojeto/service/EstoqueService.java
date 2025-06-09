package com.seuprojeto.service;

import com.seuprojeto.dao.IEstoqueDAO;
import com.seuprojeto.domain.Estoque;
import com.seuprojeto.services.generic.GenericService;

public class EstoqueService extends GenericService<Estoque, Long> implements IEstoqueService {

    public EstoqueService(IEstoqueDAO dao) {
        super(dao);
    }
}