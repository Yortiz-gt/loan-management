package com.bank.loan.management.svc;

import com.bank.loan.management.model.SolicitudPrestamo;

import java.util.List;

public interface SolicitudPrestamosService {
    SolicitudPrestamo crearSolicitud(SolicitudPrestamo solicitud);
    List<SolicitudPrestamo> getAllSolicitudes();
    List<SolicitudPrestamo> getSolicitudesByCliente(Integer clienteId);
    SolicitudPrestamo getSolicitudById(Integer solicitudId);
    SolicitudPrestamo aprobarSolicitud(Integer solicitudId, String detallesAprobacion);
    SolicitudPrestamo rechazarSolicitud(Integer solicitudId, String detallesRechazo);
}
