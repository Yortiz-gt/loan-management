package com.bank.loan.management.svc;

import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SolicitudPrestamosService {
    SolicitudPrestamoResponse crearSolicitud(SolicitudPrestamoRequest request);
    Page<SolicitudPrestamoResponse> getAllSolicitudes(Pageable pageable);
    Page<SolicitudPrestamoResponse> getSolicitudesByCliente(Integer clienteId, Pageable pageable);
    SolicitudPrestamoResponse getSolicitudById(Integer solicitudId);
    SolicitudPrestamoResponse aprobarSolicitud(Integer solicitudId, String detallesAprobacion);
    SolicitudPrestamoResponse rechazarSolicitud(Integer solicitudId, String detallesRechazo);
}
