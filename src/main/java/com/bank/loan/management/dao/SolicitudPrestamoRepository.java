package com.bank.loan.management.dao;

import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.SolicitudPrestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudPrestamoRepository extends JpaRepository<SolicitudPrestamo, Integer> {
    Page<SolicitudPrestamo> findAll(Pageable pageable);
    Page<SolicitudPrestamo> findByCliente(Cliente cliente, Pageable pageable);
    void deleteByCliente(Cliente cliente);
}
