package com.bank.loan.management.web;

import com.bank.loan.management.dto.PagoRequest;
import com.bank.loan.management.dto.PagoResponse;
import com.bank.loan.management.model.Pago;
import com.bank.loan.management.svc.GestionPagosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private GestionPagosService gestionPagosService;

    private PagoResponse convertToDto(Pago pago) {
        return new PagoResponse(
                pago.getPagoID(),
                pago.getPrestamo().getPrestamoID(),
                pago.getMontoPago(),
                pago.getFechaPago(),
                pago.getUsuarioCreacion(),
                pago.getFechaCreacion()
        );
    }

    @PostMapping
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest pagoRequest) {
        Pago nuevoPago = gestionPagosService.registrarPago(pagoRequest.getPrestamoID(), pagoRequest.getMontoPago());
        return new ResponseEntity<>(convertToDto(nuevoPago), HttpStatus.CREATED);
    }

    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<List<PagoResponse>> getPagosByPrestamo(@PathVariable Integer prestamoId) {
        List<PagoResponse> pagos = gestionPagosService.getPagosByPrestamo(prestamoId).stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(pagos);
    }
}
