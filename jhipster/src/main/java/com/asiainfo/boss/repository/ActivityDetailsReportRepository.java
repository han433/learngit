package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.ActivityDetailsReport;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ActivityDetailsReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityDetailsReportRepository extends JpaRepository<ActivityDetailsReport, Long> {

}
