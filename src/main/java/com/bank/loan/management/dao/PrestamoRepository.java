package com.bank.loan.management.dao;

import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud")
    List<Prestamo> findAllWithDetails();

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud WHERE c.id = :clienteId")
    List<Prestamo> findByClienteIdWithDetails(@Param("clienteId") Integer clienteId);

    void deleteByCliente(Cliente cliente);
}
