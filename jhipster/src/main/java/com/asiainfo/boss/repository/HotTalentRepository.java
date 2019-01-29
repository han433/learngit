package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.HotTalent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HotTalent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotTalentRepository extends JpaRepository<HotTalent, Long> {

}
