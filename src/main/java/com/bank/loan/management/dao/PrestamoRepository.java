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
    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud")
    List<Prestamo> findAllWithDetails();

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.cliente c JOIN FETCH p.solicitud s JOIN FETCH s.tipoPlazo JOIN FETCH s.estadoSolicitud WHERE c = :cliente")
    List<Prestamo> findByClienteWithDetails(@Param("cliente") Cliente cliente);

    List<Prestamo> findByCliente(Cliente cliente);
}
