package com.bank.loan.management.svc;

import com.bank.loan.management.model.Pago;

import java.math.BigDecimal;
import java.util.List;

public interface GestionPagosService {
    Pago registrarPago(Integer prestamoId, BigDecimal montoPago);
    List<Pago> getPagosByPrestamo(Integer prestamoId);
}
