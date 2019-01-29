package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.Event_user;

import com.asiainfo.boss.repository.Event_userRepository;
import com.asiainfo.boss.web.rest.errors.BadRequestAlertException;
import com.asiainfo.boss.web.rest.util.HeaderUtil;
import com.asiainfo.boss.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Event_user.
 */
@RestController
@RequestMapping("/api")
public class Event_userResource {

    private final Logger log = LoggerFactory.getLogger(Event_userResource.class);

    private static final String ENTITY_NAME = "event_user";

    private final Event_userRepository event_userRepository;

    public Event_userResource(Event_userRepository event_userRepository) {
        this.event_userRepository = event_userRepository;
    }

    /**
     * POST  /event-users : Create a new event_user.
     *
     * @param event_user the event_user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new event_user, or with status 400 (Bad Request) if the event_user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-users")
    @Timed
    public ResponseEntity<Event_user> createEvent_user(@RequestBody Event_user event_user) throws URISyntaxException {
        log.debug("REST request to save Event_user : {}", event_user);
        if (event_user.getId() != null) {
            throw new BadRequestAlertException("A new event_user cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Event_user result = event_userRepository.save(event_user);
        return ResponseEntity.created(new URI("/api/event-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /event-users : Updates an existing event_user.
     *
     * @param event_user the event_user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated event_user,
     * or with status 400 (Bad Request) if the event_user is not valid,
     * or with status 500 (Internal Server Error) if the event_user couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/event-users")
    @Timed
    public ResponseEntity<Event_user> updateEvent_user(@RequestBody Event_user event_user) throws URISyntaxException {
        log.debug("REST request to update Event_user : {}", event_user);
        if (event_user.getId() == null) {
            return createEvent_user(event_user);
        }
        Event_user result = event_userRepository.save(event_user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, event_user.getId().toString()))
            .body(result);
    }

    /**
     * GET  /event-users : get all the event_users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of event_users in body
     */
    @GetMapping("/event-users")
    @Timed
    public ResponseEntity<List<Event_user>> getAllEvent_users(Pageable pageable) {
        log.debug("REST request to get a page of Event_users");
        Page<Event_user> page = event_userRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/event-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /event-users/:id : get the "id" event_user.
     *
     * @param id the id of the event_user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the event_user, or with status 404 (Not Found)
     */
    @GetMapping("/event-users/{id}")
    @Timed
    public ResponseEntity<Event_user> getEvent_user(@PathVariable Long id) {
        log.debug("REST request to get Event_user : {}", id);
        Event_user event_user = event_userRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(event_user));
    }

    /**
     * DELETE  /event-users/:id : delete the "id" event_user.
     *
     * @param id the id of the event_user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/event-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteEvent_user(@PathVariable Long id) {
        log.debug("REST request to delete Event_user : {}", id);
        event_userRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
