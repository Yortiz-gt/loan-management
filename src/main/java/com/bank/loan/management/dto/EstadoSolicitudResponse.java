package com.bank.loan.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSolicitudResponse {
    private Integer estadoID;
    private String nombreEstado;
}
