package com.bank.loan.management.svc;

import com.bank.loan.management.dto.PrestamoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface GestionPrestamosService {
    List<PrestamoResponse> getAllPrestamos();
    List<PrestamoResponse> getPrestamosByCliente(Integer clienteId);
    PrestamoResponse getPrestamoById(Integer prestamoId);
    BigDecimal calcularSaldoPendiente(Integer prestamoId);
}
