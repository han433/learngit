package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.SubmittalsAgingReport;
import com.asiainfo.boss.repository.SubmittalsAgingReportRepository;
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
 * Test class for the SubmittalsAgingReportResource REST controller.
 *
 * @see SubmittalsAgingReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class SubmittalsAgingReportResourceIntTest {

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    @Autowired
    private SubmittalsAgingReportRepository submittalsAgingReportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubmittalsAgingReportMockMvc;

    private SubmittalsAgingReport submittalsAgingReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubmittalsAgingReportResource submittalsAgingReportResource = new SubmittalsAgingReportResource(submittalsAgingReportRepository);
        this.restSubmittalsAgingReportMockMvc = MockMvcBuilders.standaloneSetup(submittalsAgingReportResource)
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
    public static SubmittalsAgingReport createEntity(EntityManager em) {
        SubmittalsAgingReport submittalsAgingReport = new SubmittalsAgingReport()
            .company(DEFAULT_COMPANY);
        return submittalsAgingReport;
    }

    @Before
    public void initTest() {
        submittalsAgingReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubmittalsAgingReport() throws Exception {
        int databaseSizeBeforeCreate = submittalsAgingReportRepository.findAll().size();

        // Create the SubmittalsAgingReport
        restSubmittalsAgingReportMockMvc.perform(post("/api/submittals-aging-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submittalsAgingReport)))
            .andExpect(status().isCreated());

        // Validate the SubmittalsAgingReport in the database
        List<SubmittalsAgingReport> submittalsAgingReportList = submittalsAgingReportRepository.findAll();
        assertThat(submittalsAgingReportList).hasSize(databaseSizeBeforeCreate + 1);
        SubmittalsAgingReport testSubmittalsAgingReport = submittalsAgingReportList.get(submittalsAgingReportList.size() - 1);
        assertThat(testSubmittalsAgingReport.getCompany()).isEqualTo(DEFAULT_COMPANY);
    }

    @Test
    @Transactional
    public void createSubmittalsAgingReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = submittalsAgingReportRepository.findAll().size();

        // Create the SubmittalsAgingReport with an existing ID
        submittalsAgingReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmittalsAgingReportMockMvc.perform(post("/api/submittals-aging-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submittalsAgingReport)))
            .andExpect(status().isBadRequest());

        // Validate the SubmittalsAgingReport in the database
        List<SubmittalsAgingReport> submittalsAgingReportList = submittalsAgingReportRepository.findAll();
        assertThat(submittalsAgingReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSubmittalsAgingReports() throws Exception {
        // Initialize the database
        submittalsAgingReportRepository.saveAndFlush(submittalsAgingReport);

        // Get all the submittalsAgingReportList
        restSubmittalsAgingReportMockMvc.perform(get("/api/submittals-aging-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submittalsAgingReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())));
    }

    @Test
    @Transactional
    public void getSubmittalsAgingReport() throws Exception {
        // Initialize the database
        submittalsAgingReportRepository.saveAndFlush(submittalsAgingReport);

        // Get the submittalsAgingReport
        restSubmittalsAgingReportMockMvc.perform(get("/api/submittals-aging-reports/{id}", submittalsAgingReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(submittalsAgingReport.getId().intValue()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubmittalsAgingReport() throws Exception {
        // Get the submittalsAgingReport
        restSubmittalsAgingReportMockMvc.perform(get("/api/submittals-aging-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubmittalsAgingReport() throws Exception {
        // Initialize the database
        submittalsAgingReportRepository.saveAndFlush(submittalsAgingReport);
        int databaseSizeBeforeUpdate = submittalsAgingReportRepository.findAll().size();

        // Update the submittalsAgingReport
        SubmittalsAgingReport updatedSubmittalsAgingReport = submittalsAgingReportRepository.findOne(submittalsAgingReport.getId());
        // Disconnect from session so that the updates on updatedSubmittalsAgingReport are not directly saved in db
        em.detach(updatedSubmittalsAgingReport);
        updatedSubmittalsAgingReport
            .company(UPDATED_COMPANY);

        restSubmittalsAgingReportMockMvc.perform(put("/api/submittals-aging-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubmittalsAgingReport)))
            .andExpect(status().isOk());

        // Validate the SubmittalsAgingReport in the database
        List<SubmittalsAgingReport> submittalsAgingReportList = submittalsAgingReportRepository.findAll();
        assertThat(submittalsAgingReportList).hasSize(databaseSizeBeforeUpdate);
        SubmittalsAgingReport testSubmittalsAgingReport = submittalsAgingReportList.get(submittalsAgingReportList.size() - 1);
        assertThat(testSubmittalsAgingReport.getCompany()).isEqualTo(UPDATED_COMPANY);
    }

    @Test
    @Transactional
    public void updateNonExistingSubmittalsAgingReport() throws Exception {
        int databaseSizeBeforeUpdate = submittalsAgingReportRepository.findAll().size();

        // Create the SubmittalsAgingReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubmittalsAgingReportMockMvc.perform(put("/api/submittals-aging-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(submittalsAgingReport)))
            .andExpect(status().isCreated());

        // Validate the SubmittalsAgingReport in the database
        List<SubmittalsAgingReport> submittalsAgingReportList = submittalsAgingReportRepository.findAll();
        assertThat(submittalsAgingReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubmittalsAgingReport() throws Exception {
        // Initialize the database
        submittalsAgingReportRepository.saveAndFlush(submittalsAgingReport);
        int databaseSizeBeforeDelete = submittalsAgingReportRepository.findAll().size();

        // Get the submittalsAgingReport
        restSubmittalsAgingReportMockMvc.perform(delete("/api/submittals-aging-reports/{id}", submittalsAgingReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubmittalsAgingReport> submittalsAgingReportList = submittalsAgingReportRepository.findAll();
        assertThat(submittalsAgingReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubmittalsAgingReport.class);
        SubmittalsAgingReport submittalsAgingReport1 = new SubmittalsAgingReport();
        submittalsAgingReport1.setId(1L);
        SubmittalsAgingReport submittalsAgingReport2 = new SubmittalsAgingReport();
        submittalsAgingReport2.setId(submittalsAgingReport1.getId());
        assertThat(submittalsAgingReport1).isEqualTo(submittalsAgingReport2);
        submittalsAgingReport2.setId(2L);
        assertThat(submittalsAgingReport1).isNotEqualTo(submittalsAgingReport2);
        submittalsAgingReport1.setId(null);
        assertThat(submittalsAgingReport1).isNotEqualTo(submittalsAgingReport2);
    }
}
