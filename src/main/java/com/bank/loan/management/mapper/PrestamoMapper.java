package com.bank.loan.management.mapper;

import com.bank.loan.management.dto.*;
import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.Prestamo;
import com.bank.loan.management.model.SolicitudPrestamo;
import org.springframework.stereotype.Component;

@Component
public class PrestamoMapper {

    public PrestamoResponse toDto(Prestamo prestamo) {
        if (prestamo == null) {
            return null;
        }

        return new PrestamoResponse(
                prestamo.getPrestamoID(),
                toDto(prestamo.getSolicitud()),
                toDto(prestamo.getCliente()),
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

    private ClienteResponse toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteResponse(
                cliente.getClienteID(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getNumeroIdentificacion(),
                cliente.getFechaNacimiento(),
                cliente.getDireccion(),
                cliente.getCorreoElectronico(),
                cliente.getTelefono(),
                cliente.getUsuarioCreacion(),
                cliente.getFechaCreacion(),
                cliente.getUsuarioModificacion(),
                cliente.getFechaModificacion()
        );
    }

    private SolicitudPrestamoResponse toDto(SolicitudPrestamo solicitud) {
        if (solicitud == null) {
            return null;
        }

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
                null,
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
}
