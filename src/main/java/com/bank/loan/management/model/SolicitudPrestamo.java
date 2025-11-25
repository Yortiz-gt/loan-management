package com.bank.loan.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "SolicitudPrestamo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SolicitudID")
    private Integer solicitudID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @Column(name = "MontoSolicitado", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoSolicitado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PlazoID", nullable = false)
    private TipoPlazo tipoPlazo;

    @Column(name = "FechaSolicitud", nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EstadoID", nullable = false)
    private EstadoSolicitud estadoSolicitud;

    @Column(name = "DetallesAprobacion", length = 500)
    private String detallesAprobacion;

    @Column(name = "UsuarioCreacion", nullable = false, length = 50, updatable = false)
    private String usuarioCreacion;

    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "UsuarioModificacion", length = 50)
    private String usuarioModificacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaSolicitud = LocalDateTime.now();
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
