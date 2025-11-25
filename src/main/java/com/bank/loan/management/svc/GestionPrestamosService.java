package com.bank.loan.management.svc;

import com.bank.loan.management.model.Prestamo;

import java.math.BigDecimal;
import java.util.List;

public interface GestionPrestamosService {
    List<Prestamo> getAllPrestamos();
    List<Prestamo> getPrestamosByCliente(Integer clienteId);
    Prestamo getPrestamoById(Integer prestamoId);
    BigDecimal calcularSaldoPendiente(Integer prestamoId);
}
