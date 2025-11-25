package com.bank.loan.management.dao;

import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud")
    Page<Prestamo> findAllWithDetails(Pageable pageable);

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud WHERE c.id = :clienteId")
    Page<Prestamo> findByClienteIdWithDetails(@Param("clienteId") Integer clienteId, Pageable pageable);

    void deleteByCliente(Cliente cliente);
}
