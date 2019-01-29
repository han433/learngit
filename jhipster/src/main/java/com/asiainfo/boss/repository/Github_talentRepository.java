package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Github_talent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Github_talent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Github_talentRepository extends JpaRepository<Github_talent, Long> {

}
