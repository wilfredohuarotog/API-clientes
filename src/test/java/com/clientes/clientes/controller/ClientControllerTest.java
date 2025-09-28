package com.clientes.clientes.controller;

import com.clientes.clientes.dto.ClientDto;
import com.clientes.clientes.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest {

   @Autowired
    MockMvc mockMvc;

   @MockitoBean
    ClientService clientService;

   @Autowired
    ObjectMapper objectMapper;

   @Test
    void allClientsTest() throws Exception {

       //Given

       List<ClientDto> clientsDto = new ArrayList<>();

       clientsDto.add(ClientDto.builder()
               .id(1)
               .firstName("Wilfredo")
               .lastName("Huaroto")
               .age(26)
               .email("will@gmail.com")
               .build());

       clientsDto.add(ClientDto.builder()
               .id(2)
               .firstName("Rebeca")
               .lastName("Cordova")
               .age(25)
               .email("reb@gmail.com")
               .build());

       given(clientService.findAll()).willReturn(clientsDto);

       //When

       ResultActions result =  mockMvc.perform(get("/api/v1/clientes"));

       //Then

       result.andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.size()").value(2))
               .andExpect(jsonPath("$[0].firstName").value(clientsDto.get(0).getFirstName()))
               .andExpect(jsonPath("$[1].firstName").value(clientsDto.get(1).getFirstName()));

       verify(clientService, times(1)).findAll();
   }

   @Test
    void findByIdTest() throws Exception {

       //Given
       ClientDto clientDto = ClientDto.builder()
               .id(1)
               .firstName("Wilfredo")
               .lastName("Huaroto")
               .age(26)
               .email("will@gmail.com")
               .build();

       Integer id = 1;
       given(clientService.findById(id)).willReturn(clientDto);

       //When

       ResultActions result = mockMvc.perform(get("/api/v1/clientes/{id}",id));

       //Then

       result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.id").value(clientDto.getId()))
               .andExpect(jsonPath("$.firstName").value(clientDto.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(clientDto.getLastName()))
               .andExpect(jsonPath("$.age").value(clientDto.getAge()))
               .andExpect(jsonPath("$.email").value(clientDto.getEmail()));

       verify(clientService, times(1)).findById(id);

   }

   @Test
    void saveClientTest() throws Exception {
       //given

       ClientDto savedClient = ClientDto.builder()
               .id(4)
               .firstName("Raul")
               .lastName("Rodriguez")
               .age(36)
               .email("raul@gmail.com")
               .build();

       willDoNothing().given(clientService).saveClient(any(ClientDto.class));

       //When
       ResultActions result = mockMvc.perform(post("/api/v1/clientes")
               .content(objectMapper.writeValueAsString(savedClient))
               .contentType(MediaType.APPLICATION_JSON_VALUE));

       //Then
       result.andExpect(status().isCreated())
               .andExpect(content().string("Se guardó el cliente"));

       verify(clientService, times(1)).saveClient(savedClient);
   }

   @Test
   void updateClientTest() throws Exception {

      ClientDto requestClientDto = ClientDto.builder()
              .firstName("Manual")
              .lastName("Gamboa")
              .age(24)
              .email("man12@gmail.com")
              .build();

      ClientDto updatedClient = ClientDto.builder()
              .id(1)
              .firstName("Manual")
              .lastName("Gamboa")
              .age(24)
              .email("man12@gmail.com")
              .build();

      //Given
      Integer id = 1;
      given(clientService.updateClient(id,requestClientDto)).willReturn(updatedClient);

      //When
      ResultActions result = mockMvc.perform(put("/api/v1/clientes/{id}",id)
              .content(objectMapper.writeValueAsString(requestClientDto))
              .contentType(MediaType.APPLICATION_JSON_VALUE));

      //Then
      result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
              .andExpect(jsonPath("$.id").value(updatedClient.getId()))
              .andExpect(jsonPath("$.firstName").value(updatedClient.getFirstName()))
              .andExpect(jsonPath("$.lastName").value(updatedClient.getLastName()))
              .andExpect(jsonPath("$.age").value(updatedClient.getAge()))
              .andExpect(jsonPath("$.email").value(updatedClient.getEmail()));

      verify(clientService, times(1)).updateClient(id,requestClientDto);

   }

   @Test
   void deleteClientTest () throws Exception {

      //Given
      Integer id = 1;
      willDoNothing().given(clientService).deleteById(id);

      //When
      ResultActions result = mockMvc.perform(delete("/api/v1/clientes/{id}",id));

      //Then
      result.andExpect(status().isNoContent());

      verify(clientService, times(1)).deleteById(id);
   }

}
