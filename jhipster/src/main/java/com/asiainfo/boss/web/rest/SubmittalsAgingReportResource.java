package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.SubmittalsAgingReport;

import com.asiainfo.boss.repository.SubmittalsAgingReportRepository;
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
 * REST controller for managing SubmittalsAgingReport.
 */
@RestController
@RequestMapping("/api")
public class SubmittalsAgingReportResource {

    private final Logger log = LoggerFactory.getLogger(SubmittalsAgingReportResource.class);

    private static final String ENTITY_NAME = "submittalsAgingReport";

    private final SubmittalsAgingReportRepository submittalsAgingReportRepository;

    public SubmittalsAgingReportResource(SubmittalsAgingReportRepository submittalsAgingReportRepository) {
        this.submittalsAgingReportRepository = submittalsAgingReportRepository;
    }

    /**
     * POST  /submittals-aging-reports : Create a new submittalsAgingReport.
     *
     * @param submittalsAgingReport the submittalsAgingReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new submittalsAgingReport, or with status 400 (Bad Request) if the submittalsAgingReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/submittals-aging-reports")
    @Timed
    public ResponseEntity<SubmittalsAgingReport> createSubmittalsAgingReport(@RequestBody SubmittalsAgingReport submittalsAgingReport) throws URISyntaxException {
        log.debug("REST request to save SubmittalsAgingReport : {}", submittalsAgingReport);
        if (submittalsAgingReport.getId() != null) {
            throw new BadRequestAlertException("A new submittalsAgingReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubmittalsAgingReport result = submittalsAgingReportRepository.save(submittalsAgingReport);
        return ResponseEntity.created(new URI("/api/submittals-aging-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /submittals-aging-reports : Updates an existing submittalsAgingReport.
     *
     * @param submittalsAgingReport the submittalsAgingReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated submittalsAgingReport,
     * or with status 400 (Bad Request) if the submittalsAgingReport is not valid,
     * or with status 500 (Internal Server Error) if the submittalsAgingReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/submittals-aging-reports")
    @Timed
    public ResponseEntity<SubmittalsAgingReport> updateSubmittalsAgingReport(@RequestBody SubmittalsAgingReport submittalsAgingReport) throws URISyntaxException {
        log.debug("REST request to update SubmittalsAgingReport : {}", submittalsAgingReport);
        if (submittalsAgingReport.getId() == null) {
            return createSubmittalsAgingReport(submittalsAgingReport);
        }
        SubmittalsAgingReport result = submittalsAgingReportRepository.save(submittalsAgingReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, submittalsAgingReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /submittals-aging-reports : get all the submittalsAgingReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of submittalsAgingReports in body
     */
    @GetMapping("/submittals-aging-reports")
    @Timed
    public List<SubmittalsAgingReport> getAllSubmittalsAgingReports() {
        log.debug("REST request to get all SubmittalsAgingReports");
        return submittalsAgingReportRepository.findAll();
        }

    /**
     * GET  /submittals-aging-reports/:id : get the "id" submittalsAgingReport.
     *
     * @param id the id of the submittalsAgingReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the submittalsAgingReport, or with status 404 (Not Found)
     */
    @GetMapping("/submittals-aging-reports/{id}")
    @Timed
    public ResponseEntity<SubmittalsAgingReport> getSubmittalsAgingReport(@PathVariable Long id) {
        log.debug("REST request to get SubmittalsAgingReport : {}", id);
        SubmittalsAgingReport submittalsAgingReport = submittalsAgingReportRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(submittalsAgingReport));
    }

    /**
     * DELETE  /submittals-aging-reports/:id : delete the "id" submittalsAgingReport.
     *
     * @param id the id of the submittalsAgingReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/submittals-aging-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubmittalsAgingReport(@PathVariable Long id) {
        log.debug("REST request to delete SubmittalsAgingReport : {}", id);
        submittalsAgingReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
