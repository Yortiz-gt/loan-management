package com.bank.loan.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {
    private Integer pagoID;
    private Integer prestamoID;
    private BigDecimal montoPago;
    private LocalDateTime fechaPago;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
}
