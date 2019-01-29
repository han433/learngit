package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.SummaryReport;
import com.asiainfo.boss.repository.SummaryReportRepository;
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
 * Test class for the SummaryReportResource REST controller.
 *
 * @see SummaryReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class SummaryReportResourceIntTest {

    @Autowired
    private SummaryReportRepository summaryReportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSummaryReportMockMvc;

    private SummaryReport summaryReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SummaryReportResource summaryReportResource = new SummaryReportResource(summaryReportRepository);
        this.restSummaryReportMockMvc = MockMvcBuilders.standaloneSetup(summaryReportResource)
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
    public static SummaryReport createEntity(EntityManager em) {
        SummaryReport summaryReport = new SummaryReport();
        return summaryReport;
    }

    @Before
    public void initTest() {
        summaryReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createSummaryReport() throws Exception {
        int databaseSizeBeforeCreate = summaryReportRepository.findAll().size();

        // Create the SummaryReport
        restSummaryReportMockMvc.perform(post("/api/summary-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(summaryReport)))
            .andExpect(status().isCreated());

        // Validate the SummaryReport in the database
        List<SummaryReport> summaryReportList = summaryReportRepository.findAll();
        assertThat(summaryReportList).hasSize(databaseSizeBeforeCreate + 1);
        SummaryReport testSummaryReport = summaryReportList.get(summaryReportList.size() - 1);
    }

    @Test
    @Transactional
    public void createSummaryReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = summaryReportRepository.findAll().size();

        // Create the SummaryReport with an existing ID
        summaryReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSummaryReportMockMvc.perform(post("/api/summary-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(summaryReport)))
            .andExpect(status().isBadRequest());

        // Validate the SummaryReport in the database
        List<SummaryReport> summaryReportList = summaryReportRepository.findAll();
        assertThat(summaryReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSummaryReports() throws Exception {
        // Initialize the database
        summaryReportRepository.saveAndFlush(summaryReport);

        // Get all the summaryReportList
        restSummaryReportMockMvc.perform(get("/api/summary-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(summaryReport.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSummaryReport() throws Exception {
        // Initialize the database
        summaryReportRepository.saveAndFlush(summaryReport);

        // Get the summaryReport
        restSummaryReportMockMvc.perform(get("/api/summary-reports/{id}", summaryReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(summaryReport.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSummaryReport() throws Exception {
        // Get the summaryReport
        restSummaryReportMockMvc.perform(get("/api/summary-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSummaryReport() throws Exception {
        // Initialize the database
        summaryReportRepository.saveAndFlush(summaryReport);
        int databaseSizeBeforeUpdate = summaryReportRepository.findAll().size();

        // Update the summaryReport
        SummaryReport updatedSummaryReport = summaryReportRepository.findOne(summaryReport.getId());
        // Disconnect from session so that the updates on updatedSummaryReport are not directly saved in db
        em.detach(updatedSummaryReport);

        restSummaryReportMockMvc.perform(put("/api/summary-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSummaryReport)))
            .andExpect(status().isOk());

        // Validate the SummaryReport in the database
        List<SummaryReport> summaryReportList = summaryReportRepository.findAll();
        assertThat(summaryReportList).hasSize(databaseSizeBeforeUpdate);
        SummaryReport testSummaryReport = summaryReportList.get(summaryReportList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingSummaryReport() throws Exception {
        int databaseSizeBeforeUpdate = summaryReportRepository.findAll().size();

        // Create the SummaryReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSummaryReportMockMvc.perform(put("/api/summary-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(summaryReport)))
            .andExpect(status().isCreated());

        // Validate the SummaryReport in the database
        List<SummaryReport> summaryReportList = summaryReportRepository.findAll();
        assertThat(summaryReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSummaryReport() throws Exception {
        // Initialize the database
        summaryReportRepository.saveAndFlush(summaryReport);
        int databaseSizeBeforeDelete = summaryReportRepository.findAll().size();

        // Get the summaryReport
        restSummaryReportMockMvc.perform(delete("/api/summary-reports/{id}", summaryReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SummaryReport> summaryReportList = summaryReportRepository.findAll();
        assertThat(summaryReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SummaryReport.class);
        SummaryReport summaryReport1 = new SummaryReport();
        summaryReport1.setId(1L);
        SummaryReport summaryReport2 = new SummaryReport();
        summaryReport2.setId(summaryReport1.getId());
        assertThat(summaryReport1).isEqualTo(summaryReport2);
        summaryReport2.setId(2L);
        assertThat(summaryReport1).isNotEqualTo(summaryReport2);
        summaryReport1.setId(null);
        assertThat(summaryReport1).isNotEqualTo(summaryReport2);
    }
}
