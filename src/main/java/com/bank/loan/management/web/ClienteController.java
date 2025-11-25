package com.bank.loan.management.web;

import com.bank.loan.management.dto.ClienteRequest;
import com.bank.loan.management.dto.ClienteResponse;
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

    @PostMapping
    public ResponseEntity<ClienteResponse> agregarCliente(@Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse nuevoCliente = administracionCliente.agregarCliente(clienteRequest);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int actualSize = Math.min(size, 25);

        Pageable pageable = PageRequest.of(page, actualSize);
        Page<ClienteResponse> clientesPage = administracionCliente.getAllClientes(pageable);
        
        return ResponseEntity.ok(clientesPage.getContent());
    }

    @GetMapping("/id-cliente/{id}")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Integer id) {
        ClienteResponse cliente = administracionCliente.getClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/id-cliente/{id}")
    public ResponseEntity<ClienteResponse> editarCliente(@PathVariable Integer id, @Valid @RequestBody ClienteRequest clienteRequest) {
        ClienteResponse clienteActualizado = administracionCliente.editarCliente(id, clienteRequest);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/id-cliente/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        administracionCliente.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
