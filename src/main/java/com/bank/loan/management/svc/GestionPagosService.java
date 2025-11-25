package com.bank.loan.management.svc;

import com.bank.loan.management.dto.PagoRequest;
import com.bank.loan.management.dto.PagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GestionPagosService {
    PagoResponse registrarPago(PagoRequest pagoRequest);
    Page<PagoResponse> getPagosByPrestamo(Integer prestamoId, Pageable pageable);
}
