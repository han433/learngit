package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.SummaryReport;

import com.asiainfo.boss.repository.SummaryReportRepository;
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
 * REST controller for managing SummaryReport.
 */
@RestController
@RequestMapping("/api")
public class SummaryReportResource {

    private final Logger log = LoggerFactory.getLogger(SummaryReportResource.class);

    private static final String ENTITY_NAME = "summaryReport";

    private final SummaryReportRepository summaryReportRepository;

    public SummaryReportResource(SummaryReportRepository summaryReportRepository) {
        this.summaryReportRepository = summaryReportRepository;
    }

    /**
     * POST  /summary-reports : Create a new summaryReport.
     *
     * @param summaryReport the summaryReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new summaryReport, or with status 400 (Bad Request) if the summaryReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/summary-reports")
    @Timed
    public ResponseEntity<SummaryReport> createSummaryReport(@RequestBody SummaryReport summaryReport) throws URISyntaxException {
        log.debug("REST request to save SummaryReport : {}", summaryReport);
        if (summaryReport.getId() != null) {
            throw new BadRequestAlertException("A new summaryReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SummaryReport result = summaryReportRepository.save(summaryReport);
        return ResponseEntity.created(new URI("/api/summary-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /summary-reports : Updates an existing summaryReport.
     *
     * @param summaryReport the summaryReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated summaryReport,
     * or with status 400 (Bad Request) if the summaryReport is not valid,
     * or with status 500 (Internal Server Error) if the summaryReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/summary-reports")
    @Timed
    public ResponseEntity<SummaryReport> updateSummaryReport(@RequestBody SummaryReport summaryReport) throws URISyntaxException {
        log.debug("REST request to update SummaryReport : {}", summaryReport);
        if (summaryReport.getId() == null) {
            return createSummaryReport(summaryReport);
        }
        SummaryReport result = summaryReportRepository.save(summaryReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, summaryReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /summary-reports : get all the summaryReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of summaryReports in body
     */
    @GetMapping("/summary-reports")
    @Timed
    public List<SummaryReport> getAllSummaryReports() {
        log.debug("REST request to get all SummaryReports");
        return summaryReportRepository.findAll();
        }

    /**
     * GET  /summary-reports/:id : get the "id" summaryReport.
     *
     * @param id the id of the summaryReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the summaryReport, or with status 404 (Not Found)
     */
    @GetMapping("/summary-reports/{id}")
    @Timed
    public ResponseEntity<SummaryReport> getSummaryReport(@PathVariable Long id) {
        log.debug("REST request to get SummaryReport : {}", id);
        SummaryReport summaryReport = summaryReportRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(summaryReport));
    }

    /**
     * DELETE  /summary-reports/:id : delete the "id" summaryReport.
     *
     * @param id the id of the summaryReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/summary-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteSummaryReport(@PathVariable Long id) {
        log.debug("REST request to delete SummaryReport : {}", id);
        summaryReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
