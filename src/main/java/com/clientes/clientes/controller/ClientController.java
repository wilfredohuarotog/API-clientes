package com.clientes.clientes.controller;

import com.clientes.clientes.dto.ClientDto;
import com.clientes.clientes.exceptions.BadRequestException;
import com.clientes.clientes.exceptions.ResourceNotFoundException;
import com.clientes.clientes.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/v1/clientes")
@Tag(
        name = "Clientes controlador",
        description = "CRUD de clientes. Permite listar, crear, actualizar, buscar y eliminar clientes en el sistema."
)
public class ClientController {

    private final ClientService clientService;

    @Operation(
            summary = "Listar clientes",
            description = "Se obtiene la lista completa de clientes registrados"
    )
    @GetMapping
    public ResponseEntity<List<ClientDto>> allClients(){
        return new ResponseEntity<>(clientService.findAll(), HttpStatus.OK) ;
    }

    @Operation(
            summary = "Cliente por ID",
            description = "Se obtiene un cliente por su ID"
    )
    @GetMapping ("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Integer id){
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(
            summary = "Guardar",
            description = "Se guarda un nuevo cliente"
    )
    @PostMapping
    public ResponseEntity<?> saveClient(@Valid @RequestBody ClientDto clientDto){

        clientService.saveClient(clientDto);
        return new ResponseEntity<>("Se guardó el cliente",HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar",
            description = "Se actualiza los datos de un cliente identificandolo por el ID"
    )
    @PutMapping ("/{id}")
    public ResponseEntity<ClientDto> updateClient( @PathVariable Integer id, @Valid @RequestBody ClientDto clientDto){
        return ResponseEntity.ok(clientService.updateClient(id,clientDto));
    }

    @Operation(summary = "Eliminar",
            description = "Se elimina un cliente identificando por su ID")
    @DeleteMapping ("{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Integer id){
        clientService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
