package com.bank.loan.management.web;

import com.bank.loan.management.dto.PrestamoResponse;
import com.bank.loan.management.svc.GestionPrestamosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    @Autowired
    private GestionPrestamosService gestionPrestamosService;

    @Value("${app.pagination.default-page-size:10}")
    private int defaultPageSize;

    @Value("${app.pagination.max-page-size:25}")
    private int maxPageSize;

    @GetMapping
    public ResponseEntity<Page<PrestamoResponse>> getAllPrestamos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "${app.pagination.default-page-size}") int size) {

        int actualSize = Math.min(size, maxPageSize);
        Pageable pageable = PageRequest.of(page - 1, actualSize);
        Page<PrestamoResponse> prestamosPage = gestionPrestamosService.getAllPrestamos(pageable);
        return ResponseEntity.ok(prestamosPage);
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<Page<PrestamoResponse>> getPrestamosByCliente(
            @PathVariable Integer clienteId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "${app.pagination.default-page-size}") int size) {

        int actualSize = Math.min(size, maxPageSize);
        Pageable pageable = PageRequest.of(page - 1, actualSize);
        Page<PrestamoResponse> prestamosPage = gestionPrestamosService.getPrestamosByCliente(clienteId, pageable);
        return ResponseEntity.ok(prestamosPage);
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
