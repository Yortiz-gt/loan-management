package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.ClienteRepository;
import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.exception.PrestamoNotFoundException;
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
    private ClienteRepository clienteRepository;

    @Override
    public List<Prestamo> getAllPrestamos() {
        return prestamoRepository.findAllWithDetails();
    }


    @Override
    public List<Prestamo> getPrestamosByCliente(Integer clienteId) {
        return prestamoRepository.findByClienteIdWithDetails(clienteId);
    }
    @Override
    public Prestamo getPrestamoById(Integer prestamoId) {
        return prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException("Préstamo no encontrado con ID: " + prestamoId));
    }

    @Override
    public BigDecimal calcularSaldoPendiente(Integer prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNotFoundException("Préstamo no encontrado con ID: " + prestamoId));
        return prestamo.getMontoPendiente();
    }
}
