package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.CreditTransaction;
import com.asiainfo.boss.repository.CreditTransactionRepository;
import com.asiainfo.boss.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.asiainfo.boss.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CreditTransactionResource REST controller.
 *
 * @see CreditTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class CreditTransactionResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    @Autowired
    private CreditTransactionRepository creditTransactionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCreditTransactionMockMvc;

    private CreditTransaction creditTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreditTransactionResource creditTransactionResource = new CreditTransactionResource(creditTransactionRepository);
        this.restCreditTransactionMockMvc = MockMvcBuilders.standaloneSetup(creditTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditTransaction createEntity(EntityManager em) {
        CreditTransaction creditTransaction = new CreditTransaction()
            .userId(DEFAULT_USER_ID)
            .tenantId(DEFAULT_TENANT_ID);
        return creditTransaction;
    }

    @Before
    public void initTest() {
        creditTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreditTransaction() throws Exception {
        int databaseSizeBeforeCreate = creditTransactionRepository.findAll().size();

        // Create the CreditTransaction
        restCreditTransactionMockMvc.perform(post("/api/credit-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditTransaction)))
            .andExpect(status().isCreated());

        // Validate the CreditTransaction in the database
        List<CreditTransaction> creditTransactionList = creditTransactionRepository.findAll();
        assertThat(creditTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        CreditTransaction testCreditTransaction = creditTransactionList.get(creditTransactionList.size() - 1);
        assertThat(testCreditTransaction.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCreditTransaction.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
    }

    @Test
    @Transactional
    public void createCreditTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creditTransactionRepository.findAll().size();

        // Create the CreditTransaction with an existing ID
        creditTransaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditTransactionMockMvc.perform(post("/api/credit-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the CreditTransaction in the database
        List<CreditTransaction> creditTransactionList = creditTransactionRepository.findAll();
        assertThat(creditTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCreditTransactions() throws Exception {
        // Initialize the database
        creditTransactionRepository.saveAndFlush(creditTransaction);

        // Get all the creditTransactionList
        restCreditTransactionMockMvc.perform(get("/api/credit-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getCreditTransaction() throws Exception {
        // Initialize the database
        creditTransactionRepository.saveAndFlush(creditTransaction);

        // Get the creditTransaction
        restCreditTransactionMockMvc.perform(get("/api/credit-transactions/{id}", creditTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creditTransaction.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCreditTransaction() throws Exception {
        // Get the creditTransaction
        restCreditTransactionMockMvc.perform(get("/api/credit-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreditTransaction() throws Exception {
        // Initialize the database
        creditTransactionRepository.saveAndFlush(creditTransaction);
        int databaseSizeBeforeUpdate = creditTransactionRepository.findAll().size();

        // Update the creditTransaction
        CreditTransaction updatedCreditTransaction = creditTransactionRepository.findOne(creditTransaction.getId());
        // Disconnect from session so that the updates on updatedCreditTransaction are not directly saved in db
        em.detach(updatedCreditTransaction);
        updatedCreditTransaction
            .userId(UPDATED_USER_ID)
            .tenantId(UPDATED_TENANT_ID);

        restCreditTransactionMockMvc.perform(put("/api/credit-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCreditTransaction)))
            .andExpect(status().isOk());

        // Validate the CreditTransaction in the database
        List<CreditTransaction> creditTransactionList = creditTransactionRepository.findAll();
        assertThat(creditTransactionList).hasSize(databaseSizeBeforeUpdate);
        CreditTransaction testCreditTransaction = creditTransactionList.get(creditTransactionList.size() - 1);
        assertThat(testCreditTransaction.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCreditTransaction.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCreditTransaction() throws Exception {
        int databaseSizeBeforeUpdate = creditTransactionRepository.findAll().size();

        // Create the CreditTransaction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCreditTransactionMockMvc.perform(put("/api/credit-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditTransaction)))
            .andExpect(status().isCreated());

        // Validate the CreditTransaction in the database
        List<CreditTransaction> creditTransactionList = creditTransactionRepository.findAll();
        assertThat(creditTransactionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCreditTransaction() throws Exception {
        // Initialize the database
        creditTransactionRepository.saveAndFlush(creditTransaction);
        int databaseSizeBeforeDelete = creditTransactionRepository.findAll().size();

        // Get the creditTransaction
        restCreditTransactionMockMvc.perform(delete("/api/credit-transactions/{id}", creditTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CreditTransaction> creditTransactionList = creditTransactionRepository.findAll();
        assertThat(creditTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditTransaction.class);
        CreditTransaction creditTransaction1 = new CreditTransaction();
        creditTransaction1.setId(1L);
        CreditTransaction creditTransaction2 = new CreditTransaction();
        creditTransaction2.setId(creditTransaction1.getId());
        assertThat(creditTransaction1).isEqualTo(creditTransaction2);
        creditTransaction2.setId(2L);
        assertThat(creditTransaction1).isNotEqualTo(creditTransaction2);
        creditTransaction1.setId(null);
        assertThat(creditTransaction1).isNotEqualTo(creditTransaction2);
    }
}
