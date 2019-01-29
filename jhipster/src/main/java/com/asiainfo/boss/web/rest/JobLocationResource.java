package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.JobLocation;

import com.asiainfo.boss.repository.JobLocationRepository;
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
 * REST controller for managing JobLocation.
 */
@RestController
@RequestMapping("/api")
public class JobLocationResource {

    private final Logger log = LoggerFactory.getLogger(JobLocationResource.class);

    private static final String ENTITY_NAME = "jobLocation";

    private final JobLocationRepository jobLocationRepository;

    public JobLocationResource(JobLocationRepository jobLocationRepository) {
        this.jobLocationRepository = jobLocationRepository;
    }

    /**
     * POST  /job-locations : Create a new jobLocation.
     *
     * @param jobLocation the jobLocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobLocation, or with status 400 (Bad Request) if the jobLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-locations")
    @Timed
    public ResponseEntity<JobLocation> createJobLocation(@RequestBody JobLocation jobLocation) throws URISyntaxException {
        log.debug("REST request to save JobLocation : {}", jobLocation);
        if (jobLocation.getId() != null) {
            throw new BadRequestAlertException("A new jobLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobLocation result = jobLocationRepository.save(jobLocation);
        return ResponseEntity.created(new URI("/api/job-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-locations : Updates an existing jobLocation.
     *
     * @param jobLocation the jobLocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobLocation,
     * or with status 400 (Bad Request) if the jobLocation is not valid,
     * or with status 500 (Internal Server Error) if the jobLocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-locations")
    @Timed
    public ResponseEntity<JobLocation> updateJobLocation(@RequestBody JobLocation jobLocation) throws URISyntaxException {
        log.debug("REST request to update JobLocation : {}", jobLocation);
        if (jobLocation.getId() == null) {
            return createJobLocation(jobLocation);
        }
        JobLocation result = jobLocationRepository.save(jobLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobLocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-locations : get all the jobLocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobLocations in body
     */
    @GetMapping("/job-locations")
    @Timed
    public List<JobLocation> getAllJobLocations() {
        log.debug("REST request to get all JobLocations");
        return jobLocationRepository.findAll();
        }

    /**
     * GET  /job-locations/:id : get the "id" jobLocation.
     *
     * @param id the id of the jobLocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobLocation, or with status 404 (Not Found)
     */
    @GetMapping("/job-locations/{id}")
    @Timed
    public ResponseEntity<JobLocation> getJobLocation(@PathVariable Long id) {
        log.debug("REST request to get JobLocation : {}", id);
        JobLocation jobLocation = jobLocationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobLocation));
    }

    /**
     * DELETE  /job-locations/:id : delete the "id" jobLocation.
     *
     * @param id the id of the jobLocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobLocation(@PathVariable Long id) {
        log.debug("REST request to delete JobLocation : {}", id);
        jobLocationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
