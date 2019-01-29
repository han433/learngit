package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.TrendReport;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TrendReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrendReportRepository extends JpaRepository<TrendReport, Long> {

}
