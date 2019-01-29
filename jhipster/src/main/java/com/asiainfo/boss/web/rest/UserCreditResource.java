package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.UserCredit;

import com.asiainfo.boss.repository.UserCreditRepository;
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
 * REST controller for managing UserCredit.
 */
@RestController
@RequestMapping("/api")
public class UserCreditResource {

    private final Logger log = LoggerFactory.getLogger(UserCreditResource.class);

    private static final String ENTITY_NAME = "userCredit";

    private final UserCreditRepository userCreditRepository;

    public UserCreditResource(UserCreditRepository userCreditRepository) {
        this.userCreditRepository = userCreditRepository;
    }

    /**
     * POST  /user-credits : Create a new userCredit.
     *
     * @param userCredit the userCredit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userCredit, or with status 400 (Bad Request) if the userCredit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-credits")
    @Timed
    public ResponseEntity<UserCredit> createUserCredit(@RequestBody UserCredit userCredit) throws URISyntaxException {
        log.debug("REST request to save UserCredit : {}", userCredit);
        if (userCredit.getId() != null) {
            throw new BadRequestAlertException("A new userCredit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserCredit result = userCreditRepository.save(userCredit);
        return ResponseEntity.created(new URI("/api/user-credits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-credits : Updates an existing userCredit.
     *
     * @param userCredit the userCredit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userCredit,
     * or with status 400 (Bad Request) if the userCredit is not valid,
     * or with status 500 (Internal Server Error) if the userCredit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-credits")
    @Timed
    public ResponseEntity<UserCredit> updateUserCredit(@RequestBody UserCredit userCredit) throws URISyntaxException {
        log.debug("REST request to update UserCredit : {}", userCredit);
        if (userCredit.getId() == null) {
            return createUserCredit(userCredit);
        }
        UserCredit result = userCreditRepository.save(userCredit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userCredit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-credits : get all the userCredits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userCredits in body
     */
    @GetMapping("/user-credits")
    @Timed
    public List<UserCredit> getAllUserCredits() {
        log.debug("REST request to get all UserCredits");
        return userCreditRepository.findAll();
        }

    /**
     * GET  /user-credits/:id : get the "id" userCredit.
     *
     * @param id the id of the userCredit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userCredit, or with status 404 (Not Found)
     */
    @GetMapping("/user-credits/{id}")
    @Timed
    public ResponseEntity<UserCredit> getUserCredit(@PathVariable Long id) {
        log.debug("REST request to get UserCredit : {}", id);
        UserCredit userCredit = userCreditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userCredit));
    }

    /**
     * DELETE  /user-credits/:id : delete the "id" userCredit.
     *
     * @param id the id of the userCredit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-credits/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserCredit(@PathVariable Long id) {
        log.debug("REST request to delete UserCredit : {}", id);
        userCreditRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
