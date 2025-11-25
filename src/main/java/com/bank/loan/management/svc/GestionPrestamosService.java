package com.bank.loan.management.svc;

import com.bank.loan.management.dto.PrestamoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface GestionPrestamosService {
    Page<PrestamoResponse> getAllPrestamos(Pageable pageable);
    Page<PrestamoResponse> getPrestamosByCliente(Integer clienteId, Pageable pageable);
    PrestamoResponse getPrestamoById(Integer prestamoId);
    BigDecimal calcularSaldoPendiente(Integer prestamoId);
}
