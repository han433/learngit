package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.InvoiceActivity;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InvoiceActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceActivityRepository extends JpaRepository<InvoiceActivity, Long> {

}
