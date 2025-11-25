package com.bank.loan.management.dao;

import com.bank.loan.management.model.TipoPlazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPlazoRepository extends JpaRepository<TipoPlazo, Integer> {
}
