package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.PagoRepository;
import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dto.PagoRequest;
import com.bank.loan.management.dto.PagoResponse;
import com.bank.loan.management.exception.PrestamoNotFoundException;
import com.bank.loan.management.mapper.PagoMapper;
import com.bank.loan.management.model.Pago;
import com.bank.loan.management.model.Prestamo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionPagosServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private PrestamoRepository prestamoRepository;
    @Mock
    private PagoMapper pagoMapper;

    @InjectMocks
    private GestionPagosServiceImpl gestionPagosService;

    private Prestamo prestamo;
    private PagoRequest pagoRequest;
    private Pago pago;
    private PagoResponse pagoResponse;

    @BeforeEach
    void setUp() {
        prestamo = new Prestamo();
        prestamo.setPrestamoID(1);
        prestamo.setMontoPendiente(new BigDecimal("1000.00"));

        pagoRequest = new PagoRequest();
        pagoRequest.setPrestamoID(1);
        pagoRequest.setMontoPago(new BigDecimal("100.00"));

        pago = new Pago();
        pago.setPagoID(1);
        pago.setPrestamo(prestamo);
        pago.setMontoPago(new BigDecimal("100.00"));

        pagoResponse = new PagoResponse(1, 1, new BigDecimal("100.00"), null, null, null);
    }

    @Test
    void cuandoRegistrarPago_conDatosValidos_deberiaGuardarPagoYActualizarPrestamo() {
        
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        when(pagoMapper.toDto(any(Pago.class))).thenReturn(pagoResponse);

        
        PagoResponse resultado = gestionPagosService.registrarPago(pagoRequest);

        
        assertNotNull(resultado);
        assertEquals(new BigDecimal("900.00"), prestamo.getMontoPendiente());
        verify(prestamoRepository, times(1)).save(prestamo);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void cuandoRegistrarPago_conPrestamoNoExistente_deberiaLanzarExcepcion() {
        
        when(prestamoRepository.findById(99)).thenReturn(Optional.empty());
        pagoRequest.setPrestamoID(99);

        
        assertThrows(PrestamoNotFoundException.class, () -> gestionPagosService.registrarPago(pagoRequest));
    }

    @Test
    void cuandoRegistrarPago_conMontoExcesivo_deberiaLanzarExcepcion() {
        
        pagoRequest.setMontoPago(new BigDecimal("2000.00"));
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));

        
        assertThrows(IllegalArgumentException.class, () -> gestionPagosService.registrarPago(pagoRequest));
    }

    @Test
    void cuandoRegistrarPago_conMontoNegativo_deberiaLanzarExcepcion() {
        
        pagoRequest.setMontoPago(new BigDecimal("-100.00"));
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));

        
        assertThrows(IllegalArgumentException.class, () -> gestionPagosService.registrarPago(pagoRequest));
    }

    @Test
    void cuandoRegistrarPago_conMontoCero_deberiaLanzarExcepcion() {
        
        pagoRequest.setMontoPago(BigDecimal.ZERO);
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));

        
        assertThrows(IllegalArgumentException.class, () -> gestionPagosService.registrarPago(pagoRequest));
    }

    @Test
    void cuandoGetPagosByPrestamo_conIdExistente_deberiaDevolverListaDePagos() {
        
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));
        when(pagoRepository.findByPrestamo(prestamo)).thenReturn(Collections.singletonList(pago));
        when(pagoMapper.toDto(pago)).thenReturn(pagoResponse);

        
        List<PagoResponse> resultados = gestionPagosService.getPagosByPrestamo(1);

        
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals(1, resultados.get(0).getPagoID());
    }

    @Test
    void cuandoGetPagosByPrestamo_conPrestamoNoExistente_deberiaLanzarExcepcion() {
        
        when(prestamoRepository.findById(99)).thenReturn(Optional.empty());

        
        assertThrows(PrestamoNotFoundException.class, () -> gestionPagosService.getPagosByPrestamo(99));
    }
}
