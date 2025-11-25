package com.bank.loan.management.dao;

import com.bank.loan.management.model.Pago;
import com.bank.loan.management.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    Page<Pago> findByPrestamo(Prestamo prestamo, Pageable pageable);
}
