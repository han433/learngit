package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.Vc_agreement;

import com.asiainfo.boss.repository.Vc_agreementRepository;
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
 * REST controller for managing Vc_agreement.
 */
@RestController
@RequestMapping("/api")
public class Vc_agreementResource {

    private final Logger log = LoggerFactory.getLogger(Vc_agreementResource.class);

    private static final String ENTITY_NAME = "vc_agreement";

    private final Vc_agreementRepository vc_agreementRepository;

    public Vc_agreementResource(Vc_agreementRepository vc_agreementRepository) {
        this.vc_agreementRepository = vc_agreementRepository;
    }

    /**
     * POST  /vc-agreements : Create a new vc_agreement.
     *
     * @param vc_agreement the vc_agreement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vc_agreement, or with status 400 (Bad Request) if the vc_agreement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vc-agreements")
    @Timed
    public ResponseEntity<Vc_agreement> createVc_agreement(@RequestBody Vc_agreement vc_agreement) throws URISyntaxException {
        log.debug("REST request to save Vc_agreement : {}", vc_agreement);
        if (vc_agreement.getId() != null) {
            throw new BadRequestAlertException("A new vc_agreement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vc_agreement result = vc_agreementRepository.save(vc_agreement);
        return ResponseEntity.created(new URI("/api/vc-agreements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vc-agreements : Updates an existing vc_agreement.
     *
     * @param vc_agreement the vc_agreement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vc_agreement,
     * or with status 400 (Bad Request) if the vc_agreement is not valid,
     * or with status 500 (Internal Server Error) if the vc_agreement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vc-agreements")
    @Timed
    public ResponseEntity<Vc_agreement> updateVc_agreement(@RequestBody Vc_agreement vc_agreement) throws URISyntaxException {
        log.debug("REST request to update Vc_agreement : {}", vc_agreement);
        if (vc_agreement.getId() == null) {
            return createVc_agreement(vc_agreement);
        }
        Vc_agreement result = vc_agreementRepository.save(vc_agreement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vc_agreement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vc-agreements : get all the vc_agreements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vc_agreements in body
     */
    @GetMapping("/vc-agreements")
    @Timed
    public List<Vc_agreement> getAllVc_agreements() {
        log.debug("REST request to get all Vc_agreements");
        return vc_agreementRepository.findAll();
        }

    /**
     * GET  /vc-agreements/:id : get the "id" vc_agreement.
     *
     * @param id the id of the vc_agreement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vc_agreement, or with status 404 (Not Found)
     */
    @GetMapping("/vc-agreements/{id}")
    @Timed
    public ResponseEntity<Vc_agreement> getVc_agreement(@PathVariable Long id) {
        log.debug("REST request to get Vc_agreement : {}", id);
        Vc_agreement vc_agreement = vc_agreementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vc_agreement));
    }

    /**
     * DELETE  /vc-agreements/:id : delete the "id" vc_agreement.
     *
     * @param id the id of the vc_agreement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vc-agreements/{id}")
    @Timed
    public ResponseEntity<Void> deleteVc_agreement(@PathVariable Long id) {
        log.debug("REST request to delete Vc_agreement : {}", id);
        vc_agreementRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
