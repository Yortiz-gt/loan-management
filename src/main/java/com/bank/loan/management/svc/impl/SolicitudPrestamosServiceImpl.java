package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.*;
import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.exception.ClienteNotFoundException;
import com.bank.loan.management.exception.InvalidSolicitudStatusException;
import com.bank.loan.management.exception.SolicitudNotFoundException;
import com.bank.loan.management.mapper.SolicitudPrestamoMapper;
import com.bank.loan.management.model.*;
import com.bank.loan.management.svc.SolicitudPrestamosService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudPrestamosServiceImpl implements SolicitudPrestamosService {

    public static final String SOLICITUD_PRESTAMO_ERROR = "Solicitud de préstamo no encontrada con ID: ";
    private static final Integer ESTADO_EN_PROCESO_ID = 1;
    private static final Integer ESTADO_APROBADO_ID = 2;
    private static final Integer ESTADO_RECHAZADO_ID = 3;

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
    @Autowired
    private SolicitudPrestamoMapper solicitudPrestamoMapper;

    @Override
    @Transactional
    public SolicitudPrestamoResponse crearSolicitud(SolicitudPrestamoRequest request) {
        Cliente cliente = findClienteById(request.getClienteID());
        TipoPlazo tipoPlazo = findTipoPlazoById(request.getPlazoID());
        EstadoSolicitud estadoEnProceso = findEstadoSolicitudById(ESTADO_EN_PROCESO_ID);

        SolicitudPrestamo solicitud = new SolicitudPrestamo();
        solicitud.setMontoSolicitado(request.getMontoSolicitado());
        solicitud.setCliente(cliente);
        solicitud.setTipoPlazo(tipoPlazo);
        solicitud.setEstadoSolicitud(estadoEnProceso);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setUsuarioCreacion("SYSTEM_SOLICITUD");

        SolicitudPrestamo nuevaSolicitud = solicitudPrestamoRepository.save(solicitud);
        return solicitudPrestamoMapper.toDto(nuevaSolicitud);
    }

    @Override
    public List<SolicitudPrestamoResponse> getAllSolicitudes() {
        return solicitudPrestamoRepository.findAll().stream()
                .map(solicitudPrestamoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudPrestamoResponse> getSolicitudesByCliente(Integer clienteId) {
        Cliente cliente = findClienteById(clienteId);
        return solicitudPrestamoRepository.findByCliente(cliente).stream()
                .map(solicitudPrestamoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SolicitudPrestamoResponse getSolicitudById(Integer solicitudId) {
        return solicitudPrestamoMapper.toDto(findSolicitudById(solicitudId));
    }

    @Override
    @Transactional
    public SolicitudPrestamoResponse aprobarSolicitud(Integer solicitudId, String detallesAprobacion) {
        SolicitudPrestamo solicitud = findSolicitudById(solicitudId);
        validarSiEstaEnProceso(solicitud);

        EstadoSolicitud estadoAprobado = findEstadoSolicitudById(ESTADO_APROBADO_ID);
        solicitud.setEstadoSolicitud(estadoAprobado);
        solicitud.setDetallesAprobacion(detallesAprobacion);
        solicitud.setUsuarioModificacion("SYSTEM_APROBACION");
        solicitud.setFechaModificacion(LocalDateTime.now());

        SolicitudPrestamo solicitudActualizada = solicitudPrestamoRepository.save(solicitud);
        crearPrestamoDesdeSolicitud(solicitudActualizada);

        return solicitudPrestamoMapper.toDto(solicitudActualizada);
    }

    @Override
    @Transactional
    public SolicitudPrestamoResponse rechazarSolicitud(Integer solicitudId, String detallesRechazo) {
        SolicitudPrestamo solicitud = findSolicitudById(solicitudId);
        validarSiEstaEnProceso(solicitud);

        EstadoSolicitud estadoRechazado = findEstadoSolicitudById(ESTADO_RECHAZADO_ID);
        solicitud.setEstadoSolicitud(estadoRechazado);
        solicitud.setDetallesAprobacion(detallesRechazo);
        solicitud.setUsuarioModificacion("SYSTEM_RECHAZO");
        solicitud.setFechaModificacion(LocalDateTime.now());

        SolicitudPrestamo solicitudActualizada = solicitudPrestamoRepository.save(solicitud);
        return solicitudPrestamoMapper.toDto(solicitudActualizada);
    }

    private void crearPrestamoDesdeSolicitud(SolicitudPrestamo solicitud) {
        BigDecimal montoPrincipal = solicitud.getMontoSolicitado();
        BigDecimal tasaInteres = solicitud.getTipoPlazo().getTasaInteres();
        Integer plazoMeses = solicitud.getTipoPlazo().getMeses();

        BigDecimal interesTotal = montoPrincipal.multiply(tasaInteres).multiply(new BigDecimal(plazoMeses));
        BigDecimal montoPendiente = montoPrincipal.add(interesTotal);

        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setSolicitud(solicitud);
        nuevoPrestamo.setCliente(solicitud.getCliente());
        nuevoPrestamo.setMontoPrincipal(montoPrincipal);
        nuevoPrestamo.setPlazoMeses(plazoMeses);
        nuevoPrestamo.setTasaInteres(tasaInteres);
        nuevoPrestamo.setMontoPendiente(montoPendiente);
        nuevoPrestamo.setUsuarioCreacion("SYSTEM_PRESTAMO");
        nuevoPrestamo.setFechaAprobacion(new Date());
        nuevoPrestamo.setFechaCreacion(LocalDateTime.now());

        prestamoRepository.save(nuevoPrestamo);
    }

    private SolicitudPrestamo findSolicitudById(Integer solicitudId) {
        return solicitudPrestamoRepository.findById(solicitudId)
                .orElseThrow(() -> new SolicitudNotFoundException(SOLICITUD_PRESTAMO_ERROR + solicitudId));
    }

    private Cliente findClienteById(Integer clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + clienteId));
    }

    private TipoPlazo findTipoPlazoById(Integer tipoPlazoId) {
        return tipoPlazoRepository.findById(tipoPlazoId)
                .orElseThrow(() -> new SolicitudNotFoundException("Tipo de plazo no encontrado con ID: " + tipoPlazoId));
    }

    private EstadoSolicitud findEstadoSolicitudById(Integer estadoId) {
        return estadoSolicitudRepository.findById(estadoId)
                .orElseThrow(() -> new SolicitudNotFoundException("Estado de solicitud no encontrado con ID: " + estadoId));
    }

    private void validarSiEstaEnProceso(SolicitudPrestamo solicitud) {
        if (!solicitud.getEstadoSolicitud().getEstadoID().equals(ESTADO_EN_PROCESO_ID)) {
            throw new InvalidSolicitudStatusException("La solicitud no está en estado 'EN_PROCESO' y no puede ser modificada.");
        }
    }
}
