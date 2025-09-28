package com.clientes.clientes.service;

import com.clientes.clientes.dto.ClientDto;
import com.clientes.clientes.entity.Client;
import com.clientes.clientes.exceptions.BadRequestException;
import com.clientes.clientes.exceptions.ResourceNotFoundException;
import com.clientes.clientes.mapper.ClientMapper;
import com.clientes.clientes.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDto> findAll() {
        List<Client> clients = clientRepository.findAll();
            return clients.stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto findById(Integer id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No existe el cliente con el ID: "+ id));

        return clientMapper.toDto(client);
    }

    @Override
    public void saveClient(ClientDto clientDto) {

        clientRepository.findByEmail(clientDto.getEmail()).ifPresent(
                (c)-> {throw new BadRequestException("El email ingresado ya existe");}
                );

        Client client = clientMapper.toEntity(clientDto);
        clientRepository.save(client);
    }

    @Override
    public ClientDto updateClient(Integer id, ClientDto clientDto) {

        Client client = clientRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("No se encontró el usuario con ese ID")
        );

        clientRepository.findByEmail(clientDto.getEmail())
                .filter(c->!c.getId().equals(id))
                .ifPresent(c -> {throw new BadRequestException("El email ya existe");
                });

        client.setFirstName(clientDto.getFirstName());
        client.setAge(clientDto.getAge());
        client.setEmail(clientDto.getEmail());
        client.setLastName(clientDto.getLastName());

        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public void deleteById(Integer id) {

        Client client = clientRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("No existe el cliente con el ID: "+id)
        );

        clientRepository.deleteById(client.getId());
    }
}
