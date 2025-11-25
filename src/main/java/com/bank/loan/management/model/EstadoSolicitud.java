package com.bank.loan.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EstadoSolicitud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSolicitud {

    @Id
    @Column(name = "EstadoID")
    private Integer estadoID;

    @Column(name = "NombreEstado", nullable = false, unique = true, length = 50)
    private String nombreEstado;
}
