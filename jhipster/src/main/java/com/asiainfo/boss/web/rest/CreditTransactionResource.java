package com.asiainfo.boss.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.asiainfo.boss.domain.CreditTransaction;

import com.asiainfo.boss.repository.CreditTransactionRepository;
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
 * REST controller for managing CreditTransaction.
 */
@RestController
@RequestMapping("/api")
public class CreditTransactionResource {

    private final Logger log = LoggerFactory.getLogger(CreditTransactionResource.class);

    private static final String ENTITY_NAME = "creditTransaction";

    private final CreditTransactionRepository creditTransactionRepository;

    public CreditTransactionResource(CreditTransactionRepository creditTransactionRepository) {
        this.creditTransactionRepository = creditTransactionRepository;
    }

    /**
     * POST  /credit-transactions : Create a new creditTransaction.
     *
     * @param creditTransaction the creditTransaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creditTransaction, or with status 400 (Bad Request) if the creditTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/credit-transactions")
    @Timed
    public ResponseEntity<CreditTransaction> createCreditTransaction(@RequestBody CreditTransaction creditTransaction) throws URISyntaxException {
        log.debug("REST request to save CreditTransaction : {}", creditTransaction);
        if (creditTransaction.getId() != null) {
            throw new BadRequestAlertException("A new creditTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditTransaction result = creditTransactionRepository.save(creditTransaction);
        return ResponseEntity.created(new URI("/api/credit-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /credit-transactions : Updates an existing creditTransaction.
     *
     * @param creditTransaction the creditTransaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creditTransaction,
     * or with status 400 (Bad Request) if the creditTransaction is not valid,
     * or with status 500 (Internal Server Error) if the creditTransaction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/credit-transactions")
    @Timed
    public ResponseEntity<CreditTransaction> updateCreditTransaction(@RequestBody CreditTransaction creditTransaction) throws URISyntaxException {
        log.debug("REST request to update CreditTransaction : {}", creditTransaction);
        if (creditTransaction.getId() == null) {
            return createCreditTransaction(creditTransaction);
        }
        CreditTransaction result = creditTransactionRepository.save(creditTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creditTransaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /credit-transactions : get all the creditTransactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of creditTransactions in body
     */
    @GetMapping("/credit-transactions")
    @Timed
    public List<CreditTransaction> getAllCreditTransactions() {
        log.debug("REST request to get all CreditTransactions");
        return creditTransactionRepository.findAll();
        }

    /**
     * GET  /credit-transactions/:id : get the "id" creditTransaction.
     *
     * @param id the id of the creditTransaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creditTransaction, or with status 404 (Not Found)
     */
    @GetMapping("/credit-transactions/{id}")
    @Timed
    public ResponseEntity<CreditTransaction> getCreditTransaction(@PathVariable Long id) {
        log.debug("REST request to get CreditTransaction : {}", id);
        CreditTransaction creditTransaction = creditTransactionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(creditTransaction));
    }

    /**
     * DELETE  /credit-transactions/:id : delete the "id" creditTransaction.
     *
     * @param id the id of the creditTransaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/credit-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCreditTransaction(@PathVariable Long id) {
        log.debug("REST request to delete CreditTransaction : {}", id);
        creditTransactionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
