package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Sequence;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sequence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SequenceRepository extends JpaRepository<Sequence, Long> {

}
