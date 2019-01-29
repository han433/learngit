package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.HotTalent;

import com.asiainfo.boss.repository.HotTalentRepository;
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
 * REST controller for managing HotTalent.
 */
@RestController
@RequestMapping("/api")
public class HotTalentResource {

    private final Logger log = LoggerFactory.getLogger(HotTalentResource.class);

    private static final String ENTITY_NAME = "hotTalent";

    private final HotTalentRepository hotTalentRepository;

    public HotTalentResource(HotTalentRepository hotTalentRepository) {
        this.hotTalentRepository = hotTalentRepository;
    }

    /**
     * POST  /hot-talents : Create a new hotTalent.
     *
     * @param hotTalent the hotTalent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hotTalent, or with status 400 (Bad Request) if the hotTalent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hot-talents")
    @Timed
    public ResponseEntity<HotTalent> createHotTalent(@RequestBody HotTalent hotTalent) throws URISyntaxException {
        log.debug("REST request to save HotTalent : {}", hotTalent);
        if (hotTalent.getId() != null) {
            throw new BadRequestAlertException("A new hotTalent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HotTalent result = hotTalentRepository.save(hotTalent);
        return ResponseEntity.created(new URI("/api/hot-talents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hot-talents : Updates an existing hotTalent.
     *
     * @param hotTalent the hotTalent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hotTalent,
     * or with status 400 (Bad Request) if the hotTalent is not valid,
     * or with status 500 (Internal Server Error) if the hotTalent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hot-talents")
    @Timed
    public ResponseEntity<HotTalent> updateHotTalent(@RequestBody HotTalent hotTalent) throws URISyntaxException {
        log.debug("REST request to update HotTalent : {}", hotTalent);
        if (hotTalent.getId() == null) {
            return createHotTalent(hotTalent);
        }
        HotTalent result = hotTalentRepository.save(hotTalent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hotTalent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hot-talents : get all the hotTalents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hotTalents in body
     */
    @GetMapping("/hot-talents")
    @Timed
    public List<HotTalent> getAllHotTalents() {
        log.debug("REST request to get all HotTalents");
        return hotTalentRepository.findAll();
        }

    /**
     * GET  /hot-talents/:id : get the "id" hotTalent.
     *
     * @param id the id of the hotTalent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hotTalent, or with status 404 (Not Found)
     */
    @GetMapping("/hot-talents/{id}")
    @Timed
    public ResponseEntity<HotTalent> getHotTalent(@PathVariable Long id) {
        log.debug("REST request to get HotTalent : {}", id);
        HotTalent hotTalent = hotTalentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotTalent));
    }

    /**
     * DELETE  /hot-talents/:id : delete the "id" hotTalent.
     *
     * @param id the id of the hotTalent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hot-talents/{id}")
    @Timed
    public ResponseEntity<Void> deleteHotTalent(@PathVariable Long id) {
        log.debug("REST request to delete HotTalent : {}", id);
        hotTalentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
