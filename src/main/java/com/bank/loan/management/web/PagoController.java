package com.bank.loan.management.web;

import com.bank.loan.management.dto.PagoRequest;
import com.bank.loan.management.dto.PagoResponse;
import com.bank.loan.management.svc.GestionPagosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private GestionPagosService gestionPagosService;

    @Value("${app.pagination.default-page-size:10}")
    private int defaultPageSize;

    @Value("${app.pagination.max-page-size:25}")
    private int maxPageSize;

    @PostMapping
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest pagoRequest) {
        PagoResponse nuevoPago = gestionPagosService.registrarPago(pagoRequest);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }

    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<Page<PagoResponse>> getPagosByPrestamo(
            @PathVariable Integer prestamoId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "${app.pagination.default-page-size}") int size) {

        int actualSize = Math.min(size, maxPageSize);
        Pageable pageable = PageRequest.of(page - 1, actualSize);
        Page<PagoResponse> pagosPage = gestionPagosService.getPagosByPrestamo(prestamoId, pageable);
        return ResponseEntity.ok(pagosPage);
    }

    @GetMapping("/prestamo/{prestamoId}/total-pagado")
    public ResponseEntity<BigDecimal> getTotalPagado(@PathVariable Integer prestamoId) {
        BigDecimal totalPagado = gestionPagosService.calcularTotalPagado(prestamoId);
        return ResponseEntity.ok(totalPagado);
    }
}
