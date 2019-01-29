package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.UserAlert;

import com.asiainfo.boss.repository.UserAlertRepository;
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
 * REST controller for managing UserAlert.
 */
@RestController
@RequestMapping("/api")
public class UserAlertResource {

    private final Logger log = LoggerFactory.getLogger(UserAlertResource.class);

    private static final String ENTITY_NAME = "userAlert";

    private final UserAlertRepository userAlertRepository;

    public UserAlertResource(UserAlertRepository userAlertRepository) {
        this.userAlertRepository = userAlertRepository;
    }

    /**
     * POST  /user-alerts : Create a new userAlert.
     *
     * @param userAlert the userAlert to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userAlert, or with status 400 (Bad Request) if the userAlert has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-alerts")
    @Timed
    public ResponseEntity<UserAlert> createUserAlert(@RequestBody UserAlert userAlert) throws URISyntaxException {
        log.debug("REST request to save UserAlert : {}", userAlert);
        if (userAlert.getId() != null) {
            throw new BadRequestAlertException("A new userAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAlert result = userAlertRepository.save(userAlert);
        return ResponseEntity.created(new URI("/api/user-alerts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-alerts : Updates an existing userAlert.
     *
     * @param userAlert the userAlert to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userAlert,
     * or with status 400 (Bad Request) if the userAlert is not valid,
     * or with status 500 (Internal Server Error) if the userAlert couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-alerts")
    @Timed
    public ResponseEntity<UserAlert> updateUserAlert(@RequestBody UserAlert userAlert) throws URISyntaxException {
        log.debug("REST request to update UserAlert : {}", userAlert);
        if (userAlert.getId() == null) {
            return createUserAlert(userAlert);
        }
        UserAlert result = userAlertRepository.save(userAlert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userAlert.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-alerts : get all the userAlerts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userAlerts in body
     */
    @GetMapping("/user-alerts")
    @Timed
    public List<UserAlert> getAllUserAlerts() {
        log.debug("REST request to get all UserAlerts");
        return userAlertRepository.findAll();
        }

    /**
     * GET  /user-alerts/:id : get the "id" userAlert.
     *
     * @param id the id of the userAlert to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userAlert, or with status 404 (Not Found)
     */
    @GetMapping("/user-alerts/{id}")
    @Timed
    public ResponseEntity<UserAlert> getUserAlert(@PathVariable Long id) {
        log.debug("REST request to get UserAlert : {}", id);
        UserAlert userAlert = userAlertRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userAlert));
    }

    /**
     * DELETE  /user-alerts/:id : delete the "id" userAlert.
     *
     * @param id the id of the userAlert to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-alerts/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserAlert(@PathVariable Long id) {
        log.debug("REST request to delete UserAlert : {}", id);
        userAlertRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
