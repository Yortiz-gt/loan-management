package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dto.PrestamoResponse;
import com.bank.loan.management.exception.PrestamoNotFoundException;
import com.bank.loan.management.mapper.PrestamoMapper;
import com.bank.loan.management.model.Prestamo;
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
class GestionPrestamosServiceImplTest {

    @Mock
    private PrestamoRepository prestamoRepository;
    @Mock
    private PrestamoMapper prestamoMapper;

    @InjectMocks
    private GestionPrestamosServiceImpl gestionPrestamosService;

    private Prestamo prestamo;
    private PrestamoResponse prestamoResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        prestamo = new Prestamo();
        prestamo.setPrestamoID(1);
        prestamo.setMontoPrincipal(new BigDecimal("10000.00"));
        prestamo.setMontoPendiente(new BigDecimal("5000.00"));

        prestamoResponse = new PrestamoResponse(
                1, null, null, new BigDecimal("10000.00"), 12, new BigDecimal("0.05"),
                null, new BigDecimal("5000.00"), null, null, null, null
        );
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void cuandoGetAllPrestamos_deberiaDevolverPaginaDePrestamoResponse() {
        
        Page<Prestamo> prestamoPage = new PageImpl<>(Collections.singletonList(prestamo));
        when(prestamoRepository.findAllWithDetails(pageable)).thenReturn(prestamoPage);
        when(prestamoMapper.toDto(any(Prestamo.class))).thenReturn(prestamoResponse);

        
        Page<PrestamoResponse> resultados = gestionPrestamosService.getAllPrestamos(pageable);

        
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.getTotalElements());
        assertEquals(prestamoResponse.getPrestamoID(), resultados.getContent().get(0).getPrestamoID());
        verify(prestamoRepository, times(1)).findAllWithDetails(pageable);
        verify(prestamoMapper, times(1)).toDto(any(Prestamo.class));
    }

    @Test
    void cuandoGetPrestamosByCliente_conClienteExistente_deberiaDevolverPaginaDePrestamoResponse() {
        
        Integer clienteId = 1;
        Page<Prestamo> prestamoPage = new PageImpl<>(Collections.singletonList(prestamo));
        when(prestamoRepository.findByClienteIdWithDetails(clienteId, pageable)).thenReturn(prestamoPage);
        when(prestamoMapper.toDto(any(Prestamo.class))).thenReturn(prestamoResponse);

        
        Page<PrestamoResponse> resultados = gestionPrestamosService.getPrestamosByCliente(clienteId, pageable);

        
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.getTotalElements());
        assertEquals(prestamoResponse.getPrestamoID(), resultados.getContent().get(0).getPrestamoID());
        verify(prestamoRepository, times(1)).findByClienteIdWithDetails(clienteId, pageable);
        verify(prestamoMapper, times(1)).toDto(any(Prestamo.class));
    }

    @Test
    void cuandoGetPrestamoById_conIdExistente_deberiaDevolverPrestamoResponse() {
        
        Integer prestamoId = 1;
        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));
        when(prestamoMapper.toDto(any(Prestamo.class))).thenReturn(prestamoResponse);

        
        PrestamoResponse resultado = gestionPrestamosService.getPrestamoById(prestamoId);

        
        assertNotNull(resultado);
        assertEquals(prestamoResponse.getPrestamoID(), resultado.getPrestamoID());
        verify(prestamoRepository, times(1)).findById(prestamoId);
        verify(prestamoMapper, times(1)).toDto(any(Prestamo.class));
    }

    @Test
    void cuandoGetPrestamoById_conIdNoExistente_deberiaLanzarExcepcion() {
        
        Integer prestamoId = 99;
        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.empty());

        
        assertThrows(PrestamoNotFoundException.class, () -> gestionPrestamosService.getPrestamoById(prestamoId));
        verify(prestamoRepository, times(1)).findById(prestamoId);
        verify(prestamoMapper, never()).toDto(any(Prestamo.class));
    }

    @Test
    void cuandoCalcularSaldoPendiente_conIdExistente_deberiaDevolverSaldo() {
        
        Integer prestamoId = 1;
        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.of(prestamo));

        
        BigDecimal saldo = gestionPrestamosService.calcularSaldoPendiente(prestamoId);

        
        assertNotNull(saldo);
        assertEquals(new BigDecimal("5000.00"), saldo);
        verify(prestamoRepository, times(1)).findById(prestamoId);
    }

    @Test
    void cuandoCalcularSaldoPendiente_conIdNoExistente_deberiaLanzarExcepcion() {
        
        Integer prestamoId = 99;
        when(prestamoRepository.findById(prestamoId)).thenReturn(Optional.empty());

        
        assertThrows(PrestamoNotFoundException.class, () -> gestionPrestamosService.calcularSaldoPendiente(prestamoId));
        verify(prestamoRepository, times(1)).findById(prestamoId);
    }
}
