package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.ActivityReport;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ActivityReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long> {

}
