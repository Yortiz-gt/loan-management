package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.PagoRepository;
import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dto.PagoRequest;
import com.bank.loan.management.dto.PagoResponse;
import com.bank.loan.management.exception.PrestamoNotFoundException;
import com.bank.loan.management.mapper.PagoMapper;
import com.bank.loan.management.model.Pago;
import com.bank.loan.management.model.Prestamo;
import com.bank.loan.management.svc.GestionPagosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GestionPagosServiceImpl implements GestionPagosService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PagoMapper pagoMapper;

    @Override
    @Transactional
    public PagoResponse registrarPago(PagoRequest pagoRequest) {
        Prestamo prestamo = findPrestamoById(pagoRequest.getPrestamoID());
        BigDecimal montoPago = pagoRequest.getMontoPago();

        if (montoPago.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser positivo.");
        }
        if (montoPago.compareTo(prestamo.getMontoPendiente()) > 0) {
            throw new IllegalArgumentException("El monto del pago excede el monto pendiente del préstamo.");
        }

        prestamo.setMontoPendiente(prestamo.getMontoPendiente().subtract(montoPago));
        prestamo.setUsuarioModificacion("SYSTEM_PAGO");
        prestamo.setFechaModificacion(LocalDateTime.now());
        prestamoRepository.save(prestamo);

        Pago pago = new Pago();
        pago.setPrestamo(prestamo);
        pago.setMontoPago(montoPago);
        pago.setFechaPago(LocalDateTime.now());
        pago.setUsuarioCreacion("SYSTEM_PAGO");
        pago.setFechaCreacion(LocalDateTime.now());

        Pago nuevoPago = pagoRepository.save(pago);
        return pagoMapper.toDto(nuevoPago);
    }

    @Override
    public Page<PagoResponse> getPagosByPrestamo(Integer prestamoId, Pageable pageable) {
        Prestamo prestamo = findPrestamoById(prestamoId);
        return pagoRepository.findByPrestamo(prestamo, pageable)
                .map(pagoMapper::toDto);
    }

    private Prestamo findPrestamoById(Integer prestamoId) {
        return prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException("Préstamo no encontrado con ID: " + prestamoId));
    }
}
