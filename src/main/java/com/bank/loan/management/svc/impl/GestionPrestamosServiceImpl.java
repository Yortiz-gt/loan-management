package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dto.PrestamoResponse;
import com.bank.loan.management.exception.PrestamoNotFoundException;
import com.bank.loan.management.mapper.PrestamoMapper;
import com.bank.loan.management.model.Prestamo;
import com.bank.loan.management.svc.GestionPrestamosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GestionPrestamosServiceImpl implements GestionPrestamosService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PrestamoMapper prestamoMapper;

    @Override
    public Page<PrestamoResponse> getAllPrestamos(Pageable pageable) {
        return prestamoRepository.findAllWithDetails(pageable)
                .map(prestamoMapper::toDto);
    }

    @Override
    public Page<PrestamoResponse> getPrestamosByCliente(Integer clienteId, Pageable pageable) {
        return prestamoRepository.findByClienteIdWithDetails(clienteId, pageable)
                .map(prestamoMapper::toDto);
    }

    @Override
    public PrestamoResponse getPrestamoById(Integer prestamoId) {
        Prestamo prestamo = findPrestamoById(prestamoId);
        return prestamoMapper.toDto(prestamo);
    }

    @Override
    public BigDecimal calcularSaldoPendiente(Integer prestamoId) {
        Prestamo prestamo = findPrestamoById(prestamoId);
        return prestamo.getMontoPendiente();
    }

    private Prestamo findPrestamoById(Integer prestamoId) {
        return prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException("Préstamo no encontrado con ID: " + prestamoId));
    }
}
