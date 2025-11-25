package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.ClienteRepository;
import com.bank.loan.management.dao.EstadoSolicitudRepository;
import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dao.SolicitudPrestamoRepository;
import com.bank.loan.management.dao.TipoPlazoRepository;
import com.bank.loan.management.exception.ClienteNotFoundException;
import com.bank.loan.management.exception.InvalidSolicitudStatusException;
import com.bank.loan.management.exception.SolicitudNotFoundException;
import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.EstadoSolicitud;
import com.bank.loan.management.model.Prestamo;
import com.bank.loan.management.model.SolicitudPrestamo;
import com.bank.loan.management.model.TipoPlazo;
import com.bank.loan.management.svc.SolicitudPrestamosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SolicitudPrestamosServiceImpl implements SolicitudPrestamosService {

    public static final String SOLICITUD_PRESTAMO_ERROR = "Solicitud de préstamo no encontrada con ID: ";
    @Autowired
    private SolicitudPrestamoRepository solicitudPrestamoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private TipoPlazoRepository tipoPlazoRepository;
    @Autowired
    private EstadoSolicitudRepository estadoSolicitudRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;

    private static final Integer ESTADO_EN_PROCESO_ID = 1;
    private static final Integer ESTADO_APROBADO_ID = 2;
    private static final Integer ESTADO_RECHAZADO_ID = 3;

 
    @Override 
    @Transactional
    public SolicitudPrestamo crearSolicitud(SolicitudPrestamo solicitud) {
        Cliente cliente = clienteRepository.findById(solicitud.getCliente().getClienteID())
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + solicitud.getCliente().getClienteID()));

        TipoPlazo tipoPlazo = tipoPlazoRepository.findById(solicitud.getTipoPlazo().getPlazoID())
                .orElseThrow(() -> new InvalidSolicitudStatusException("Tipo de plazo no encontrado con ID: " + solicitud.getTipoPlazo().getPlazoID()));

        EstadoSolicitud estadoEnProceso = estadoSolicitudRepository.findById(ESTADO_EN_PROCESO_ID)
                .orElseThrow(() -> new RuntimeException("Estado 'EN_PROCESO' no encontrado en la base de datos."));

        solicitud.setCliente(cliente);
        solicitud.setTipoPlazo(tipoPlazo);
        solicitud.setEstadoSolicitud(estadoEnProceso);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setUsuarioCreacion("SYSTEM_SOLICITUD");

        return solicitudPrestamoRepository.save(solicitud);
    }

    @Override
    public List<SolicitudPrestamo> getAllSolicitudes() {
        return solicitudPrestamoRepository.findAll();
    }

    @Override
    public List<SolicitudPrestamo> getSolicitudesByCliente(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + clienteId));
        return solicitudPrestamoRepository.findByCliente(cliente);
    }

    @Override
    public SolicitudPrestamo getSolicitudById(Integer solicitudId) {
        return solicitudPrestamoRepository.findById(solicitudId)
                .orElseThrow(() -> new SolicitudNotFoundException(SOLICITUD_PRESTAMO_ERROR + solicitudId));
    }

    @Override
    @Transactional
    public SolicitudPrestamo aprobarSolicitud(Integer solicitudId, String detallesAprobacion) {
        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(solicitudId)
                .orElseThrow(() -> new SolicitudNotFoundException(SOLICITUD_PRESTAMO_ERROR + solicitudId));

        if (!solicitud.getEstadoSolicitud().getEstadoID().equals(ESTADO_EN_PROCESO_ID)) {
            throw new InvalidSolicitudStatusException("La solicitud no está en estado 'EN_PROCESO' y no puede ser aprobada.");
        }

        EstadoSolicitud estadoAprobado = estadoSolicitudRepository.findById(ESTADO_APROBADO_ID)
                .orElseThrow(() -> new RuntimeException("Estado 'APROBADO' no encontrado en la base de datos."));

        solicitud.setEstadoSolicitud(estadoAprobado);
        solicitud.setDetallesAprobacion(detallesAprobacion);
        solicitud.setUsuarioModificacion("SYSTEM_APROBACION");
        solicitud.setFechaModificacion(LocalDateTime.now());

        SolicitudPrestamo solicitudActualizada = solicitudPrestamoRepository.save(solicitud);

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setSolicitud(solicitudActualizada);
        nuevoPrestamo.setCliente(solicitudActualizada.getCliente());
        nuevoPrestamo.setMontoPrincipal(solicitudActualizada.getMontoSolicitado());
        nuevoPrestamo.setPlazoMeses(solicitudActualizada.getTipoPlazo().getMeses());
        nuevoPrestamo.setTasaInteres(solicitudActualizada.getTipoPlazo().getTasaInteres());
        nuevoPrestamo.setMontoPendiente(solicitudActualizada.getMontoSolicitado());
        nuevoPrestamo.setUsuarioCreacion("SYSTEM_PRESTAMO");
        nuevoPrestamo.setFechaAprobacion(new Date());
        nuevoPrestamo.setFechaCreacion(LocalDateTime.now());

        prestamoRepository.save(nuevoPrestamo);

        return solicitudActualizada;
    }

    @Override
    @Transactional
    public SolicitudPrestamo rechazarSolicitud(Integer solicitudId, String detallesRechazo) {
        SolicitudPrestamo solicitud = solicitudPrestamoRepository.findById(solicitudId)
                .orElseThrow(() -> new SolicitudNotFoundException(SOLICITUD_PRESTAMO_ERROR+ solicitudId));

        if (!solicitud.getEstadoSolicitud().getEstadoID().equals(ESTADO_EN_PROCESO_ID)) {
            throw new InvalidSolicitudStatusException("La solicitud no está en estado 'EN_PROCESO' y no puede ser rechazada.");
        }

        EstadoSolicitud estadoRechazado = estadoSolicitudRepository.findById(ESTADO_RECHAZADO_ID)
                .orElseThrow(() -> new RuntimeException("Estado 'RECHAZADO' no encontrado en la base de datos."));

        solicitud.setEstadoSolicitud(estadoRechazado);
        solicitud.setDetallesAprobacion(detallesRechazo);
        solicitud.setUsuarioModificacion("SYSTEM_RECHAZO"); 
        solicitud.setFechaModificacion(LocalDateTime.now());

        return solicitudPrestamoRepository.save(solicitud);
    }
}
