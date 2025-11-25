package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.*;
import com.bank.loan.management.dto.SolicitudPrestamoRequest;
import com.bank.loan.management.dto.SolicitudPrestamoResponse;
import com.bank.loan.management.exception.ClienteNotFoundException;
import com.bank.loan.management.exception.InvalidSolicitudStatusException;
import com.bank.loan.management.exception.SolicitudNotFoundException; 
import com.bank.loan.management.mapper.SolicitudPrestamoMapper;
import com.bank.loan.management.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudPrestamosServiceImplTest {

    @Mock private SolicitudPrestamoRepository solicitudPrestamoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private TipoPlazoRepository tipoPlazoRepository;
    @Mock private EstadoSolicitudRepository estadoSolicitudRepository;
    @Mock private PrestamoRepository prestamoRepository;
    @Mock private SolicitudPrestamoMapper solicitudPrestamoMapper;

    @InjectMocks
    private SolicitudPrestamosServiceImpl solicitudPrestamosService;

    private SolicitudPrestamoRequest solicitudRequest;
    private Cliente cliente;
    private TipoPlazo tipoPlazo;
    private EstadoSolicitud estadoEnProceso;
    private EstadoSolicitud estadoAprobado;
    private EstadoSolicitud estadoRechazado;
    private SolicitudPrestamo solicitud;
    private SolicitudPrestamoResponse solicitudResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        solicitudRequest = new SolicitudPrestamoRequest(1, new BigDecimal("5000.00"),1 );
        cliente = new Cliente();
        cliente.setClienteID(1);
        tipoPlazo = new TipoPlazo(1, 12, new BigDecimal("0.05"));
        estadoEnProceso = new EstadoSolicitud(1, "EN_PROCESO");
        estadoAprobado = new EstadoSolicitud(2, "APROBADO");
        estadoRechazado = new EstadoSolicitud(3, "RECHAZADO");

        solicitud = new SolicitudPrestamo();
        solicitud.setSolicitudID(1);
        solicitud.setCliente(cliente);
        solicitud.setTipoPlazo(tipoPlazo);
        solicitud.setEstadoSolicitud(estadoEnProceso);
        solicitud.setMontoSolicitado(new BigDecimal("5000.00"));

        solicitudResponse = new SolicitudPrestamoResponse(1, null, new BigDecimal("5000.00"), null, null, null, null, null, null, null, null);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void cuandoCrearSolicitud_conDatosValidos_deberiaGuardarSolicitud() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(tipoPlazoRepository.findById(1)).thenReturn(Optional.of(tipoPlazo));
        when(estadoSolicitudRepository.findById(1)).thenReturn(Optional.of(estadoEnProceso));
        when(solicitudPrestamoRepository.save(any(SolicitudPrestamo.class))).thenReturn(solicitud);
        when(solicitudPrestamoMapper.toDto(any(SolicitudPrestamo.class))).thenReturn(solicitudResponse);

        
        SolicitudPrestamoResponse resultado = solicitudPrestamosService.crearSolicitud(solicitudRequest);
        
        
        assertNotNull(resultado);
        assertEquals(1, resultado.getSolicitudID());
        verify(solicitudPrestamoRepository, times(1)).save(any(SolicitudPrestamo.class));
    }

    @Test
    void cuandoCrearSolicitud_conClienteNoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.empty());

        
        assertThrows(ClienteNotFoundException.class, () -> solicitudPrestamosService.crearSolicitud(solicitudRequest));
    }

    @Test
    void cuandoCrearSolicitud_conTipoPlazoNoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(cliente));
        when(tipoPlazoRepository.findById(anyInt())).thenReturn(Optional.empty());

        
        assertThrows(SolicitudNotFoundException.class, () -> solicitudPrestamosService.crearSolicitud(solicitudRequest));
    }

    @Test
    void cuandoCrearSolicitud_conEstadoEnProcesoNoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(cliente));
        when(tipoPlazoRepository.findById(anyInt())).thenReturn(Optional.of(tipoPlazo));
        when(estadoSolicitudRepository.findById(anyInt())).thenReturn(Optional.empty());

        
        assertThrows(SolicitudNotFoundException.class, () -> solicitudPrestamosService.crearSolicitud(solicitudRequest));
    }

    @Test
    void cuandoGetAllSolicitudes_deberiaDevolverPaginaDeSolicitudes() {
        
        Page<SolicitudPrestamo> solicitudPage = new PageImpl<>(Collections.singletonList(solicitud));
        when(solicitudPrestamoRepository.findAll(pageable)).thenReturn(solicitudPage);
        when(solicitudPrestamoMapper.toDto(any(SolicitudPrestamo.class))).thenReturn(solicitudResponse);

        
        Page<SolicitudPrestamoResponse> resultados = solicitudPrestamosService.getAllSolicitudes(pageable);

        
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.getTotalElements());
        assertEquals(1, resultados.getContent().get(0).getSolicitudID());
        verify(solicitudPrestamoRepository, times(1)).findAll(pageable);
        verify(solicitudPrestamoMapper, times(1)).toDto(any(SolicitudPrestamo.class));
    }

    @Test
    void cuandoGetSolicitudesByCliente_conClienteExistente_deberiaDevolverPaginaDeSolicitudes() {
        
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        Page<SolicitudPrestamo> solicitudPage = new PageImpl<>(Collections.singletonList(solicitud));
        when(solicitudPrestamoRepository.findByCliente(cliente, pageable)).thenReturn(solicitudPage);
        when(solicitudPrestamoMapper.toDto(any(SolicitudPrestamo.class))).thenReturn(solicitudResponse);

        
        Page<SolicitudPrestamoResponse> resultados = solicitudPrestamosService.getSolicitudesByCliente(1, pageable);

        
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.getTotalElements());
        assertEquals(1, resultados.getContent().get(0).getSolicitudID());
        verify(solicitudPrestamoRepository, times(1)).findByCliente(cliente, pageable);
        verify(solicitudPrestamoMapper, times(1)).toDto(any(SolicitudPrestamo.class));
    }

    @Test
    void cuandoGetSolicitudesByCliente_conClienteNoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.empty());

        
        assertThrows(ClienteNotFoundException.class, () -> solicitudPrestamosService.getSolicitudesByCliente(99, pageable));
    }

    @Test
    void cuandoGetSolicitudById_conIdExistente_deberiaDevolverSolicitud() {
        
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(solicitudPrestamoMapper.toDto(any(SolicitudPrestamo.class))).thenReturn(solicitudResponse);

        
        SolicitudPrestamoResponse resultado = solicitudPrestamosService.getSolicitudById(1);

        
        assertNotNull(resultado);
        assertEquals(1, resultado.getSolicitudID());
    }

    @Test
    void cuandoGetSolicitudById_conIdNoExistente_deberiaLanzarExcepcion() {
        
        when(solicitudPrestamoRepository.findById(anyInt())).thenReturn(Optional.empty());

        
        assertThrows(SolicitudNotFoundException.class, () -> solicitudPrestamosService.getSolicitudById(99));
    }

    @Test
    void cuandoAprobarSolicitud_conSolicitudValida_deberiaAprobarYCrearPrestamo() {
        
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(2)).thenReturn(Optional.of(estadoAprobado));
        when(solicitudPrestamoRepository.save(any(SolicitudPrestamo.class))).thenReturn(solicitud);
        when(solicitudPrestamoMapper.toDto(any(SolicitudPrestamo.class))).thenReturn(solicitudResponse);

        
        SolicitudPrestamoResponse resultado = solicitudPrestamosService.aprobarSolicitud(1, "Aprobado");

        
        assertNotNull(resultado);
        assertEquals(estadoAprobado, solicitud.getEstadoSolicitud());
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
    }

    @Test
    void cuandoAprobarSolicitud_conEstadoInvalido_deberiaLanzarExcepcion() {
        
        solicitud.setEstadoSolicitud(estadoAprobado);
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));

        
        assertThrows(InvalidSolicitudStatusException.class, () -> solicitudPrestamosService.aprobarSolicitud(1, "Aprobado de nuevo"));
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void cuandoAprobarSolicitud_conEstadoAprobadoNoExistente_deberiaLanzarExcepcion() {
        
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(2)).thenReturn(Optional.empty());

        
        assertThrows(SolicitudNotFoundException.class, () -> solicitudPrestamosService.aprobarSolicitud(1, "Aprobado"));
    }

    @Test
    void cuandoRechazarSolicitud_conSolicitudValida_deberiaRechazarSolicitud() {
        
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(3)).thenReturn(Optional.of(estadoRechazado));
        when(solicitudPrestamoRepository.save(any(SolicitudPrestamo.class))).thenReturn(solicitud);
        when(solicitudPrestamoMapper.toDto(any(SolicitudPrestamo.class))).thenReturn(solicitudResponse);

        
        SolicitudPrestamoResponse resultado = solicitudPrestamosService.rechazarSolicitud(1, "Rechazado");

        
        assertNotNull(resultado);
        assertEquals(estadoRechazado, solicitud.getEstadoSolicitud());
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void cuandoRechazarSolicitud_conEstadoInvalido_deberiaLanzarExcepcion() {
        
        solicitud.setEstadoSolicitud(estadoAprobado);
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));

        
        assertThrows(InvalidSolicitudStatusException.class, () -> solicitudPrestamosService.rechazarSolicitud(1, "Rechazado de nuevo"));
    }

    @Test
    void cuandoRechazarSolicitud_conEstadoRechazadoNoExistente_deberiaLanzarExcepcion() {
        
        when(solicitudPrestamoRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(3)).thenReturn(Optional.empty());

        
        assertThrows(SolicitudNotFoundException.class, () -> solicitudPrestamosService.rechazarSolicitud(1, "Rechazado"));
    }
}
