package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.ActivityReport;
import com.asiainfo.boss.repository.ActivityReportRepository;
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
 * Test class for the ActivityReportResource REST controller.
 *
 * @see ActivityReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class ActivityReportResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ActivityReportRepository activityReportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityReportMockMvc;

    private ActivityReport activityReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityReportResource activityReportResource = new ActivityReportResource(activityReportRepository);
        this.restActivityReportMockMvc = MockMvcBuilders.standaloneSetup(activityReportResource)
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
    public static ActivityReport createEntity(EntityManager em) {
        ActivityReport activityReport = new ActivityReport()
            .name(DEFAULT_NAME);
        return activityReport;
    }

    @Before
    public void initTest() {
        activityReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityReport() throws Exception {
        int databaseSizeBeforeCreate = activityReportRepository.findAll().size();

        // Create the ActivityReport
        restActivityReportMockMvc.perform(post("/api/activity-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityReport)))
            .andExpect(status().isCreated());

        // Validate the ActivityReport in the database
        List<ActivityReport> activityReportList = activityReportRepository.findAll();
        assertThat(activityReportList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityReport testActivityReport = activityReportList.get(activityReportList.size() - 1);
        assertThat(testActivityReport.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createActivityReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityReportRepository.findAll().size();

        // Create the ActivityReport with an existing ID
        activityReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityReportMockMvc.perform(post("/api/activity-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityReport)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityReport in the database
        List<ActivityReport> activityReportList = activityReportRepository.findAll();
        assertThat(activityReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActivityReports() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        // Get all the activityReportList
        restActivityReportMockMvc.perform(get("/api/activity-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getActivityReport() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        // Get the activityReport
        restActivityReportMockMvc.perform(get("/api/activity-reports/{id}", activityReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityReport() throws Exception {
        // Get the activityReport
        restActivityReportMockMvc.perform(get("/api/activity-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityReport() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);
        int databaseSizeBeforeUpdate = activityReportRepository.findAll().size();

        // Update the activityReport
        ActivityReport updatedActivityReport = activityReportRepository.findOne(activityReport.getId());
        // Disconnect from session so that the updates on updatedActivityReport are not directly saved in db
        em.detach(updatedActivityReport);
        updatedActivityReport
            .name(UPDATED_NAME);

        restActivityReportMockMvc.perform(put("/api/activity-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActivityReport)))
            .andExpect(status().isOk());

        // Validate the ActivityReport in the database
        List<ActivityReport> activityReportList = activityReportRepository.findAll();
        assertThat(activityReportList).hasSize(databaseSizeBeforeUpdate);
        ActivityReport testActivityReport = activityReportList.get(activityReportList.size() - 1);
        assertThat(testActivityReport.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityReport() throws Exception {
        int databaseSizeBeforeUpdate = activityReportRepository.findAll().size();

        // Create the ActivityReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityReportMockMvc.perform(put("/api/activity-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityReport)))
            .andExpect(status().isCreated());

        // Validate the ActivityReport in the database
        List<ActivityReport> activityReportList = activityReportRepository.findAll();
        assertThat(activityReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActivityReport() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);
        int databaseSizeBeforeDelete = activityReportRepository.findAll().size();

        // Get the activityReport
        restActivityReportMockMvc.perform(delete("/api/activity-reports/{id}", activityReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityReport> activityReportList = activityReportRepository.findAll();
        assertThat(activityReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityReport.class);
        ActivityReport activityReport1 = new ActivityReport();
        activityReport1.setId(1L);
        ActivityReport activityReport2 = new ActivityReport();
        activityReport2.setId(activityReport1.getId());
        assertThat(activityReport1).isEqualTo(activityReport2);
        activityReport2.setId(2L);
        assertThat(activityReport1).isNotEqualTo(activityReport2);
        activityReport1.setId(null);
        assertThat(activityReport1).isNotEqualTo(activityReport2);
    }
}
