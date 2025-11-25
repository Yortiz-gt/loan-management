package com.bank.loan.management.svc;

import com.bank.loan.management.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdministracionClienteService {
    Cliente agregarCliente(Cliente cliente);
    Page<Cliente> getAllClientes(Pageable pageable);
    Cliente getClienteById(Integer id);
    Cliente editarCliente(Integer id, Cliente clienteDetails);
    void eliminarCliente(Integer id);
}
