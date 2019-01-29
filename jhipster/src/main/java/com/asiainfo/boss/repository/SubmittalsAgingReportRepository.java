package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.SubmittalsAgingReport;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SubmittalsAgingReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubmittalsAgingReportRepository extends JpaRepository<SubmittalsAgingReport, Long> {

}
