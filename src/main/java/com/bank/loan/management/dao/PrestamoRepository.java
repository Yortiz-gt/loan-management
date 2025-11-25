package com.bank.loan.management.dao;

import com.bank.loan.management.model.Cliente;
import com.bank.loan.management.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    @Query("SELECT new com.bank.loan.management.model.Prestamo(p.prestamoID, p.solicitud, p.cliente, p.montoPrincipal, p.plazoMeses, p.tasaInteres, p.fechaAprobacion, p.montoPendiente, p.usuarioCreacion, p.fechaCreacion, p.usuarioModificacion, p.fechaModificacion) FROM Prestamo p JOIN p.cliente c JOIN p.solicitud s JOIN s.tipoPlazo JOIN s.estadoSolicitud")
    List<Prestamo> findAllWithDetails();

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud WHERE c.id = :clienteId")
    List<Prestamo> findByClienteIdWithDetails(@Param("clienteId") Integer clienteId);

    List<Prestamo> findByCliente(Cliente cliente);
}
