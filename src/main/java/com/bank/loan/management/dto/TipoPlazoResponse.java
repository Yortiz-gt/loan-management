package com.bank.loan.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPlazoResponse {
    private Integer plazoID;
    private Integer meses;
    private BigDecimal tasaInteres;
}
