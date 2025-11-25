package com.bank.loan.management.dao;

import com.bank.loan.management.model.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoSolicitudRepository extends JpaRepository<EstadoSolicitud, Integer> {
}
