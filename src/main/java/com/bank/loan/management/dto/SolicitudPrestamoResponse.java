package com.bank.loan.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPrestamoResponse {
    private Integer solicitudID;
    private ClienteResponse cliente;
    private BigDecimal montoSolicitado;
    private TipoPlazoResponse tipoPlazo;
    private LocalDateTime fechaSolicitud;
    private EstadoSolicitudResponse estadoSolicitud;
    private String detallesAprobacion;
    private String usuarioCreacion;
    private LocalDateTime fechaCreacion;
    private String usuarioModificacion;
    private LocalDateTime fechaModificacion;
}
