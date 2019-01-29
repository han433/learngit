package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.ApnParam;

import com.asiainfo.boss.repository.ApnParamRepository;
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
 * REST controller for managing ApnParam.
 */
@RestController
@RequestMapping("/api")
public class ApnParamResource {

    private final Logger log = LoggerFactory.getLogger(ApnParamResource.class);

    private static final String ENTITY_NAME = "apnParam";

    private final ApnParamRepository apnParamRepository;

    public ApnParamResource(ApnParamRepository apnParamRepository) {
        this.apnParamRepository = apnParamRepository;
    }

    /**
     * POST  /apn-params : Create a new apnParam.
     *
     * @param apnParam the apnParam to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apnParam, or with status 400 (Bad Request) if the apnParam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apn-params")
    @Timed
    public ResponseEntity<ApnParam> createApnParam(@RequestBody ApnParam apnParam) throws URISyntaxException {
        log.debug("REST request to save ApnParam : {}", apnParam);
        if (apnParam.getId() != null) {
            throw new BadRequestAlertException("A new apnParam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApnParam result = apnParamRepository.save(apnParam);
        return ResponseEntity.created(new URI("/api/apn-params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apn-params : Updates an existing apnParam.
     *
     * @param apnParam the apnParam to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apnParam,
     * or with status 400 (Bad Request) if the apnParam is not valid,
     * or with status 500 (Internal Server Error) if the apnParam couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apn-params")
    @Timed
    public ResponseEntity<ApnParam> updateApnParam(@RequestBody ApnParam apnParam) throws URISyntaxException {
        log.debug("REST request to update ApnParam : {}", apnParam);
        if (apnParam.getId() == null) {
            return createApnParam(apnParam);
        }
        ApnParam result = apnParamRepository.save(apnParam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, apnParam.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apn-params : get all the apnParams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apnParams in body
     */
    @GetMapping("/apn-params")
    @Timed
    public List<ApnParam> getAllApnParams() {
        log.debug("REST request to get all ApnParams");
        return apnParamRepository.findAll();
        }

    /**
     * GET  /apn-params/:id : get the "id" apnParam.
     *
     * @param id the id of the apnParam to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apnParam, or with status 404 (Not Found)
     */
    @GetMapping("/apn-params/{id}")
    @Timed
    public ResponseEntity<ApnParam> getApnParam(@PathVariable Long id) {
        log.debug("REST request to get ApnParam : {}", id);
        ApnParam apnParam = apnParamRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(apnParam));
    }

    /**
     * DELETE  /apn-params/:id : delete the "id" apnParam.
     *
     * @param id the id of the apnParam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apn-params/{id}")
    @Timed
    public ResponseEntity<Void> deleteApnParam(@PathVariable Long id) {
        log.debug("REST request to delete ApnParam : {}", id);
        apnParamRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
