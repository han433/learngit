package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.Application_commission;

import com.asiainfo.boss.repository.Application_commissionRepository;
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
 * REST controller for managing Application_commission.
 */
@RestController
@RequestMapping("/api")
public class Application_commissionResource {

    private final Logger log = LoggerFactory.getLogger(Application_commissionResource.class);

    private static final String ENTITY_NAME = "application_commission";

    private final Application_commissionRepository application_commissionRepository;

    public Application_commissionResource(Application_commissionRepository application_commissionRepository) {
        this.application_commissionRepository = application_commissionRepository;
    }

    /**
     * POST  /application-commissions : Create a new application_commission.
     *
     * @param application_commission the application_commission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new application_commission, or with status 400 (Bad Request) if the application_commission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-commissions")
    @Timed
    public ResponseEntity<Application_commission> createApplication_commission(@RequestBody Application_commission application_commission) throws URISyntaxException {
        log.debug("REST request to save Application_commission : {}", application_commission);
        if (application_commission.getId() != null) {
            throw new BadRequestAlertException("A new application_commission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Application_commission result = application_commissionRepository.save(application_commission);
        return ResponseEntity.created(new URI("/api/application-commissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-commissions : Updates an existing application_commission.
     *
     * @param application_commission the application_commission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated application_commission,
     * or with status 400 (Bad Request) if the application_commission is not valid,
     * or with status 500 (Internal Server Error) if the application_commission couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-commissions")
    @Timed
    public ResponseEntity<Application_commission> updateApplication_commission(@RequestBody Application_commission application_commission) throws URISyntaxException {
        log.debug("REST request to update Application_commission : {}", application_commission);
        if (application_commission.getId() == null) {
            return createApplication_commission(application_commission);
        }
        Application_commission result = application_commissionRepository.save(application_commission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, application_commission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-commissions : get all the application_commissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of application_commissions in body
     */
    @GetMapping("/application-commissions")
    @Timed
    public List<Application_commission> getAllApplication_commissions() {
        log.debug("REST request to get all Application_commissions");
        return application_commissionRepository.findAll();
        }

    /**
     * GET  /application-commissions/:id : get the "id" application_commission.
     *
     * @param id the id of the application_commission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the application_commission, or with status 404 (Not Found)
     */
    @GetMapping("/application-commissions/{id}")
    @Timed
    public ResponseEntity<Application_commission> getApplication_commission(@PathVariable Long id) {
        log.debug("REST request to get Application_commission : {}", id);
        Application_commission application_commission = application_commissionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(application_commission));
    }

    /**
     * DELETE  /application-commissions/:id : delete the "id" application_commission.
     *
     * @param id the id of the application_commission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-commissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplication_commission(@PathVariable Long id) {
        log.debug("REST request to delete Application_commission : {}", id);
        application_commissionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
