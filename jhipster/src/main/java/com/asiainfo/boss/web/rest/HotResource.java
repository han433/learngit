package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.Hot;

import com.asiainfo.boss.repository.HotRepository;
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
 * REST controller for managing Hot.
 */
@RestController
@RequestMapping("/api")
public class HotResource {

    private final Logger log = LoggerFactory.getLogger(HotResource.class);

    private static final String ENTITY_NAME = "hot";

    private final HotRepository hotRepository;

    public HotResource(HotRepository hotRepository) {
        this.hotRepository = hotRepository;
    }

    /**
     * POST  /hots : Create a new hot.
     *
     * @param hot the hot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hot, or with status 400 (Bad Request) if the hot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hots")
    @Timed
    public ResponseEntity<Hot> createHot(@RequestBody Hot hot) throws URISyntaxException {
        log.debug("REST request to save Hot : {}", hot);
        if (hot.getId() != null) {
            throw new BadRequestAlertException("A new hot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hot result = hotRepository.save(hot);
        return ResponseEntity.created(new URI("/api/hots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hots : Updates an existing hot.
     *
     * @param hot the hot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hot,
     * or with status 400 (Bad Request) if the hot is not valid,
     * or with status 500 (Internal Server Error) if the hot couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hots")
    @Timed
    public ResponseEntity<Hot> updateHot(@RequestBody Hot hot) throws URISyntaxException {
        log.debug("REST request to update Hot : {}", hot);
        if (hot.getId() == null) {
            return createHot(hot);
        }
        Hot result = hotRepository.save(hot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hots : get all the hots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hots in body
     */
    @GetMapping("/hots")
    @Timed
    public List<Hot> getAllHots() {
        log.debug("REST request to get all Hots");
        return hotRepository.findAll();
        }

    /**
     * GET  /hots/:id : get the "id" hot.
     *
     * @param id the id of the hot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hot, or with status 404 (Not Found)
     */
    @GetMapping("/hots/{id}")
    @Timed
    public ResponseEntity<Hot> getHot(@PathVariable Long id) {
        log.debug("REST request to get Hot : {}", id);
        Hot hot = hotRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hot));
    }

    /**
     * DELETE  /hots/:id : delete the "id" hot.
     *
     * @param id the id of the hot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hots/{id}")
    @Timed
    public ResponseEntity<Void> deleteHot(@PathVariable Long id) {
        log.debug("REST request to delete Hot : {}", id);
        hotRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
