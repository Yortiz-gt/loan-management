package com.bank.loan.management.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String apellido;

    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(max = 13, message = "El número de identificación no puede exceder los 20 caracteres")
    private String numeroIdentificacion;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres")
    private String direccion;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
    private String correoElectronico;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
    private String telefono;


}
