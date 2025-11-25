package com.bank.loan.management.mapper;

import com.bank.loan.management.dto.PagoResponse;
import com.bank.loan.management.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoResponse toDto(Pago pago) {
        if (pago == null) {
            return null;
        }

        return new PagoResponse(
                pago.getPagoID(),
                pago.getPrestamo().getPrestamoID(),
                pago.getMontoPago(),
                pago.getFechaPago(),
                pago.getUsuarioCreacion(),
                pago.getFechaCreacion()
        );
    }
}
