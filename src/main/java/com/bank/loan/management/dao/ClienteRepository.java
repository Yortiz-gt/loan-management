package com.bank.loan.management.dao;

import com.bank.loan.management.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
}
