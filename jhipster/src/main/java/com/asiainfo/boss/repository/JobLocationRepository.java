package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.JobLocation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobLocationRepository extends JpaRepository<JobLocation, Long> {

}
