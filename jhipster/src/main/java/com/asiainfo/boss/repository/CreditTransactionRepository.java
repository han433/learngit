package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.CreditTransaction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CreditTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

}
