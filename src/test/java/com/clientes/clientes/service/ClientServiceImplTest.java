package com.clientes.clientes.service;

import com.clientes.clientes.dto.ClientDto;
import com.clientes.clientes.entity.Client;
import com.clientes.clientes.exceptions.BadRequestException;
import com.clientes.clientes.exceptions.ResourceNotFoundException;
import com.clientes.clientes.mapper.ClientMapper;
import com.clientes.clientes.repository.ClientRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client1;
    private ClientDto clientDto1;


    @BeforeEach
    void setInit() {

        client1 = Client.builder()
                .id(1)
                .firstName("Wilfredo")
                .lastName("Huaroto")
                .age(26)
                .email("will@gmail.com")
                .build();

        clientDto1 = ClientDto.builder()
                .id(1)
                .firstName("Wilfredo")
                .lastName("Huaroto")
                .age(26)
                .email("will@gmail.com")
                .build();
    }

    @Test
    void findAllTest() {

        //Given
        Client client2 = Client.builder()
                .id(2)
                .firstName("Rocio")
                .lastName("Mendoza")
                .age(24)
                .email("roci@gmail.com")
                .build();

        ClientDto clientDto2 = ClientDto.builder()
                .id(2)
                .firstName("Rocio")
                .lastName("Mendoza")
                .age(24)
                .email("roci@gmail.com")
                .build();

        given(clientRepository.findAll()).willReturn(List.of(client1,client2));
        given(clientMapper.toDto(client1)).willReturn(clientDto1);
        given(clientMapper.toDto(client2)).willReturn(clientDto2);

        //When
        List<ClientDto> result = clientService.findAll();

        //Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(clientDto1));
        assertTrue(result.contains(clientDto2));

        verify(clientRepository, times(1)).findAll();
        verify(clientMapper).toDto(client1);
        verify(clientMapper).toDto(client2);
    }

    @Test
    void findByIdTest() {

        //Given
        given(clientRepository.findById(1)).willReturn(Optional.of(client1));
        given(clientMapper.toDto(client1)).willReturn(clientDto1);

        //When
        ClientDto result = clientService.findById(client1.getId());

        //Then
        assertNotNull(result);
        assertEquals(1,result.getId());
        assertEquals("Wilfredo",result.getFirstName());
        assertEquals("Huaroto",result.getLastName());
        assertEquals(26,result.getAge());
        assertEquals("will@gmail.com",result.getEmail());

        verify(clientRepository, times(1)).findById(anyInt());
        verify(clientMapper, times(1)).toDto(client1);
    }

    @Test
    void findByIdWhenNoExistTest() {

        //Given
        given(clientRepository.findById(9)).willReturn(Optional.empty());

        //When/then
        assertThrows(ResourceNotFoundException.class,()->clientService.findById(9));

        verify(clientRepository, times(1)).findById(anyInt());
        verify(clientMapper, never()).toDto(any(Client.class));
    }

    @Test
    void saveClientTest() {

        //Given
        given(clientRepository.findByEmail(clientDto1.getEmail())).willReturn(Optional.empty());
        given(clientMapper.toEntity(clientDto1)).willReturn(client1);
        given(clientRepository.save(client1)).willReturn(client1);

        //When
        clientService.saveClient(clientDto1);

        //Then
        verify(clientRepository, times(1)).findByEmail(anyString());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(clientMapper, times(1)).toEntity(any(ClientDto.class));
    }

    @Test
    void saveClientIfEmailExistTest() {

        //Given
        given(clientRepository.findByEmail(clientDto1.getEmail())).willReturn(Optional.of(client1));
        //When-Then
        assertThrows(BadRequestException.class, () -> clientService.saveClient(clientDto1));

        verify(clientRepository,times(1)).findByEmail(anyString());
        verify(clientMapper, never()).toEntity(any(ClientDto.class));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void updateClientWhenNoExistTest() {

        //Given
        ClientDto updateClient = ClientDto.builder()
                .firstName("Juan")
                .lastName("Ramirez")
                .age(19)
                .email("jramirez@gmail.com")
                .build();

        Integer id = 9;
        given(clientRepository.findById(id)).willReturn(Optional.empty());

        //When-Then
        assertThrows(ResourceNotFoundException.class, () -> clientService.updateClient(id,updateClient));

        verify(clientRepository, times(1)).findById(anyInt());
        verify(clientRepository, never()).findByEmail(anyString());
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientMapper, never()).toDto(any(Client.class));
    }

    @Test
    void updateClient_whenEmailExistsButSameClientId_test() {

        //Given
        ClientDto updateClient = ClientDto.builder()
                .firstName("Juan")
                .lastName("Perez")
                .age(35)
                .email("will@gmail.com")
                .build();

        ClientDto responseClient = ClientDto.builder()
                .id(1)
                .firstName("Juan")
                .lastName("Perez")
                .age(35)
                .email("will@gmail.com")
                .build();

        Integer id = 1;
        given(clientRepository.findById(id)).willReturn(Optional.of(client1));
        given(clientRepository.findByEmail(updateClient.getEmail())).willReturn(Optional.of(client1));
        given(clientRepository.save(any(Client.class))).willAnswer(inv -> inv.getArgument(0));
        given(clientMapper.toDto(any(Client.class))).willReturn(responseClient);

        // When
        ClientDto result = clientService.updateClient(id, updateClient);

        // Then
        assertNotNull(result);
        assertEquals(responseClient.getId(), result.getId());
        assertEquals(responseClient.getFirstName(), result.getFirstName());
        assertEquals(responseClient.getLastName(), result.getLastName());
        assertEquals(responseClient.getAge(), result.getAge());
        assertEquals(responseClient.getEmail(), result.getEmail());

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).findByEmail(updateClient.getEmail());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(clientMapper, times(1)).toDto(any(Client.class));
    }

    @Test
    void updateClient_whenEmailExistsWithDifferentId_test() {
        //Given
        Client anotherClient = Client.builder()
                .id(2)
                .firstName("Renato")
                .lastName("Vargas")
                .age(28)
                .email("renat@gmail.com")
                .build();

        ClientDto updateClient = ClientDto.builder()
                .firstName("Richard")
                .lastName("Villegas")
                .age(50)
                .email("renat@gmail.com")
                .build();

        Integer id = 1;
        given(clientRepository.findById(id)).willReturn(Optional.of(client1));
        given(clientRepository.findByEmail(updateClient.getEmail())).willReturn(Optional.of(anotherClient));

        //When - Then
        assertThrows(BadRequestException.class, () -> clientService.updateClient(id, updateClient));

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).findByEmail(updateClient.getEmail());
        verify(clientRepository, never()).save(any(Client.class));
        verify(clientMapper, never()).toDto(any(Client.class));
    }

    @Test
    void deleteByIdTest() {

        //Given
        Integer id = client1.getId();
        given(clientRepository.findById(id)).willReturn(Optional.of(client1));

        //When
        clientService.deleteById(id);

        //Then
        verify(clientRepository, times(1)).findById(anyInt());
        verify(clientRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteByIdWhenNoExistTest() {

        //Given
        Integer id = 5;
        given(clientRepository.findById(id)).willReturn(Optional.empty());

        //When-Then
        assertThrows(ResourceNotFoundException.class, ()-> clientService.deleteById(id));

        verify(clientRepository, times(1)).findById(anyInt());
        verify(clientRepository, never()).deleteById(anyInt());
    }



}
