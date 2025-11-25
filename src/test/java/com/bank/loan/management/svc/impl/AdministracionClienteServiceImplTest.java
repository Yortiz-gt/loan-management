package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.ClienteRepository;
import com.bank.loan.management.dao.PrestamoRepository;
import com.bank.loan.management.dao.SolicitudPrestamoRepository;
import com.bank.loan.management.dto.ClienteRequest;
import com.bank.loan.management.dto.ClienteResponse;
import com.bank.loan.management.exception.ClienteAlreadyExistsException;
import com.bank.loan.management.exception.ClienteNotFoundException;
import com.bank.loan.management.mapper.ClienteMapper;
import com.bank.loan.management.model.Cliente;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministracionClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private SolicitudPrestamoRepository solicitudPrestamoRepository;
    @Mock
    private PrestamoRepository prestamoRepository;
    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private AdministracionClienteServiceImpl administracionClienteService;

    private ClienteRequest clienteRequest;
    private Cliente cliente;
    private ClienteResponse clienteResponse;
    private Cliente otroCliente;

    @BeforeEach
    void setUp() {
        clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("John");
        clienteRequest.setApellido("Doe");
        clienteRequest.setNumeroIdentificacion("12345");
        clienteRequest.setCorreoElectronico("john.doe@example.com");

        cliente = new Cliente();
        cliente.setClienteID(1);
        cliente.setNombre("John");
        cliente.setApellido("Doe");
        cliente.setNumeroIdentificacion("12345");
        cliente.setCorreoElectronico("john.doe@example.com");

        otroCliente = new Cliente();
        otroCliente.setClienteID(2);
        otroCliente.setNumeroIdentificacion("54321");
        otroCliente.setCorreoElectronico("another.doe@example.com");

        clienteResponse = new ClienteResponse(
            1, "John", "Doe", "12345", null, null, "john.doe@example.com", null, null, null, null, null
        );
    }

    @Test
    void cuandoAgregarCliente_conDatosNuevos_deberiaGuardarCliente() {
        
        when(clienteRepository.findByNumeroIdentificacion(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.findByCorreoElectronico(anyString())).thenReturn(Optional.empty());
        when(clienteMapper.toEntity(any(ClienteRequest.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteResponse);

        
        ClienteResponse resultado = administracionClienteService.agregarCliente(clienteRequest);

        
        assertNotNull(resultado);
        assertEquals(clienteResponse.getClienteID(), resultado.getClienteID());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void cuandoAgregarCliente_conIdentificacionExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findByNumeroIdentificacion("12345")).thenReturn(Optional.of(cliente));

        
        assertThrows(ClienteAlreadyExistsException.class, () -> administracionClienteService.agregarCliente(clienteRequest));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void cuandoAgregarCliente_conCorreoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findByNumeroIdentificacion(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.findByCorreoElectronico("john.doe@example.com")).thenReturn(Optional.of(cliente));

        
        assertThrows(ClienteAlreadyExistsException.class, () -> administracionClienteService.agregarCliente(clienteRequest));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void cuandoGetClienteById_conIdExistente_deberiaDevolverCliente() {
        
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDto(cliente)).thenReturn(clienteResponse);

        
        ClienteResponse resultado = administracionClienteService.getClienteById(1);

        
        assertNotNull(resultado);
        assertEquals(1, resultado.getClienteID());
    }

    @Test
    void cuandoGetClienteById_conIdNoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        
        assertThrows(ClienteNotFoundException.class, () -> administracionClienteService.getClienteById(99));
    }

    @Test
    void cuandoGetAllClientes_deberiaDevolverPaginaDeClientes() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> clientePage = new PageImpl<>(Collections.singletonList(cliente));
        when(clienteRepository.findAll(pageable)).thenReturn(clientePage);
        when(clienteMapper.toDto(cliente)).thenReturn(clienteResponse);

        
        Page<ClienteResponse> resultado = administracionClienteService.getAllClientes(pageable);

        
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("John", resultado.getContent().get(0).getNombre());
    }

    @Test
    void cuandoEditarCliente_conIdExistente_deberiaActualizarCliente() {
        
        ClienteRequest requestActualizado = new ClienteRequest();
        requestActualizado.setNombre("John Updated");
        requestActualizado.setNumeroIdentificacion("12345");
        requestActualizado.setCorreoElectronico("john.doe@example.com");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByNumeroIdentificacion(anyString())).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByCorreoElectronico(anyString())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteResponse);

        
        ClienteResponse resultado = administracionClienteService.editarCliente(1, requestActualizado);

        
        assertNotNull(resultado);
        verify(clienteMapper).updateEntityFromDto(requestActualizado, cliente);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void cuandoEditarCliente_conIdentificacionExistenteParaOtroCliente_deberiaLanzarExcepcion() {
        
        ClienteRequest requestActualizado = new ClienteRequest();
        requestActualizado.setNumeroIdentificacion("54321");
        requestActualizado.setCorreoElectronico("john.doe@example.com");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByNumeroIdentificacion("54321")).thenReturn(Optional.of(otroCliente));

        
        assertThrows(ClienteAlreadyExistsException.class, () -> administracionClienteService.editarCliente(1, requestActualizado));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void cuandoEditarCliente_conCorreoExistenteParaOtroCliente_deberiaLanzarExcepcion() {
        
        ClienteRequest requestActualizado = new ClienteRequest();
        requestActualizado.setNumeroIdentificacion("12345");
        requestActualizado.setCorreoElectronico("another.doe@example.com");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByNumeroIdentificacion(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.findByCorreoElectronico("another.doe@example.com")).thenReturn(Optional.of(otroCliente));

        
        assertThrows(ClienteAlreadyExistsException.class, () -> administracionClienteService.editarCliente(1, requestActualizado));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void cuandoEditarCliente_conClienteNoExistente_deberiaLanzarExcepcion() {
        
        ClienteRequest requestActualizado = new ClienteRequest();
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        
        assertThrows(ClienteNotFoundException.class, () -> administracionClienteService.editarCliente(99, requestActualizado));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void cuandoEliminarCliente_conIdExistente_deberiaEliminarEntidades() {
        
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        
        administracionClienteService.eliminarCliente(1);

        
        verify(prestamoRepository, times(1)).deleteByCliente(cliente);
        verify(solicitudPrestamoRepository, times(1)).deleteByCliente(cliente);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void cuandoEliminarCliente_conClienteNoExistente_deberiaLanzarExcepcion() {
        
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        
        assertThrows(ClienteNotFoundException.class, () -> administracionClienteService.eliminarCliente(99));
        verify(prestamoRepository, never()).deleteByCliente(any(Cliente.class));
        verify(solicitudPrestamoRepository, never()).deleteByCliente(any(Cliente.class));
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }
}
