package com.bank.loan.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TipoPlazo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPlazo {

    @Id
    @Column(name = "PlazoID")
    private Integer plazoID;

    @Column(name = "Meses", nullable = false, unique = true)
    private Integer meses;

    @Column(name = "TasaInteres", nullable = false, precision = 5, scale = 4)
    private BigDecimal tasaInteres;
}
