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
import com.bank.loan.management.svc.AdministracionClienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdministracionClienteServiceImpl implements AdministracionClienteService {

    public static final String CLIENTE_NOT_FOUND = "Cliente no encontrado con ID: ";

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SolicitudPrestamoRepository solicitudPrestamoRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteResponse agregarCliente(ClienteRequest clienteRequest) {
        if (clienteRepository.findByNumeroIdentificacion(clienteRequest.getNumeroIdentificacion()).isPresent()) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el número de identificación: " + clienteRequest.getNumeroIdentificacion());
        }
        if (clienteRepository.findByCorreoElectronico(clienteRequest.getCorreoElectronico()).isPresent()) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el correo electrónico: " + clienteRequest.getCorreoElectronico());
        }

        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setUsuarioCreacion("SYSTEM");

        Cliente nuevoCliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(nuevoCliente);
    }

    @Override
    public Page<ClienteResponse> getAllClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(clienteMapper::toDto);
    }

    @Override
    public ClienteResponse getClienteById(Integer id) {
        Cliente cliente = findClienteById(id);
        return clienteMapper.toDto(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse editarCliente(Integer id, ClienteRequest clienteRequest) {
        Cliente cliente = findClienteById(id);

        clienteRepository.findByNumeroIdentificacion(clienteRequest.getNumeroIdentificacion())
                .ifPresent(existingCliente -> {
                    if (!existingCliente.getClienteID().equals(id)) {
                        throw new ClienteAlreadyExistsException("Ya existe otro cliente con el número de identificación: " + clienteRequest.getNumeroIdentificacion());
                    }
                });
        clienteRepository.findByCorreoElectronico(clienteRequest.getCorreoElectronico())
                .ifPresent(existingCliente -> {
                    if (!existingCliente.getClienteID().equals(id)) {
                        throw new ClienteAlreadyExistsException("Ya existe otro cliente con el correo electrónico: " + clienteRequest.getCorreoElectronico());
                    }
                });

        clienteMapper.updateEntityFromDto(clienteRequest, cliente);
        cliente.setUsuarioModificacion("SYSTEM_UPDATE");
        cliente.setFechaModificacion(LocalDateTime.now());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteActualizado);
    }

    @Override
    @Transactional
    public void eliminarCliente(Integer id) {
        Cliente cliente = findClienteById(id);
        prestamoRepository.deleteByCliente(cliente);
        solicitudPrestamoRepository.deleteByCliente(cliente);
        clienteRepository.delete(cliente);
    }

    private Cliente findClienteById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(CLIENTE_NOT_FOUND + id));
    }
}
