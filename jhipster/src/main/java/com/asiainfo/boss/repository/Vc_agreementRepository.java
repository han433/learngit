package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Vc_agreement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Vc_agreement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Vc_agreementRepository extends JpaRepository<Vc_agreement, Long> {

}
