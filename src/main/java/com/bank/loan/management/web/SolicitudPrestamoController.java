package com.bank.loan.management.web;

import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.svc.SolicitudPrestamosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPrestamoController {

    @Autowired
    private SolicitudPrestamosService solicitudPrestamosService;

    @Value("${app.pagination.default-page-size:10}")
    private int defaultPageSize;

    @Value("${app.pagination.max-page-size:25}")
    private int maxPageSize;

    @PostMapping
    public ResponseEntity<SolicitudPrestamoResponse> crearSolicitud(@Valid @RequestBody SolicitudPrestamoRequest request) {
        SolicitudPrestamoResponse nuevaSolicitud = solicitudPrestamosService.crearSolicitud(request);
        return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<SolicitudPrestamoResponse>> getAllSolicitudes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "${app.pagination.default-page-size}") int size) {

        int actualSize = Math.min(size, maxPageSize);
        Pageable pageable = PageRequest.of(page - 1, actualSize);
        Page<SolicitudPrestamoResponse> solicitudesPage = solicitudPrestamosService.getAllSolicitudes(pageable);
        return ResponseEntity.ok(solicitudesPage);
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<Page<SolicitudPrestamoResponse>> getSolicitudesByCliente(
            @PathVariable Integer clienteId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "${app.pagination.default-page-size}") int size) {

        int actualSize = Math.min(size, maxPageSize);
        Pageable pageable = PageRequest.of(page - 1, actualSize);
        Page<SolicitudPrestamoResponse> solicitudesPage = solicitudPrestamosService.getSolicitudesByCliente(clienteId, pageable);
        return ResponseEntity.ok(solicitudesPage);
    }

    @GetMapping("/prestamo-id/{id}")
    public ResponseEntity<SolicitudPrestamoResponse> getSolicitudById(@PathVariable Integer id) {
        SolicitudPrestamoResponse solicitud = solicitudPrestamosService.getSolicitudById(id);
        return ResponseEntity.ok(solicitud);
    }

    @PutMapping("/prestamo-id/{id}/aprobar")
    public ResponseEntity<SolicitudPrestamoResponse> aprobarSolicitud(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String detalles = payload.get("detalles");
        SolicitudPrestamoResponse solicitudAprobada = solicitudPrestamosService.aprobarSolicitud(id, detalles);
        return ResponseEntity.ok(solicitudAprobada);
    }

    @PutMapping("/prestamo-id/{id}/rechazar")
    public ResponseEntity<SolicitudPrestamoResponse> rechazarSolicitud(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String detalles = payload.get("detalles");
        SolicitudPrestamoResponse solicitudRechazada = solicitudPrestamosService.rechazarSolicitud(id, detalles);
        return ResponseEntity.ok(solicitudRechazada);
    }
}
