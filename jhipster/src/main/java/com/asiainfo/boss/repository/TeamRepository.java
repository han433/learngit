package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Team;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
