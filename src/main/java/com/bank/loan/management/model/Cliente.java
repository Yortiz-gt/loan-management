package com.bank.loan.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClienteID")
    private Integer clienteID;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "NumeroIdentificacion", nullable = false, unique = true, length = 20)
    private String numeroIdentificacion;

    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "Direccion", length = 255)
    private String direccion;

    @Column(name = "CorreoElectronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "Telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "UsuarioCreacion", nullable = false, length = 50)
    private String usuarioCreacion;

    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioModificacion", length = 50)
    private String usuarioModificacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

}
