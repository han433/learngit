package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.InvoiceActivity;

import com.asiainfo.boss.repository.InvoiceActivityRepository;
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
 * REST controller for managing InvoiceActivity.
 */
@RestController
@RequestMapping("/api")
public class InvoiceActivityResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceActivityResource.class);

    private static final String ENTITY_NAME = "invoiceActivity";

    private final InvoiceActivityRepository invoiceActivityRepository;

    public InvoiceActivityResource(InvoiceActivityRepository invoiceActivityRepository) {
        this.invoiceActivityRepository = invoiceActivityRepository;
    }

    /**
     * POST  /invoice-activities : Create a new invoiceActivity.
     *
     * @param invoiceActivity the invoiceActivity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceActivity, or with status 400 (Bad Request) if the invoiceActivity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-activities")
    @Timed
    public ResponseEntity<InvoiceActivity> createInvoiceActivity(@RequestBody InvoiceActivity invoiceActivity) throws URISyntaxException {
        log.debug("REST request to save InvoiceActivity : {}", invoiceActivity);
        if (invoiceActivity.getId() != null) {
            throw new BadRequestAlertException("A new invoiceActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceActivity result = invoiceActivityRepository.save(invoiceActivity);
        return ResponseEntity.created(new URI("/api/invoice-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-activities : Updates an existing invoiceActivity.
     *
     * @param invoiceActivity the invoiceActivity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceActivity,
     * or with status 400 (Bad Request) if the invoiceActivity is not valid,
     * or with status 500 (Internal Server Error) if the invoiceActivity couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-activities")
    @Timed
    public ResponseEntity<InvoiceActivity> updateInvoiceActivity(@RequestBody InvoiceActivity invoiceActivity) throws URISyntaxException {
        log.debug("REST request to update InvoiceActivity : {}", invoiceActivity);
        if (invoiceActivity.getId() == null) {
            return createInvoiceActivity(invoiceActivity);
        }
        InvoiceActivity result = invoiceActivityRepository.save(invoiceActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceActivity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-activities : get all the invoiceActivities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceActivities in body
     */
    @GetMapping("/invoice-activities")
    @Timed
    public List<InvoiceActivity> getAllInvoiceActivities() {
        log.debug("REST request to get all InvoiceActivities");
        return invoiceActivityRepository.findAll();
        }

    /**
     * GET  /invoice-activities/:id : get the "id" invoiceActivity.
     *
     * @param id the id of the invoiceActivity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceActivity, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-activities/{id}")
    @Timed
    public ResponseEntity<InvoiceActivity> getInvoiceActivity(@PathVariable Long id) {
        log.debug("REST request to get InvoiceActivity : {}", id);
        InvoiceActivity invoiceActivity = invoiceActivityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invoiceActivity));
    }

    /**
     * DELETE  /invoice-activities/:id : delete the "id" invoiceActivity.
     *
     * @param id the id of the invoiceActivity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-activities/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceActivity(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceActivity : {}", id);
        invoiceActivityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
