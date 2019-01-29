package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Application_commission;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Application_commission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Application_commissionRepository extends JpaRepository<Application_commission, Long> {

}
