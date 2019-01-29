package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.HotUser;

import com.asiainfo.boss.repository.HotUserRepository;
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
 * REST controller for managing HotUser.
 */
@RestController
@RequestMapping("/api")
public class HotUserResource {

    private final Logger log = LoggerFactory.getLogger(HotUserResource.class);

    private static final String ENTITY_NAME = "hotUser";

    private final HotUserRepository hotUserRepository;

    public HotUserResource(HotUserRepository hotUserRepository) {
        this.hotUserRepository = hotUserRepository;
    }

    /**
     * POST  /hot-users : Create a new hotUser.
     *
     * @param hotUser the hotUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hotUser, or with status 400 (Bad Request) if the hotUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hot-users")
    @Timed
    public ResponseEntity<HotUser> createHotUser(@RequestBody HotUser hotUser) throws URISyntaxException {
        log.debug("REST request to save HotUser : {}", hotUser);
        if (hotUser.getId() != null) {
            throw new BadRequestAlertException("A new hotUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HotUser result = hotUserRepository.save(hotUser);
        return ResponseEntity.created(new URI("/api/hot-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hot-users : Updates an existing hotUser.
     *
     * @param hotUser the hotUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hotUser,
     * or with status 400 (Bad Request) if the hotUser is not valid,
     * or with status 500 (Internal Server Error) if the hotUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hot-users")
    @Timed
    public ResponseEntity<HotUser> updateHotUser(@RequestBody HotUser hotUser) throws URISyntaxException {
        log.debug("REST request to update HotUser : {}", hotUser);
        if (hotUser.getId() == null) {
            return createHotUser(hotUser);
        }
        HotUser result = hotUserRepository.save(hotUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hotUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hot-users : get all the hotUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hotUsers in body
     */
    @GetMapping("/hot-users")
    @Timed
    public List<HotUser> getAllHotUsers() {
        log.debug("REST request to get all HotUsers");
        return hotUserRepository.findAll();
        }

    /**
     * GET  /hot-users/:id : get the "id" hotUser.
     *
     * @param id the id of the hotUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hotUser, or with status 404 (Not Found)
     */
    @GetMapping("/hot-users/{id}")
    @Timed
    public ResponseEntity<HotUser> getHotUser(@PathVariable Long id) {
        log.debug("REST request to get HotUser : {}", id);
        HotUser hotUser = hotUserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hotUser));
    }

    /**
     * DELETE  /hot-users/:id : delete the "id" hotUser.
     *
     * @param id the id of the hotUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hot-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteHotUser(@PathVariable Long id) {
        log.debug("REST request to delete HotUser : {}", id);
        hotUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
