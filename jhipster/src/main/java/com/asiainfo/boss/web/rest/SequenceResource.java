package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.Sequence;

import com.asiainfo.boss.repository.SequenceRepository;
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
 * REST controller for managing Sequence.
 */
@RestController
@RequestMapping("/api")
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);

    private static final String ENTITY_NAME = "sequence";

    private final SequenceRepository sequenceRepository;

    public SequenceResource(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    /**
     * POST  /sequences : Create a new sequence.
     *
     * @param sequence the sequence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sequence, or with status 400 (Bad Request) if the sequence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sequences")
    @Timed
    public ResponseEntity<Sequence> createSequence(@RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequence);
        if (sequence.getId() != null) {
            throw new BadRequestAlertException("A new sequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sequence result = sequenceRepository.save(sequence);
        return ResponseEntity.created(new URI("/api/sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sequences : Updates an existing sequence.
     *
     * @param sequence the sequence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sequence,
     * or with status 400 (Bad Request) if the sequence is not valid,
     * or with status 500 (Internal Server Error) if the sequence couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sequences")
    @Timed
    public ResponseEntity<Sequence> updateSequence(@RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequence);
        if (sequence.getId() == null) {
            return createSequence(sequence);
        }
        Sequence result = sequenceRepository.save(sequence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sequence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sequences : get all the sequences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sequences in body
     */
    @GetMapping("/sequences")
    @Timed
    public List<Sequence> getAllSequences() {
        log.debug("REST request to get all Sequences");
        return sequenceRepository.findAll();
        }

    /**
     * GET  /sequences/:id : get the "id" sequence.
     *
     * @param id the id of the sequence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sequence, or with status 404 (Not Found)
     */
    @GetMapping("/sequences/{id}")
    @Timed
    public ResponseEntity<Sequence> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        Sequence sequence = sequenceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sequence));
    }

    /**
     * DELETE  /sequences/:id : delete the "id" sequence.
     *
     * @param id the id of the sequence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sequences/{id}")
    @Timed
    public ResponseEntity<Void> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
