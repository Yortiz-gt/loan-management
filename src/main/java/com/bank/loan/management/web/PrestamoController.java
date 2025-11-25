package com.bank.loan.management.web;

import com.bank.loan.management.dto.ClienteResponse;
import com.bank.loan.management.dto.PrestamoResponse;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.dto.EstadoSolicitudResponse;
import com.bank.loan.management.dto.TipoPlazoResponse;
import com.bank.loan.management.model.Prestamo;
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

    private PrestamoResponse convertToDto(Prestamo prestamo) {
        ClienteResponse clienteResponse = new ClienteResponse(
                prestamo.getCliente().getClienteID(),
                prestamo.getCliente().getNombre(),
                prestamo.getCliente().getApellido(),
                prestamo.getCliente().getNumeroIdentificacion(),
                prestamo.getCliente().getFechaNacimiento(),
                prestamo.getCliente().getDireccion(),
                prestamo.getCliente().getCorreoElectronico(),
                prestamo.getCliente().getTelefono(),
                prestamo.getCliente().getUsuarioCreacion(),
                prestamo.getCliente().getFechaCreacion(),
                prestamo.getCliente().getUsuarioModificacion(),
                prestamo.getCliente().getFechaModificacion()
        );

        SolicitudPrestamoResponse solicitudResponse = null;
        if (prestamo.getSolicitud() != null) {
            TipoPlazoResponse tipoPlazoResponse = new TipoPlazoResponse(
                    prestamo.getSolicitud().getTipoPlazo().getPlazoID(),
                    prestamo.getSolicitud().getTipoPlazo().getMeses(),
                    prestamo.getSolicitud().getTipoPlazo().getTasaInteres()
            );
            EstadoSolicitudResponse estadoSolicitudResponse = new EstadoSolicitudResponse(
                    prestamo.getSolicitud().getEstadoSolicitud().getEstadoID(),
                    prestamo.getSolicitud().getEstadoSolicitud().getNombreEstado()
            );
            solicitudResponse = new SolicitudPrestamoResponse(
                    prestamo.getSolicitud().getSolicitudID(),
                    null,
                    prestamo.getSolicitud().getMontoSolicitado(),
                    tipoPlazoResponse,
                    prestamo.getSolicitud().getFechaSolicitud(),
                    estadoSolicitudResponse,
                    prestamo.getSolicitud().getDetallesAprobacion(),
                    prestamo.getSolicitud().getUsuarioCreacion(),
                    prestamo.getSolicitud().getFechaCreacion(),
                    prestamo.getSolicitud().getUsuarioModificacion(),
                    prestamo.getSolicitud().getFechaModificacion()
            );
        }


        return new PrestamoResponse(
                prestamo.getPrestamoID(),
                solicitudResponse,
                clienteResponse,
                prestamo.getMontoPrincipal(),
                prestamo.getPlazoMeses(),
                prestamo.getTasaInteres(),
                prestamo.getFechaAprobacion(),
                prestamo.getMontoPendiente(),
                prestamo.getUsuarioCreacion(),
                prestamo.getFechaCreacion(),
                prestamo.getUsuarioModificacion(),
                prestamo.getFechaModificacion()
        );
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponse>> getAllPrestamos() {
        List<PrestamoResponse> prestamos = gestionPrestamosService.getAllPrestamos().stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/cliente-id/{clienteId}")
    public ResponseEntity<List<PrestamoResponse>> getPrestamosByCliente(@PathVariable Integer clienteId) {
        List<PrestamoResponse> prestamos = gestionPrestamosService.getPrestamosByCliente(clienteId).stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/prestamo-id/{id}")
    public ResponseEntity<PrestamoResponse> getPrestamoById(@PathVariable Integer id) {
        Prestamo prestamo = gestionPrestamosService.getPrestamoById(id);
        return ResponseEntity.ok(convertToDto(prestamo));
    }

    @GetMapping("/prestamo-id/{id}/saldo-pendiente")
    public ResponseEntity<BigDecimal> getSaldoPendiente(@PathVariable Integer id) {
        BigDecimal saldo = gestionPrestamosService.calcularSaldoPendiente(id);
        return ResponseEntity.ok(saldo);
    }
}
