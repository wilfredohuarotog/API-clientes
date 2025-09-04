package com.clientes.clientes.mapper;


import com.clientes.clientes.dto.ClientDto;
import com.clientes.clientes.entity.Client;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final ModelMapper modelMapper;

    public ClientDto toDto(Client client){
         ClientDto clientDto = modelMapper.map(client,ClientDto.class);
        return clientDto;
    }

    public Client toEntity(ClientDto clientDto){
        Client client = modelMapper.map(clientDto,Client.class);
        return client;
    }


}
