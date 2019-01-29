package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.TrendReport;
import com.asiainfo.boss.repository.TrendReportRepository;
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
 * Test class for the TrendReportResource REST controller.
 *
 * @see TrendReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class TrendReportResourceIntTest {

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_HIRE = "AAAAAAAAAA";
    private static final String UPDATED_HIRE = "BBBBBBBBBB";

    @Autowired
    private TrendReportRepository trendReportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrendReportMockMvc;

    private TrendReport trendReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrendReportResource trendReportResource = new TrendReportResource(trendReportRepository);
        this.restTrendReportMockMvc = MockMvcBuilders.standaloneSetup(trendReportResource)
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
    public static TrendReport createEntity(EntityManager em) {
        TrendReport trendReport = new TrendReport()
            .company(DEFAULT_COMPANY)
            .hire(DEFAULT_HIRE);
        return trendReport;
    }

    @Before
    public void initTest() {
        trendReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrendReport() throws Exception {
        int databaseSizeBeforeCreate = trendReportRepository.findAll().size();

        // Create the TrendReport
        restTrendReportMockMvc.perform(post("/api/trend-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trendReport)))
            .andExpect(status().isCreated());

        // Validate the TrendReport in the database
        List<TrendReport> trendReportList = trendReportRepository.findAll();
        assertThat(trendReportList).hasSize(databaseSizeBeforeCreate + 1);
        TrendReport testTrendReport = trendReportList.get(trendReportList.size() - 1);
        assertThat(testTrendReport.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testTrendReport.getHire()).isEqualTo(DEFAULT_HIRE);
    }

    @Test
    @Transactional
    public void createTrendReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trendReportRepository.findAll().size();

        // Create the TrendReport with an existing ID
        trendReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrendReportMockMvc.perform(post("/api/trend-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trendReport)))
            .andExpect(status().isBadRequest());

        // Validate the TrendReport in the database
        List<TrendReport> trendReportList = trendReportRepository.findAll();
        assertThat(trendReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTrendReports() throws Exception {
        // Initialize the database
        trendReportRepository.saveAndFlush(trendReport);

        // Get all the trendReportList
        restTrendReportMockMvc.perform(get("/api/trend-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trendReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())))
            .andExpect(jsonPath("$.[*].hire").value(hasItem(DEFAULT_HIRE.toString())));
    }

    @Test
    @Transactional
    public void getTrendReport() throws Exception {
        // Initialize the database
        trendReportRepository.saveAndFlush(trendReport);

        // Get the trendReport
        restTrendReportMockMvc.perform(get("/api/trend-reports/{id}", trendReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trendReport.getId().intValue()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()))
            .andExpect(jsonPath("$.hire").value(DEFAULT_HIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTrendReport() throws Exception {
        // Get the trendReport
        restTrendReportMockMvc.perform(get("/api/trend-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrendReport() throws Exception {
        // Initialize the database
        trendReportRepository.saveAndFlush(trendReport);
        int databaseSizeBeforeUpdate = trendReportRepository.findAll().size();

        // Update the trendReport
        TrendReport updatedTrendReport = trendReportRepository.findOne(trendReport.getId());
        // Disconnect from session so that the updates on updatedTrendReport are not directly saved in db
        em.detach(updatedTrendReport);
        updatedTrendReport
            .company(UPDATED_COMPANY)
            .hire(UPDATED_HIRE);

        restTrendReportMockMvc.perform(put("/api/trend-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrendReport)))
            .andExpect(status().isOk());

        // Validate the TrendReport in the database
        List<TrendReport> trendReportList = trendReportRepository.findAll();
        assertThat(trendReportList).hasSize(databaseSizeBeforeUpdate);
        TrendReport testTrendReport = trendReportList.get(trendReportList.size() - 1);
        assertThat(testTrendReport.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testTrendReport.getHire()).isEqualTo(UPDATED_HIRE);
    }

    @Test
    @Transactional
    public void updateNonExistingTrendReport() throws Exception {
        int databaseSizeBeforeUpdate = trendReportRepository.findAll().size();

        // Create the TrendReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrendReportMockMvc.perform(put("/api/trend-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trendReport)))
            .andExpect(status().isCreated());

        // Validate the TrendReport in the database
        List<TrendReport> trendReportList = trendReportRepository.findAll();
        assertThat(trendReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrendReport() throws Exception {
        // Initialize the database
        trendReportRepository.saveAndFlush(trendReport);
        int databaseSizeBeforeDelete = trendReportRepository.findAll().size();

        // Get the trendReport
        restTrendReportMockMvc.perform(delete("/api/trend-reports/{id}", trendReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TrendReport> trendReportList = trendReportRepository.findAll();
        assertThat(trendReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrendReport.class);
        TrendReport trendReport1 = new TrendReport();
        trendReport1.setId(1L);
        TrendReport trendReport2 = new TrendReport();
        trendReport2.setId(trendReport1.getId());
        assertThat(trendReport1).isEqualTo(trendReport2);
        trendReport2.setId(2L);
        assertThat(trendReport1).isNotEqualTo(trendReport2);
        trendReport1.setId(null);
        assertThat(trendReport1).isNotEqualTo(trendReport2);
    }
}
