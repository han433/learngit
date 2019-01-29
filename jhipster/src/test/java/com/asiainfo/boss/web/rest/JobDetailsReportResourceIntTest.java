package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.JobDetailsReport;
import com.asiainfo.boss.repository.JobDetailsReportRepository;
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
 * Test class for the JobDetailsReportResource REST controller.
 *
 * @see JobDetailsReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class JobDetailsReportResourceIntTest {

    private static final String DEFAULT_BATCH_ID = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_ID = "BBBBBBBBBB";

    @Autowired
    private JobDetailsReportRepository jobDetailsReportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobDetailsReportMockMvc;

    private JobDetailsReport jobDetailsReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobDetailsReportResource jobDetailsReportResource = new JobDetailsReportResource(jobDetailsReportRepository);
        this.restJobDetailsReportMockMvc = MockMvcBuilders.standaloneSetup(jobDetailsReportResource)
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
    public static JobDetailsReport createEntity(EntityManager em) {
        JobDetailsReport jobDetailsReport = new JobDetailsReport()
            .batchId(DEFAULT_BATCH_ID);
        return jobDetailsReport;
    }

    @Before
    public void initTest() {
        jobDetailsReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobDetailsReport() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsReportRepository.findAll().size();

        // Create the JobDetailsReport
        restJobDetailsReportMockMvc.perform(post("/api/job-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetailsReport)))
            .andExpect(status().isCreated());

        // Validate the JobDetailsReport in the database
        List<JobDetailsReport> jobDetailsReportList = jobDetailsReportRepository.findAll();
        assertThat(jobDetailsReportList).hasSize(databaseSizeBeforeCreate + 1);
        JobDetailsReport testJobDetailsReport = jobDetailsReportList.get(jobDetailsReportList.size() - 1);
        assertThat(testJobDetailsReport.getBatchId()).isEqualTo(DEFAULT_BATCH_ID);
    }

    @Test
    @Transactional
    public void createJobDetailsReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsReportRepository.findAll().size();

        // Create the JobDetailsReport with an existing ID
        jobDetailsReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobDetailsReportMockMvc.perform(post("/api/job-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetailsReport)))
            .andExpect(status().isBadRequest());

        // Validate the JobDetailsReport in the database
        List<JobDetailsReport> jobDetailsReportList = jobDetailsReportRepository.findAll();
        assertThat(jobDetailsReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobDetailsReports() throws Exception {
        // Initialize the database
        jobDetailsReportRepository.saveAndFlush(jobDetailsReport);

        // Get all the jobDetailsReportList
        restJobDetailsReportMockMvc.perform(get("/api/job-details-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetailsReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchId").value(hasItem(DEFAULT_BATCH_ID.toString())));
    }

    @Test
    @Transactional
    public void getJobDetailsReport() throws Exception {
        // Initialize the database
        jobDetailsReportRepository.saveAndFlush(jobDetailsReport);

        // Get the jobDetailsReport
        restJobDetailsReportMockMvc.perform(get("/api/job-details-reports/{id}", jobDetailsReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobDetailsReport.getId().intValue()))
            .andExpect(jsonPath("$.batchId").value(DEFAULT_BATCH_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobDetailsReport() throws Exception {
        // Get the jobDetailsReport
        restJobDetailsReportMockMvc.perform(get("/api/job-details-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobDetailsReport() throws Exception {
        // Initialize the database
        jobDetailsReportRepository.saveAndFlush(jobDetailsReport);
        int databaseSizeBeforeUpdate = jobDetailsReportRepository.findAll().size();

        // Update the jobDetailsReport
        JobDetailsReport updatedJobDetailsReport = jobDetailsReportRepository.findOne(jobDetailsReport.getId());
        // Disconnect from session so that the updates on updatedJobDetailsReport are not directly saved in db
        em.detach(updatedJobDetailsReport);
        updatedJobDetailsReport
            .batchId(UPDATED_BATCH_ID);

        restJobDetailsReportMockMvc.perform(put("/api/job-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobDetailsReport)))
            .andExpect(status().isOk());

        // Validate the JobDetailsReport in the database
        List<JobDetailsReport> jobDetailsReportList = jobDetailsReportRepository.findAll();
        assertThat(jobDetailsReportList).hasSize(databaseSizeBeforeUpdate);
        JobDetailsReport testJobDetailsReport = jobDetailsReportList.get(jobDetailsReportList.size() - 1);
        assertThat(testJobDetailsReport.getBatchId()).isEqualTo(UPDATED_BATCH_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingJobDetailsReport() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsReportRepository.findAll().size();

        // Create the JobDetailsReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobDetailsReportMockMvc.perform(put("/api/job-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobDetailsReport)))
            .andExpect(status().isCreated());

        // Validate the JobDetailsReport in the database
        List<JobDetailsReport> jobDetailsReportList = jobDetailsReportRepository.findAll();
        assertThat(jobDetailsReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobDetailsReport() throws Exception {
        // Initialize the database
        jobDetailsReportRepository.saveAndFlush(jobDetailsReport);
        int databaseSizeBeforeDelete = jobDetailsReportRepository.findAll().size();

        // Get the jobDetailsReport
        restJobDetailsReportMockMvc.perform(delete("/api/job-details-reports/{id}", jobDetailsReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobDetailsReport> jobDetailsReportList = jobDetailsReportRepository.findAll();
        assertThat(jobDetailsReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobDetailsReport.class);
        JobDetailsReport jobDetailsReport1 = new JobDetailsReport();
        jobDetailsReport1.setId(1L);
        JobDetailsReport jobDetailsReport2 = new JobDetailsReport();
        jobDetailsReport2.setId(jobDetailsReport1.getId());
        assertThat(jobDetailsReport1).isEqualTo(jobDetailsReport2);
        jobDetailsReport2.setId(2L);
        assertThat(jobDetailsReport1).isNotEqualTo(jobDetailsReport2);
        jobDetailsReport1.setId(null);
        assertThat(jobDetailsReport1).isNotEqualTo(jobDetailsReport2);
    }
}
