package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.InvoiceActivity;
import com.asiainfo.boss.repository.InvoiceActivityRepository;
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
 * Test class for the InvoiceActivityResource REST controller.
 *
 * @see InvoiceActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class InvoiceActivityResourceIntTest {

    private static final String DEFAULT_SUB_INVOICE_NO = "AAAAAAAAAA";
    private static final String UPDATED_SUB_INVOICE_NO = "BBBBBBBBBB";

    @Autowired
    private InvoiceActivityRepository invoiceActivityRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceActivityMockMvc;

    private InvoiceActivity invoiceActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceActivityResource invoiceActivityResource = new InvoiceActivityResource(invoiceActivityRepository);
        this.restInvoiceActivityMockMvc = MockMvcBuilders.standaloneSetup(invoiceActivityResource)
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
    public static InvoiceActivity createEntity(EntityManager em) {
        InvoiceActivity invoiceActivity = new InvoiceActivity()
            .subInvoiceNo(DEFAULT_SUB_INVOICE_NO);
        return invoiceActivity;
    }

    @Before
    public void initTest() {
        invoiceActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceActivity() throws Exception {
        int databaseSizeBeforeCreate = invoiceActivityRepository.findAll().size();

        // Create the InvoiceActivity
        restInvoiceActivityMockMvc.perform(post("/api/invoice-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceActivity)))
            .andExpect(status().isCreated());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceActivity testInvoiceActivity = invoiceActivityList.get(invoiceActivityList.size() - 1);
        assertThat(testInvoiceActivity.getSubInvoiceNo()).isEqualTo(DEFAULT_SUB_INVOICE_NO);
    }

    @Test
    @Transactional
    public void createInvoiceActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceActivityRepository.findAll().size();

        // Create the InvoiceActivity with an existing ID
        invoiceActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceActivityMockMvc.perform(post("/api/invoice-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceActivity)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvoiceActivities() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);

        // Get all the invoiceActivityList
        restInvoiceActivityMockMvc.perform(get("/api/invoice-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].subInvoiceNo").value(hasItem(DEFAULT_SUB_INVOICE_NO.toString())));
    }

    @Test
    @Transactional
    public void getInvoiceActivity() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);

        // Get the invoiceActivity
        restInvoiceActivityMockMvc.perform(get("/api/invoice-activities/{id}", invoiceActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceActivity.getId().intValue()))
            .andExpect(jsonPath("$.subInvoiceNo").value(DEFAULT_SUB_INVOICE_NO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoiceActivity() throws Exception {
        // Get the invoiceActivity
        restInvoiceActivityMockMvc.perform(get("/api/invoice-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceActivity() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);
        int databaseSizeBeforeUpdate = invoiceActivityRepository.findAll().size();

        // Update the invoiceActivity
        InvoiceActivity updatedInvoiceActivity = invoiceActivityRepository.findOne(invoiceActivity.getId());
        // Disconnect from session so that the updates on updatedInvoiceActivity are not directly saved in db
        em.detach(updatedInvoiceActivity);
        updatedInvoiceActivity
            .subInvoiceNo(UPDATED_SUB_INVOICE_NO);

        restInvoiceActivityMockMvc.perform(put("/api/invoice-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceActivity)))
            .andExpect(status().isOk());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeUpdate);
        InvoiceActivity testInvoiceActivity = invoiceActivityList.get(invoiceActivityList.size() - 1);
        assertThat(testInvoiceActivity.getSubInvoiceNo()).isEqualTo(UPDATED_SUB_INVOICE_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceActivity() throws Exception {
        int databaseSizeBeforeUpdate = invoiceActivityRepository.findAll().size();

        // Create the InvoiceActivity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceActivityMockMvc.perform(put("/api/invoice-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceActivity)))
            .andExpect(status().isCreated());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoiceActivity() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);
        int databaseSizeBeforeDelete = invoiceActivityRepository.findAll().size();

        // Get the invoiceActivity
        restInvoiceActivityMockMvc.perform(delete("/api/invoice-activities/{id}", invoiceActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceActivity.class);
        InvoiceActivity invoiceActivity1 = new InvoiceActivity();
        invoiceActivity1.setId(1L);
        InvoiceActivity invoiceActivity2 = new InvoiceActivity();
        invoiceActivity2.setId(invoiceActivity1.getId());
        assertThat(invoiceActivity1).isEqualTo(invoiceActivity2);
        invoiceActivity2.setId(2L);
        assertThat(invoiceActivity1).isNotEqualTo(invoiceActivity2);
        invoiceActivity1.setId(null);
        assertThat(invoiceActivity1).isNotEqualTo(invoiceActivity2);
    }
}
