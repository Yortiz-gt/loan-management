package com.bank.loan.management.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequest {

    @NotNull(message = "El ID del préstamo no puede ser nulo")
    private Integer prestamoID;

    @NotNull(message = "El monto del pago no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto del pago debe ser mayor que cero")
    private BigDecimal montoPago;
}
