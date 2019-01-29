package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.JobDetailsReport;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobDetailsReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobDetailsReportRepository extends JpaRepository<JobDetailsReport, Long> {

}
