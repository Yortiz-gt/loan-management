package com.bank.loan.management.mapper;

import com.bank.loan.management.dto.ClienteRequest;
import com.bank.loan.management.dto.ClienteResponse;
import com.bank.loan.management.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponse toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteResponse(
                cliente.getClienteID(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getNumeroIdentificacion(),
                cliente.getFechaNacimiento(),
                cliente.getDireccion(),
                cliente.getCorreoElectronico(),
                cliente.getTelefono(),
                cliente.getUsuarioCreacion(),
                cliente.getFechaCreacion(),
                cliente.getUsuarioModificacion(),
                cliente.getFechaModificacion()
        );
    }

    public Cliente toEntity(ClienteRequest clienteRequest) {
        if (clienteRequest == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(clienteRequest.getNombre());
        cliente.setApellido(clienteRequest.getApellido());
        cliente.setNumeroIdentificacion(clienteRequest.getNumeroIdentificacion());
        cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        cliente.setDireccion(clienteRequest.getDireccion());
        cliente.setCorreoElectronico(clienteRequest.getCorreoElectronico());
        cliente.setTelefono(clienteRequest.getTelefono());
        return cliente;
    }

    public void updateEntityFromDto(ClienteRequest clienteRequest, Cliente cliente) {
        if (clienteRequest == null || cliente == null) {
            return;
        }

        cliente.setNombre(clienteRequest.getNombre());
        cliente.setApellido(clienteRequest.getApellido());
        cliente.setNumeroIdentificacion(clienteRequest.getNumeroIdentificacion());
        cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        cliente.setDireccion(clienteRequest.getDireccion());
        cliente.setCorreoElectronico(clienteRequest.getCorreoElectronico());
        cliente.setTelefono(clienteRequest.getTelefono());
    }
}
