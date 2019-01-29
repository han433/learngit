package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.JobDetailsReport;

import com.asiainfo.boss.repository.JobDetailsReportRepository;
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
 * REST controller for managing JobDetailsReport.
 */
@RestController
@RequestMapping("/api")
public class JobDetailsReportResource {

    private final Logger log = LoggerFactory.getLogger(JobDetailsReportResource.class);

    private static final String ENTITY_NAME = "jobDetailsReport";

    private final JobDetailsReportRepository jobDetailsReportRepository;

    public JobDetailsReportResource(JobDetailsReportRepository jobDetailsReportRepository) {
        this.jobDetailsReportRepository = jobDetailsReportRepository;
    }

    /**
     * POST  /job-details-reports : Create a new jobDetailsReport.
     *
     * @param jobDetailsReport the jobDetailsReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobDetailsReport, or with status 400 (Bad Request) if the jobDetailsReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-details-reports")
    @Timed
    public ResponseEntity<JobDetailsReport> createJobDetailsReport(@RequestBody JobDetailsReport jobDetailsReport) throws URISyntaxException {
        log.debug("REST request to save JobDetailsReport : {}", jobDetailsReport);
        if (jobDetailsReport.getId() != null) {
            throw new BadRequestAlertException("A new jobDetailsReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobDetailsReport result = jobDetailsReportRepository.save(jobDetailsReport);
        return ResponseEntity.created(new URI("/api/job-details-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-details-reports : Updates an existing jobDetailsReport.
     *
     * @param jobDetailsReport the jobDetailsReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobDetailsReport,
     * or with status 400 (Bad Request) if the jobDetailsReport is not valid,
     * or with status 500 (Internal Server Error) if the jobDetailsReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-details-reports")
    @Timed
    public ResponseEntity<JobDetailsReport> updateJobDetailsReport(@RequestBody JobDetailsReport jobDetailsReport) throws URISyntaxException {
        log.debug("REST request to update JobDetailsReport : {}", jobDetailsReport);
        if (jobDetailsReport.getId() == null) {
            return createJobDetailsReport(jobDetailsReport);
        }
        JobDetailsReport result = jobDetailsReportRepository.save(jobDetailsReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobDetailsReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-details-reports : get all the jobDetailsReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobDetailsReports in body
     */
    @GetMapping("/job-details-reports")
    @Timed
    public List<JobDetailsReport> getAllJobDetailsReports() {
        log.debug("REST request to get all JobDetailsReports");
        return jobDetailsReportRepository.findAll();
        }

    /**
     * GET  /job-details-reports/:id : get the "id" jobDetailsReport.
     *
     * @param id the id of the jobDetailsReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobDetailsReport, or with status 404 (Not Found)
     */
    @GetMapping("/job-details-reports/{id}")
    @Timed
    public ResponseEntity<JobDetailsReport> getJobDetailsReport(@PathVariable Long id) {
        log.debug("REST request to get JobDetailsReport : {}", id);
        JobDetailsReport jobDetailsReport = jobDetailsReportRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobDetailsReport));
    }

    /**
     * DELETE  /job-details-reports/:id : delete the "id" jobDetailsReport.
     *
     * @param id the id of the jobDetailsReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-details-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobDetailsReport(@PathVariable Long id) {
        log.debug("REST request to delete JobDetailsReport : {}", id);
        jobDetailsReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
