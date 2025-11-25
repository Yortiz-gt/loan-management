package com.bank.loan.management.svc;

import com.bank.loan.management.dto.PagoRequest;
import com.bank.loan.management.dto.PagoResponse;

import java.util.List;

public interface GestionPagosService {
    PagoResponse registrarPago(PagoRequest pagoRequest);
    List<PagoResponse> getPagosByPrestamo(Integer prestamoId);
}
