package com.bank.loan.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoResponse {
    private Integer prestamoID;
    private SolicitudPrestamoResponse solicitud;
    private ClienteResponse cliente;
    private BigDecimal montoPrincipal;
    private Integer plazoMeses;
    private BigDecimal tasaInteres;
    private LocalDateTime fechaAprobacion;
    private BigDecimal montoPendiente;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;

    public PrestamoResponse(Integer prestamoID, SolicitudPrestamoResponse solicitudResponse, ClienteResponse clienteResponse, BigDecimal montoPrincipal, Integer plazoMeses, BigDecimal tasaInteres, Date fechaAprobacion, BigDecimal montoPendiente, String usuarioCreacion, LocalDateTime fechaCreacion, String usuarioModificacion, LocalDateTime fechaModificacion) {
    }
}
