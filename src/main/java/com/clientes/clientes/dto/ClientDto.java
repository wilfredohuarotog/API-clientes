package com.clientes.clientes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientDto{

        private Integer id;

        @NotBlank
        @Size (min = 3, message = "Escriba bien su nombre")
        private String firstName;

        @NotBlank
        @Size (min = 3, message = "Escriba bien su nombre")
        private String lastName;

        @Email (message = "ingrese bien su correo: ejemplo@dominio.com")
        private String email;

        @NotNull
        @Min(value = 18, message = "edad minima 18 años")
        @Max(value = 65, message = "edad máxima 65 años")
        private Integer age;
}
