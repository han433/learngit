package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.SummaryReport;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SummaryReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SummaryReportRepository extends JpaRepository<SummaryReport, Long> {

}
