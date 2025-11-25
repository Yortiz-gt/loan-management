package com.bank.loan.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Integer clienteID;
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String correoElectronico;
    private String telefono;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;
}
