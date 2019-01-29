package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.TalentLocation;

import com.asiainfo.boss.repository.TalentLocationRepository;
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
 * REST controller for managing TalentLocation.
 */
@RestController
@RequestMapping("/api")
public class TalentLocationResource {

    private final Logger log = LoggerFactory.getLogger(TalentLocationResource.class);

    private static final String ENTITY_NAME = "talentLocation";

    private final TalentLocationRepository talentLocationRepository;

    public TalentLocationResource(TalentLocationRepository talentLocationRepository) {
        this.talentLocationRepository = talentLocationRepository;
    }

    /**
     * POST  /talent-locations : Create a new talentLocation.
     *
     * @param talentLocation the talentLocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new talentLocation, or with status 400 (Bad Request) if the talentLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/talent-locations")
    @Timed
    public ResponseEntity<TalentLocation> createTalentLocation(@RequestBody TalentLocation talentLocation) throws URISyntaxException {
        log.debug("REST request to save TalentLocation : {}", talentLocation);
        if (talentLocation.getId() != null) {
            throw new BadRequestAlertException("A new talentLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TalentLocation result = talentLocationRepository.save(talentLocation);
        return ResponseEntity.created(new URI("/api/talent-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /talent-locations : Updates an existing talentLocation.
     *
     * @param talentLocation the talentLocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated talentLocation,
     * or with status 400 (Bad Request) if the talentLocation is not valid,
     * or with status 500 (Internal Server Error) if the talentLocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/talent-locations")
    @Timed
    public ResponseEntity<TalentLocation> updateTalentLocation(@RequestBody TalentLocation talentLocation) throws URISyntaxException {
        log.debug("REST request to update TalentLocation : {}", talentLocation);
        if (talentLocation.getId() == null) {
            return createTalentLocation(talentLocation);
        }
        TalentLocation result = talentLocationRepository.save(talentLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, talentLocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /talent-locations : get all the talentLocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of talentLocations in body
     */
    @GetMapping("/talent-locations")
    @Timed
    public List<TalentLocation> getAllTalentLocations() {
        log.debug("REST request to get all TalentLocations");
        return talentLocationRepository.findAll();
        }

    /**
     * GET  /talent-locations/:id : get the "id" talentLocation.
     *
     * @param id the id of the talentLocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the talentLocation, or with status 404 (Not Found)
     */
    @GetMapping("/talent-locations/{id}")
    @Timed
    public ResponseEntity<TalentLocation> getTalentLocation(@PathVariable Long id) {
        log.debug("REST request to get TalentLocation : {}", id);
        TalentLocation talentLocation = talentLocationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(talentLocation));
    }

    /**
     * DELETE  /talent-locations/:id : delete the "id" talentLocation.
     *
     * @param id the id of the talentLocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/talent-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTalentLocation(@PathVariable Long id) {
        log.debug("REST request to delete TalentLocation : {}", id);
        talentLocationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
