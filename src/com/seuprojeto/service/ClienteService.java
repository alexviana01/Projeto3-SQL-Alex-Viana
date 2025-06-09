package com.seuprojeto.service;

import com.seuprojeto.dao.IClienteDAO;
import com.seuprojeto.domain.Cliente;
import com.seuprojeto.services.generic.GenericService;

public class ClienteService extends GenericService<Cliente, Long> implements IClienteService {

    public ClienteService(IClienteDAO dao) {
        super(dao);
    }
}