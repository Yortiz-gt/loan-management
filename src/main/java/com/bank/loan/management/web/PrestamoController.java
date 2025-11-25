package com.bank.loan.management.web;

import com.bank.loan.management.dto.PrestamoResponse;
import com.bank.loan.management.svc.GestionPrestamosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private GestionPrestamosService gestionPrestamosService;

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> getAllPrestamos() {
        List<PrestamoResponse> prestamos = gestionPrestamosService.getAllPrestamos();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<List<PrestamoResponse>> getPrestamosByCliente(@PathVariable Integer clienteId) {
        List<PrestamoResponse> prestamos = gestionPrestamosService.getPrestamosByCliente(clienteId);
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/prestamo-id/{id}")
    public ResponseEntity<PrestamoResponse> getPrestamoById(@PathVariable Integer id) {
        PrestamoResponse prestamo = gestionPrestamosService.getPrestamoById(id);
        return ResponseEntity.ok(prestamo);
    }

    @GetMapping("/prestamo-id/{id}/saldo-pendiente")
    public ResponseEntity<BigDecimal> getSaldoPendiente(@PathVariable Integer id) {
        BigDecimal saldo = gestionPrestamosService.calcularSaldoPendiente(id);
        return ResponseEntity.ok(saldo);
    }
}
