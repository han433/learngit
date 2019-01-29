package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.TeamUser;

import com.asiainfo.boss.repository.TeamUserRepository;
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
 * REST controller for managing TeamUser.
 */
@RestController
@RequestMapping("/api")
public class TeamUserResource {

    private final Logger log = LoggerFactory.getLogger(TeamUserResource.class);

    private static final String ENTITY_NAME = "teamUser";

    private final TeamUserRepository teamUserRepository;

    public TeamUserResource(TeamUserRepository teamUserRepository) {
        this.teamUserRepository = teamUserRepository;
    }

    /**
     * POST  /team-users : Create a new teamUser.
     *
     * @param teamUser the teamUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamUser, or with status 400 (Bad Request) if the teamUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-users")
    @Timed
    public ResponseEntity<TeamUser> createTeamUser(@RequestBody TeamUser teamUser) throws URISyntaxException {
        log.debug("REST request to save TeamUser : {}", teamUser);
        if (teamUser.getId() != null) {
            throw new BadRequestAlertException("A new teamUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamUser result = teamUserRepository.save(teamUser);
        return ResponseEntity.created(new URI("/api/team-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-users : Updates an existing teamUser.
     *
     * @param teamUser the teamUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamUser,
     * or with status 400 (Bad Request) if the teamUser is not valid,
     * or with status 500 (Internal Server Error) if the teamUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-users")
    @Timed
    public ResponseEntity<TeamUser> updateTeamUser(@RequestBody TeamUser teamUser) throws URISyntaxException {
        log.debug("REST request to update TeamUser : {}", teamUser);
        if (teamUser.getId() == null) {
            return createTeamUser(teamUser);
        }
        TeamUser result = teamUserRepository.save(teamUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-users : get all the teamUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teamUsers in body
     */
    @GetMapping("/team-users")
    @Timed
    public List<TeamUser> getAllTeamUsers() {
        log.debug("REST request to get all TeamUsers");
        return teamUserRepository.findAll();
        }

    /**
     * GET  /team-users/:id : get the "id" teamUser.
     *
     * @param id the id of the teamUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamUser, or with status 404 (Not Found)
     */
    @GetMapping("/team-users/{id}")
    @Timed
    public ResponseEntity<TeamUser> getTeamUser(@PathVariable Long id) {
        log.debug("REST request to get TeamUser : {}", id);
        TeamUser teamUser = teamUserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamUser));
    }

    /**
     * DELETE  /team-users/:id : delete the "id" teamUser.
     *
     * @param id the id of the teamUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeamUser(@PathVariable Long id) {
        log.debug("REST request to delete TeamUser : {}", id);
        teamUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
