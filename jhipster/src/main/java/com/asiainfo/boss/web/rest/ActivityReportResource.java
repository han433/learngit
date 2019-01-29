package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.ActivityReport;

import com.asiainfo.boss.repository.ActivityReportRepository;
import com.asiainfo.boss.web.rest.errors.BadRequestAlertException;
import com.asiainfo.boss.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ActivityReport.
 */
@RestController
@RequestMapping("/api")
public class ActivityReportResource {

    private final Logger log = LoggerFactory.getLogger(ActivityReportResource.class);

    private static final String ENTITY_NAME = "activityReport";

    private final ActivityReportRepository activityReportRepository;

    public ActivityReportResource(ActivityReportRepository activityReportRepository) {
        this.activityReportRepository = activityReportRepository;
    }

    /**
     * POST  /activity-reports : Create a new activityReport.
     *
     * @param activityReport the activityReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityReport, or with status 400 (Bad Request) if the activityReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-reports")
    @Timed
    public ResponseEntity<ActivityReport> createActivityReport(@RequestBody ActivityReport activityReport) throws URISyntaxException {
        log.debug("REST request to save ActivityReport : {}", activityReport);
        if (activityReport.getId() != null) {
            throw new BadRequestAlertException("A new activityReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityReport result = activityReportRepository.save(activityReport);
        return ResponseEntity.created(new URI("/api/activity-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-reports : Updates an existing activityReport.
     *
     * @param activityReport the activityReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityReport,
     * or with status 400 (Bad Request) if the activityReport is not valid,
     * or with status 500 (Internal Server Error) if the activityReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-reports")
    @Timed
    public ResponseEntity<ActivityReport> updateActivityReport(@RequestBody ActivityReport activityReport) throws URISyntaxException {
        log.debug("REST request to update ActivityReport : {}", activityReport);
        if (activityReport.getId() == null) {
            return createActivityReport(activityReport);
        }
        ActivityReport result = activityReportRepository.save(activityReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-reports : get all the activityReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of activityReports in body
     */
    @GetMapping("/activity-reports")
    @Timed
    public List<ActivityReport> getAllActivityReports() {
        log.debug("REST request to get all ActivityReports");
        return activityReportRepository.findAll();
        }

    /**
     * GET  /activity-reports/:id : get the "id" activityReport.
     *
     * @param id the id of the activityReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityReport, or with status 404 (Not Found)
     */
    @GetMapping("/activity-reports/{id}")
    @Timed
    public ResponseEntity<ActivityReport> getActivityReport(@PathVariable Long id) {
        log.debug("REST request to get ActivityReport : {}", id);
        ActivityReport activityReport = activityReportRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(activityReport));
    }

    /**
     * DELETE  /activity-reports/:id : delete the "id" activityReport.
     *
     * @param id the id of the activityReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityReport(@PathVariable Long id) {
        log.debug("REST request to delete ActivityReport : {}", id);
        activityReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
