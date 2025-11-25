package com.bank.loan.management.mapper;

import com.bank.loan.management.dto.ClienteResponse;
import com.bank.loan.management.dto.EstadoSolicitudResponse;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.dto.TipoPlazoResponse;
import com.bank.loan.management.model.SolicitudPrestamo;
import org.springframework.stereotype.Component;

@Component
public class SolicitudPrestamoMapper {

    public SolicitudPrestamoResponse toDto(SolicitudPrestamo solicitud) {
        if (solicitud == null) {
            return null;
        }

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
}
