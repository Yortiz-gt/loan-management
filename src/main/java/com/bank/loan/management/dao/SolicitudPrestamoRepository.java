package com.bank.loan.management.dao;

import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.SolicitudPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudPrestamoRepository extends JpaRepository<SolicitudPrestamo, Integer> {
    List<SolicitudPrestamo> findByCliente(Cliente cliente);
    void deleteByCliente(Cliente cliente);
}
