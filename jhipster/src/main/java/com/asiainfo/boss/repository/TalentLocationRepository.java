package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.TalentLocation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TalentLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TalentLocationRepository extends JpaRepository<TalentLocation, Long> {

}
