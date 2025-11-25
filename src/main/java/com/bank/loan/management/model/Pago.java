package com.bank.loan.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PagoID")
    private Integer pagoID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PrestamoID", nullable = false)
    private Prestamo prestamo;

    @Column(name = "MontoPago", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoPago;

    @Column(name = "FechaPago", nullable = false, updatable = false)
    private LocalDateTime fechaPago;

    @Column(name = "UsuarioCreacion", nullable = false, length = 50, updatable = false)
    private String usuarioCreacion;

    @Column(name = "FechaCreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaPago = LocalDateTime.now();
        fechaCreacion = LocalDateTime.now();
    }
}
