package com.clientes.clientes.service;

import com.clientes.clientes.dto.ClientDto;
import com.clientes.clientes.entity.Client;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientDto> findAll();

    ClientDto findById(Integer id);

    void saveClient (ClientDto clientdto);

    ClientDto updateClient (Integer id, ClientDto clientDto);

    void deleteById (Integer id);
}
