package com.bank.loan.management.svc.impl;

import com.bank.loan.management.dao.ClienteRepository;
import com.bank.loan.management.dao.SolicitudPrestamoRepository;
import com.bank.loan.management.exception.ClienteAlreadyExistsException;
import com.bank.loan.management.exception.ClienteNotFoundException;
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


    @Override
    @Transactional
    public Cliente agregarCliente(Cliente cliente) {

        if (clienteRepository.findByNumeroIdentificacion(cliente.getNumeroIdentificacion()).isPresent()) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el número de identificación: " + cliente.getNumeroIdentificacion());
        }
        if (clienteRepository.findByCorreoElectronico(cliente.getCorreoElectronico()).isPresent()) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con el correo electrónico: " + cliente.getCorreoElectronico());
        }
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setUsuarioCreacion("SYSTEM");

        return clienteRepository.save(cliente);
    }


    @Override
    public Page<Cliente> getAllClientes(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }


    @Override
    public Cliente getClienteById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(CLIENTE_NOT_FOUND + id));
    }


    @Override
    @Transactional
    public Cliente editarCliente(Integer id, Cliente clienteDetails) {
        return clienteRepository.findById(id).map(cliente -> {
            clienteRepository.findByNumeroIdentificacion(clienteDetails.getNumeroIdentificacion())
                    .ifPresent(existingCliente -> {
                        if (!existingCliente.getClienteID().equals(id)) {
                            throw new ClienteAlreadyExistsException("Ya existe otro cliente con el número de identificación: " + clienteDetails.getNumeroIdentificacion());
                        }
                    });
            clienteRepository.findByCorreoElectronico(clienteDetails.getCorreoElectronico())
                    .ifPresent(existingCliente -> {
                        if (!existingCliente.getClienteID().equals(id)) {
                            throw new ClienteAlreadyExistsException("Ya existe otro cliente con el correo electrónico: " + clienteDetails.getCorreoElectronico());
                        }
                    });

            cliente.setNombre(clienteDetails.getNombre());
            cliente.setApellido(clienteDetails.getApellido());
            cliente.setNumeroIdentificacion(clienteDetails.getNumeroIdentificacion());
            cliente.setFechaNacimiento(clienteDetails.getFechaNacimiento());
            cliente.setDireccion(clienteDetails.getDireccion());
            cliente.setCorreoElectronico(clienteDetails.getCorreoElectronico());
            cliente.setTelefono(clienteDetails.getTelefono());
            cliente.setUsuarioModificacion("SYSTEM_UPDATE");
            cliente.setFechaModificacion(LocalDateTime.now());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new ClienteNotFoundException(CLIENTE_NOT_FOUND + id));
    }

    @Override
    @Transactional
    public void eliminarCliente(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(CLIENTE_NOT_FOUND + id));

        solicitudPrestamoRepository.deleteByCliente(cliente);
        clienteRepository.delete(cliente);
    }
}
