package com.bank.loan.management.svc;

import com.bank.loan.management.dto.ClienteRequest;
import com.bank.loan.management.dto.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministracionClienteService {
    ClienteResponse agregarCliente(ClienteRequest clienteRequest);
    Page<ClienteResponse> getAllClientes(Pageable pageable);
    ClienteResponse getClienteById(Integer id);
    ClienteResponse editarCliente(Integer id, ClienteRequest clienteRequest);
    void eliminarCliente(Integer id);
}
