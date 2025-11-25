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
public class SolicitudPrestamoRequest {

    @NotNull(message = "El ID del cliente no puede ser nulo")
    private Integer clienteID;

    @NotNull(message = "El monto solicitado no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto solicitado debe ser mayor que cero")
    private BigDecimal montoSolicitado;

    @NotNull(message = "El ID del tipo de plazo no puede ser nulo")
    private Integer plazoID;


}
