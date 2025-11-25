package com.bank.loan.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Prestamo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PrestamoID")
    private Integer prestamoID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SolicitudID", nullable = false, unique = true)
    private SolicitudPrestamo solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClienteID", nullable = false)
    private Cliente cliente;

    @Column(name = "MontoPrincipal", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoPrincipal;

    @Column(name = "PlazoMeses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "TasaInteres", nullable = false, precision = 5, scale = 4)
    private BigDecimal tasaInteres;

    @Column(name = "FechaAprobacion", nullable = false, updatable = false)
    private Date fechaAprobacion;

    @Column(name = "MontoPendiente", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoPendiente;

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
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
