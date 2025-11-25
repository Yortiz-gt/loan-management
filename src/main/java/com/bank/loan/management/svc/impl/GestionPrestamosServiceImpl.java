package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dto.PrestamoResponse;
import com.bank.loan.management.exception.PrestamoNotFoundException;
import com.bank.loan.management.mapper.PrestamoMapper;
import com.bank.loan.management.model.Prestamo;
import com.bank.loan.management.svc.GestionPrestamosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GestionPrestamosServiceImpl implements GestionPrestamosService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PrestamoMapper prestamoMapper;

    @Override
    public List<PrestamoResponse> getAllPrestamos() {
        return prestamoRepository.findAllWithDetails().stream()
                .map(prestamoMapper::toDto)
                .toList();
    }

    @Override
    public List<PrestamoResponse> getPrestamosByCliente(Integer clienteId) {
        return prestamoRepository.findByClienteIdWithDetails(clienteId).stream()
                .map(prestamoMapper::toDto)
                .toList();
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
