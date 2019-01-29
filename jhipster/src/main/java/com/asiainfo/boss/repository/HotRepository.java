package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Hot;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Hot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotRepository extends JpaRepository<Hot, Long> {

}
