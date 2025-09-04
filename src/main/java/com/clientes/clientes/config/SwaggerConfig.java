package com.clientes.clientes.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "API DE CLIENTES",
                version = "1.0.0",
                description = "API RESTful que permite realizar operaciones CRUD sobre la entidad Clientes. " +
                        "Incluye endpoints para crear, leer, actualizar y eliminar registros.",
                contact = @Contact(
                        name = "Wilfredo Huaroto",
                        email = "ejemplo@gmail.com"
                )
        )
)
public class SwaggerConfig {
}
