package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.TrendReport;

import com.asiainfo.boss.repository.TrendReportRepository;
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
 * REST controller for managing TrendReport.
 */
@RestController
@RequestMapping("/api")
public class TrendReportResource {

    private final Logger log = LoggerFactory.getLogger(TrendReportResource.class);

    private static final String ENTITY_NAME = "trendReport";

    private final TrendReportRepository trendReportRepository;

    public TrendReportResource(TrendReportRepository trendReportRepository) {
        this.trendReportRepository = trendReportRepository;
    }

    /**
     * POST  /trend-reports : Create a new trendReport.
     *
     * @param trendReport the trendReport to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trendReport, or with status 400 (Bad Request) if the trendReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trend-reports")
    @Timed
    public ResponseEntity<TrendReport> createTrendReport(@RequestBody TrendReport trendReport) throws URISyntaxException {
        log.debug("REST request to save TrendReport : {}", trendReport);
        if (trendReport.getId() != null) {
            throw new BadRequestAlertException("A new trendReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrendReport result = trendReportRepository.save(trendReport);
        return ResponseEntity.created(new URI("/api/trend-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trend-reports : Updates an existing trendReport.
     *
     * @param trendReport the trendReport to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trendReport,
     * or with status 400 (Bad Request) if the trendReport is not valid,
     * or with status 500 (Internal Server Error) if the trendReport couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trend-reports")
    @Timed
    public ResponseEntity<TrendReport> updateTrendReport(@RequestBody TrendReport trendReport) throws URISyntaxException {
        log.debug("REST request to update TrendReport : {}", trendReport);
        if (trendReport.getId() == null) {
            return createTrendReport(trendReport);
        }
        TrendReport result = trendReportRepository.save(trendReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trendReport.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trend-reports : get all the trendReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trendReports in body
     */
    @GetMapping("/trend-reports")
    @Timed
    public List<TrendReport> getAllTrendReports() {
        log.debug("REST request to get all TrendReports");
        return trendReportRepository.findAll();
        }

    /**
     * GET  /trend-reports/:id : get the "id" trendReport.
     *
     * @param id the id of the trendReport to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trendReport, or with status 404 (Not Found)
     */
    @GetMapping("/trend-reports/{id}")
    @Timed
    public ResponseEntity<TrendReport> getTrendReport(@PathVariable Long id) {
        log.debug("REST request to get TrendReport : {}", id);
        TrendReport trendReport = trendReportRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trendReport));
    }

    /**
     * DELETE  /trend-reports/:id : delete the "id" trendReport.
     *
     * @param id the id of the trendReport to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trend-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrendReport(@PathVariable Long id) {
        log.debug("REST request to delete TrendReport : {}", id);
        trendReportRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
