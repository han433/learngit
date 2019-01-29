package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.ApnParam;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApnParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApnParamRepository extends JpaRepository<ApnParam, Long> {

}
