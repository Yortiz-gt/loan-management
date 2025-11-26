package com.bank.loan.management.dao;

import com.bank.loan.management.model.Pago;
import com.bank.loan.management.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    Page<Pago> findByPrestamo(Prestamo prestamo, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.montoPago), 0.00) FROM Pago p WHERE p.prestamo.prestamoID = :prestamoId")
    BigDecimal sumMontoPagoByPrestamoId(@Param("prestamoId") Integer prestamoId);
}
