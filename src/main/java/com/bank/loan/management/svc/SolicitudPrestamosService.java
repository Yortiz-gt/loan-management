package com.bank.loan.management.svc;

import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;

import java.util.List;

public interface SolicitudPrestamosService {
    SolicitudPrestamoResponse crearSolicitud(SolicitudPrestamoRequest request);
    List<SolicitudPrestamoResponse> getAllSolicitudes();
    List<SolicitudPrestamoResponse> getSolicitudesByCliente(Integer clienteId);
    SolicitudPrestamoResponse getSolicitudById(Integer solicitudId);
    SolicitudPrestamoResponse aprobarSolicitud(Integer solicitudId, String detallesAprobacion);
    SolicitudPrestamoResponse rechazarSolicitud(Integer solicitudId, String detallesRechazo);
}
