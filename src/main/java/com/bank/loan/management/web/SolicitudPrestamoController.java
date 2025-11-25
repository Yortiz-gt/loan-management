package com.bank.loan.management.web;

import com.bank.loan.management.dto.ClienteResponse;
import com.bank.loan.management.dto.EstadoSolicitudResponse;
import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.dto.TipoPlazoResponse;
import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.SolicitudPrestamo;
import com.bank.loan.management.model.TipoPlazo;
import com.bank.loan.management.svc.SolicitudPrestamosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudPrestamoController {

    @Autowired
    private SolicitudPrestamosService solicitudPrestamosService;

    private SolicitudPrestamoResponse convertToDto(SolicitudPrestamo solicitud) {
        ClienteResponse clienteResponse = new ClienteResponse(
                solicitud.getCliente().getClienteID(),
                solicitud.getCliente().getNombre(),
                solicitud.getCliente().getApellido(),
                solicitud.getCliente().getNumeroIdentificacion(),
                solicitud.getCliente().getFechaNacimiento(),
                solicitud.getCliente().getDireccion(),
                solicitud.getCliente().getCorreoElectronico(),
                solicitud.getCliente().getTelefono(),
                solicitud.getCliente().getUsuarioCreacion(),
                solicitud.getCliente().getFechaCreacion(),
                solicitud.getCliente().getUsuarioModificacion(),
                solicitud.getCliente().getFechaModificacion()
        );

        TipoPlazoResponse tipoPlazoResponse = new TipoPlazoResponse(
                solicitud.getTipoPlazo().getPlazoID(),
                solicitud.getTipoPlazo().getMeses(),
                solicitud.getTipoPlazo().getTasaInteres()
        );

        EstadoSolicitudResponse estadoSolicitudResponse = new EstadoSolicitudResponse(
                solicitud.getEstadoSolicitud().getEstadoID(),
                solicitud.getEstadoSolicitud().getNombreEstado()
        );

        return new SolicitudPrestamoResponse(
                solicitud.getSolicitudID(),
                clienteResponse,
                solicitud.getMontoSolicitado(),
                tipoPlazoResponse,
                solicitud.getFechaSolicitud(),
                estadoSolicitudResponse,
                solicitud.getDetallesAprobacion(),
                solicitud.getUsuarioCreacion(),
                solicitud.getFechaCreacion(),
                solicitud.getUsuarioModificacion(),
                solicitud.getFechaModificacion()
        );
    }

    private SolicitudPrestamo convertToEntity(SolicitudPrestamoRequest request) {
        SolicitudPrestamo solicitud = new SolicitudPrestamo();
        solicitud.setMontoSolicitado(request.getMontoSolicitado());

        Cliente cliente = new Cliente();
        cliente.setClienteID(request.getClienteID());
        solicitud.setCliente(cliente);

        TipoPlazo tipoPlazo = new TipoPlazo();
        tipoPlazo.setPlazoID(request.getPlazoID());
        solicitud.setTipoPlazo(tipoPlazo);

        return solicitud;
    }

    @PostMapping
    public ResponseEntity<SolicitudPrestamoResponse> crearSolicitud(@Valid @RequestBody SolicitudPrestamoRequest request) {
        SolicitudPrestamo solicitud = convertToEntity(request);
        SolicitudPrestamo nuevaSolicitud = solicitudPrestamosService.crearSolicitud(solicitud);
        return new ResponseEntity<>(convertToDto(nuevaSolicitud), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudPrestamoResponse>> getAllSolicitudes() {
        List<SolicitudPrestamoResponse> solicitudes = solicitudPrestamosService.getAllSolicitudes().stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<List<SolicitudPrestamoResponse>> getSolicitudesByCliente(@PathVariable Integer clienteId) {
        List<SolicitudPrestamoResponse> solicitudes = solicitudPrestamosService.getSolicitudesByCliente(clienteId).stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/prestamo-id/{id}")
    public ResponseEntity<SolicitudPrestamoResponse> getSolicitudById(@PathVariable Integer id) {
        SolicitudPrestamo solicitud = solicitudPrestamosService.getSolicitudById(id);
        return ResponseEntity.ok(convertToDto(solicitud));
    }

    @PutMapping("/prestamo-id/{id}/aprobar")
    public ResponseEntity<SolicitudPrestamoResponse> aprobarSolicitud(@PathVariable Integer id, @RequestBody String detallesAprobacion) {
        SolicitudPrestamo solicitudAprobada = solicitudPrestamosService.aprobarSolicitud(id, detallesAprobacion);
        return ResponseEntity.ok(convertToDto(solicitudAprobada));
    }

    @PutMapping("/prestamo-id/{id}/rechazar")
    public ResponseEntity<SolicitudPrestamoResponse> rechazarSolicitud(@PathVariable Integer id, @RequestBody String detallesRechazo) {
        SolicitudPrestamo solicitudRechazada = solicitudPrestamosService.rechazarSolicitud(id, detallesRechazo);
        return ResponseEntity.ok(convertToDto(solicitudRechazada));
    }
}
