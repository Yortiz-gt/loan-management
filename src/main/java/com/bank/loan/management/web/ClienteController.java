package com.bank.loan.management.web;

import com.bank.loan.management.dto.ClienteRequest;
import com.bank.loan.management.dto.ClienteResponse;
import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.svc.AdministracionClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private AdministracionClienteService administracionCliente;

    private ClienteResponse convertToDto(Cliente cliente) {
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

    private Cliente convertToEntity(ClienteRequest clienteRequest) {
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

    @PostMapping
    public ResponseEntity<ClienteResponse> agregarCliente(@Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente cliente = convertToEntity(clienteRequest);
        Cliente nuevoCliente = administracionCliente.agregarCliente(cliente);
        return new ResponseEntity<>(convertToDto(nuevoCliente), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int actualSize = Math.min(size, 25);

        Pageable pageable = PageRequest.of(page, actualSize);
        Page<ClienteResponse> clientesPage = administracionCliente.getAllClientes(pageable)
                .map(this::convertToDto);
        
        return ResponseEntity.ok(clientesPage.getContent());
    }

    @GetMapping("/id-cliente/{id}")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Integer id) {
        Cliente cliente = administracionCliente.getClienteById(id);
        return ResponseEntity.ok(convertToDto(cliente));
    }

    @PutMapping("/id-cliente/{id}")
    public ResponseEntity<ClienteResponse> editarCliente(@PathVariable Integer id, @Valid @RequestBody ClienteRequest clienteRequest) {
        Cliente clienteDetails = convertToEntity(clienteRequest);
        Cliente clienteActualizado = administracionCliente.editarCliente(id, clienteDetails);
        return ResponseEntity.ok(convertToDto(clienteActualizado));
    }

    @DeleteMapping("/id-cliente/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        administracionCliente.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
