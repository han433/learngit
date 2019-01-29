package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.UserFavoriteTalent;

import com.asiainfo.boss.repository.UserFavoriteTalentRepository;
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
 * REST controller for managing UserFavoriteTalent.
 */
@RestController
@RequestMapping("/api")
public class UserFavoriteTalentResource {

    private final Logger log = LoggerFactory.getLogger(UserFavoriteTalentResource.class);

    private static final String ENTITY_NAME = "userFavoriteTalent";

    private final UserFavoriteTalentRepository userFavoriteTalentRepository;

    public UserFavoriteTalentResource(UserFavoriteTalentRepository userFavoriteTalentRepository) {
        this.userFavoriteTalentRepository = userFavoriteTalentRepository;
    }

    /**
     * POST  /user-favorite-talents : Create a new userFavoriteTalent.
     *
     * @param userFavoriteTalent the userFavoriteTalent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userFavoriteTalent, or with status 400 (Bad Request) if the userFavoriteTalent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-favorite-talents")
    @Timed
    public ResponseEntity<UserFavoriteTalent> createUserFavoriteTalent(@RequestBody UserFavoriteTalent userFavoriteTalent) throws URISyntaxException {
        log.debug("REST request to save UserFavoriteTalent : {}", userFavoriteTalent);
        if (userFavoriteTalent.getId() != null) {
            throw new BadRequestAlertException("A new userFavoriteTalent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserFavoriteTalent result = userFavoriteTalentRepository.save(userFavoriteTalent);
        return ResponseEntity.created(new URI("/api/user-favorite-talents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-favorite-talents : Updates an existing userFavoriteTalent.
     *
     * @param userFavoriteTalent the userFavoriteTalent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userFavoriteTalent,
     * or with status 400 (Bad Request) if the userFavoriteTalent is not valid,
     * or with status 500 (Internal Server Error) if the userFavoriteTalent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-favorite-talents")
    @Timed
    public ResponseEntity<UserFavoriteTalent> updateUserFavoriteTalent(@RequestBody UserFavoriteTalent userFavoriteTalent) throws URISyntaxException {
        log.debug("REST request to update UserFavoriteTalent : {}", userFavoriteTalent);
        if (userFavoriteTalent.getId() == null) {
            return createUserFavoriteTalent(userFavoriteTalent);
        }
        UserFavoriteTalent result = userFavoriteTalentRepository.save(userFavoriteTalent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userFavoriteTalent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-favorite-talents : get all the userFavoriteTalents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userFavoriteTalents in body
     */
    @GetMapping("/user-favorite-talents")
    @Timed
    public List<UserFavoriteTalent> getAllUserFavoriteTalents() {
        log.debug("REST request to get all UserFavoriteTalents");
        return userFavoriteTalentRepository.findAll();
        }

    /**
     * GET  /user-favorite-talents/:id : get the "id" userFavoriteTalent.
     *
     * @param id the id of the userFavoriteTalent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userFavoriteTalent, or with status 404 (Not Found)
     */
    @GetMapping("/user-favorite-talents/{id}")
    @Timed
    public ResponseEntity<UserFavoriteTalent> getUserFavoriteTalent(@PathVariable Long id) {
        log.debug("REST request to get UserFavoriteTalent : {}", id);
        UserFavoriteTalent userFavoriteTalent = userFavoriteTalentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userFavoriteTalent));
    }

    /**
     * DELETE  /user-favorite-talents/:id : delete the "id" userFavoriteTalent.
     *
     * @param id the id of the userFavoriteTalent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-favorite-talents/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserFavoriteTalent(@PathVariable Long id) {
        log.debug("REST request to delete UserFavoriteTalent : {}", id);
        userFavoriteTalentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
