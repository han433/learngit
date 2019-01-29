package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.Github_talent;

import com.asiainfo.boss.repository.Github_talentRepository;
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
 * REST controller for managing Github_talent.
 */
@RestController
@RequestMapping("/api")
public class Github_talentResource {

    private final Logger log = LoggerFactory.getLogger(Github_talentResource.class);

    private static final String ENTITY_NAME = "github_talent";

    private final Github_talentRepository github_talentRepository;

    public Github_talentResource(Github_talentRepository github_talentRepository) {
        this.github_talentRepository = github_talentRepository;
    }

    /**
     * POST  /github-talents : Create a new github_talent.
     *
     * @param github_talent the github_talent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new github_talent, or with status 400 (Bad Request) if the github_talent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/github-talents")
    @Timed
    public ResponseEntity<Github_talent> createGithub_talent(@RequestBody Github_talent github_talent) throws URISyntaxException {
        log.debug("REST request to save Github_talent : {}", github_talent);
        if (github_talent.getId() != null) {
            throw new BadRequestAlertException("A new github_talent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Github_talent result = github_talentRepository.save(github_talent);
        return ResponseEntity.created(new URI("/api/github-talents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /github-talents : Updates an existing github_talent.
     *
     * @param github_talent the github_talent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated github_talent,
     * or with status 400 (Bad Request) if the github_talent is not valid,
     * or with status 500 (Internal Server Error) if the github_talent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/github-talents")
    @Timed
    public ResponseEntity<Github_talent> updateGithub_talent(@RequestBody Github_talent github_talent) throws URISyntaxException {
        log.debug("REST request to update Github_talent : {}", github_talent);
        if (github_talent.getId() == null) {
            return createGithub_talent(github_talent);
        }
        Github_talent result = github_talentRepository.save(github_talent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, github_talent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /github-talents : get all the github_talents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of github_talents in body
     */
    @GetMapping("/github-talents")
    @Timed
    public List<Github_talent> getAllGithub_talents() {
        log.debug("REST request to get all Github_talents");
        return github_talentRepository.findAll();
        }

    /**
     * GET  /github-talents/:id : get the "id" github_talent.
     *
     * @param id the id of the github_talent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the github_talent, or with status 404 (Not Found)
     */
    @GetMapping("/github-talents/{id}")
    @Timed
    public ResponseEntity<Github_talent> getGithub_talent(@PathVariable Long id) {
        log.debug("REST request to get Github_talent : {}", id);
        Github_talent github_talent = github_talentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(github_talent));
    }

    /**
     * DELETE  /github-talents/:id : delete the "id" github_talent.
     *
     * @param id the id of the github_talent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/github-talents/{id}")
    @Timed
    public ResponseEntity<Void> deleteGithub_talent(@PathVariable Long id) {
        log.debug("REST request to delete Github_talent : {}", id);
        github_talentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
