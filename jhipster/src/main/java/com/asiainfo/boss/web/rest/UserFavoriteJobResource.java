package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.UserFavoriteJob;

import com.asiainfo.boss.repository.UserFavoriteJobRepository;
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
 * REST controller for managing UserFavoriteJob.
 */
@RestController
@RequestMapping("/api")
public class UserFavoriteJobResource {

    private final Logger log = LoggerFactory.getLogger(UserFavoriteJobResource.class);

    private static final String ENTITY_NAME = "userFavoriteJob";

    private final UserFavoriteJobRepository userFavoriteJobRepository;

    public UserFavoriteJobResource(UserFavoriteJobRepository userFavoriteJobRepository) {
        this.userFavoriteJobRepository = userFavoriteJobRepository;
    }

    /**
     * POST  /user-favorite-jobs : Create a new userFavoriteJob.
     *
     * @param userFavoriteJob the userFavoriteJob to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userFavoriteJob, or with status 400 (Bad Request) if the userFavoriteJob has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-favorite-jobs")
    @Timed
    public ResponseEntity<UserFavoriteJob> createUserFavoriteJob(@RequestBody UserFavoriteJob userFavoriteJob) throws URISyntaxException {
        log.debug("REST request to save UserFavoriteJob : {}", userFavoriteJob);
        if (userFavoriteJob.getId() != null) {
            throw new BadRequestAlertException("A new userFavoriteJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserFavoriteJob result = userFavoriteJobRepository.save(userFavoriteJob);
        return ResponseEntity.created(new URI("/api/user-favorite-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-favorite-jobs : Updates an existing userFavoriteJob.
     *
     * @param userFavoriteJob the userFavoriteJob to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userFavoriteJob,
     * or with status 400 (Bad Request) if the userFavoriteJob is not valid,
     * or with status 500 (Internal Server Error) if the userFavoriteJob couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-favorite-jobs")
    @Timed
    public ResponseEntity<UserFavoriteJob> updateUserFavoriteJob(@RequestBody UserFavoriteJob userFavoriteJob) throws URISyntaxException {
        log.debug("REST request to update UserFavoriteJob : {}", userFavoriteJob);
        if (userFavoriteJob.getId() == null) {
            return createUserFavoriteJob(userFavoriteJob);
        }
        UserFavoriteJob result = userFavoriteJobRepository.save(userFavoriteJob);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userFavoriteJob.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-favorite-jobs : get all the userFavoriteJobs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userFavoriteJobs in body
     */
    @GetMapping("/user-favorite-jobs")
    @Timed
    public List<UserFavoriteJob> getAllUserFavoriteJobs() {
        log.debug("REST request to get all UserFavoriteJobs");
        return userFavoriteJobRepository.findAll();
        }

    /**
     * GET  /user-favorite-jobs/:id : get the "id" userFavoriteJob.
     *
     * @param id the id of the userFavoriteJob to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userFavoriteJob, or with status 404 (Not Found)
     */
    @GetMapping("/user-favorite-jobs/{id}")
    @Timed
    public ResponseEntity<UserFavoriteJob> getUserFavoriteJob(@PathVariable Long id) {
        log.debug("REST request to get UserFavoriteJob : {}", id);
        UserFavoriteJob userFavoriteJob = userFavoriteJobRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userFavoriteJob));
    }

    /**
     * DELETE  /user-favorite-jobs/:id : delete the "id" userFavoriteJob.
     *
     * @param id the id of the userFavoriteJob to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-favorite-jobs/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserFavoriteJob(@PathVariable Long id) {
        log.debug("REST request to delete UserFavoriteJob : {}", id);
        userFavoriteJobRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
