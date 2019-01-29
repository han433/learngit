package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.ActivityDetailsReport;

import com.asiainfo.boss.repository.ActivityDetailsReportRepository;
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
 * REST controller for managing ActivityDetailsReport.
 */
@RestController
@RequestMapping("/api")
public class ActivityDetailsReportResource {

    private final Logger log = LoggerFactory.getLogger(ActivityDetailsReportResource.class);

    private static final String ENTITY_NAME = "activityDetailsReport";

    private final ActivityDetailsReportRepository activityDetailsReportRepository;

    public ActivityDetailsReportResource(ActivityDetailsReportRepository activityDetailsReportRepository) {
        this.activityDetailsReportRepository = activityDetailsReportRepository;
    }

    /**
     * POST  /activity-details-reports : Create a new activityDetailsReport.
     *
     * @param activityDetailsReport the activityDetailsReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityDetailsReport, or with status 400 (Bad Request) if the activityDetailsReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-details-reports")
    @Timed
    public ResponseEntity<ActivityDetailsReport> createActivityDetailsReport(@RequestBody ActivityDetailsReport activityDetailsReport) throws URISyntaxException {
        log.debug("REST request to save ActivityDetailsReport : {}", activityDetailsReport);
        if (activityDetailsReport.getId() != null) {
            throw new BadRequestAlertException("A new activityDetailsReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityDetailsReport result = activityDetailsReportRepository.save(activityDetailsReport);
        return ResponseEntity.created(new URI("/api/activity-details-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-details-reports : Updates an existing activityDetailsReport.
     *
     * @param activityDetailsReport the activityDetailsReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityDetailsReport,
     * or with status 400 (Bad Request) if the activityDetailsReport is not valid,
     * or with status 500 (Internal Server Error) if the activityDetailsReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-details-reports")
    @Timed
    public ResponseEntity<ActivityDetailsReport> updateActivityDetailsReport(@RequestBody ActivityDetailsReport activityDetailsReport) throws URISyntaxException {
        log.debug("REST request to update ActivityDetailsReport : {}", activityDetailsReport);
        if (activityDetailsReport.getId() == null) {
            return createActivityDetailsReport(activityDetailsReport);
        }
        ActivityDetailsReport result = activityDetailsReportRepository.save(activityDetailsReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityDetailsReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-details-reports : get all the activityDetailsReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of activityDetailsReports in body
     */
    @GetMapping("/activity-details-reports")
    @Timed
    public List<ActivityDetailsReport> getAllActivityDetailsReports() {
        log.debug("REST request to get all ActivityDetailsReports");
        return activityDetailsReportRepository.findAll();
        }

    /**
     * GET  /activity-details-reports/:id : get the "id" activityDetailsReport.
     *
     * @param id the id of the activityDetailsReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityDetailsReport, or with status 404 (Not Found)
     */
    @GetMapping("/activity-details-reports/{id}")
    @Timed
    public ResponseEntity<ActivityDetailsReport> getActivityDetailsReport(@PathVariable Long id) {
        log.debug("REST request to get ActivityDetailsReport : {}", id);
        ActivityDetailsReport activityDetailsReport = activityDetailsReportRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(activityDetailsReport));
    }

    /**
     * DELETE  /activity-details-reports/:id : delete the "id" activityDetailsReport.
     *
     * @param id the id of the activityDetailsReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-details-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityDetailsReport(@PathVariable Long id) {
        log.debug("REST request to delete ActivityDetailsReport : {}", id);
        activityDetailsReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
