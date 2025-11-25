package com.bank.loan.management.dao;

import com.bank.loan.management.model.Pago;
import com.bank.loan.management.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByPrestamo(Prestamo prestamo);
}
