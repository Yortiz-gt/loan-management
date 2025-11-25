package com.bank.loan.management.web;

import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.svc.SolicitudPrestamosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPrestamoController {

    @Autowired
    private SolicitudPrestamosService solicitudPrestamosService;

    @PostMapping
    public ResponseEntity<SolicitudPrestamoResponse> crearSolicitud(@Valid @RequestBody SolicitudPrestamoRequest request) {
        SolicitudPrestamoResponse nuevaSolicitud = solicitudPrestamosService.crearSolicitud(request);
        return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudPrestamoResponse>> getAllSolicitudes() {
        List<SolicitudPrestamoResponse> solicitudes = solicitudPrestamosService.getAllSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<List<SolicitudPrestamoResponse>> getSolicitudesByCliente(@PathVariable Integer clienteId) {
        List<SolicitudPrestamoResponse> solicitudes = solicitudPrestamosService.getSolicitudesByCliente(clienteId);
        return ResponseEntity.ok(solicitudes);
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
