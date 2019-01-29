package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.ActivityDetailsReport;
import com.asiainfo.boss.repository.ActivityDetailsReportRepository;
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
 * Test class for the ActivityDetailsReportResource REST controller.
 *
 * @see ActivityDetailsReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class ActivityDetailsReportResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ActivityDetailsReportRepository activityDetailsReportRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityDetailsReportMockMvc;

    private ActivityDetailsReport activityDetailsReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityDetailsReportResource activityDetailsReportResource = new ActivityDetailsReportResource(activityDetailsReportRepository);
        this.restActivityDetailsReportMockMvc = MockMvcBuilders.standaloneSetup(activityDetailsReportResource)
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
    public static ActivityDetailsReport createEntity(EntityManager em) {
        ActivityDetailsReport activityDetailsReport = new ActivityDetailsReport()
            .name(DEFAULT_NAME);
        return activityDetailsReport;
    }

    @Before
    public void initTest() {
        activityDetailsReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityDetailsReport() throws Exception {
        int databaseSizeBeforeCreate = activityDetailsReportRepository.findAll().size();

        // Create the ActivityDetailsReport
        restActivityDetailsReportMockMvc.perform(post("/api/activity-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDetailsReport)))
            .andExpect(status().isCreated());

        // Validate the ActivityDetailsReport in the database
        List<ActivityDetailsReport> activityDetailsReportList = activityDetailsReportRepository.findAll();
        assertThat(activityDetailsReportList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityDetailsReport testActivityDetailsReport = activityDetailsReportList.get(activityDetailsReportList.size() - 1);
        assertThat(testActivityDetailsReport.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createActivityDetailsReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityDetailsReportRepository.findAll().size();

        // Create the ActivityDetailsReport with an existing ID
        activityDetailsReport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityDetailsReportMockMvc.perform(post("/api/activity-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDetailsReport)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityDetailsReport in the database
        List<ActivityDetailsReport> activityDetailsReportList = activityDetailsReportRepository.findAll();
        assertThat(activityDetailsReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActivityDetailsReports() throws Exception {
        // Initialize the database
        activityDetailsReportRepository.saveAndFlush(activityDetailsReport);

        // Get all the activityDetailsReportList
        restActivityDetailsReportMockMvc.perform(get("/api/activity-details-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityDetailsReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getActivityDetailsReport() throws Exception {
        // Initialize the database
        activityDetailsReportRepository.saveAndFlush(activityDetailsReport);

        // Get the activityDetailsReport
        restActivityDetailsReportMockMvc.perform(get("/api/activity-details-reports/{id}", activityDetailsReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityDetailsReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityDetailsReport() throws Exception {
        // Get the activityDetailsReport
        restActivityDetailsReportMockMvc.perform(get("/api/activity-details-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityDetailsReport() throws Exception {
        // Initialize the database
        activityDetailsReportRepository.saveAndFlush(activityDetailsReport);
        int databaseSizeBeforeUpdate = activityDetailsReportRepository.findAll().size();

        // Update the activityDetailsReport
        ActivityDetailsReport updatedActivityDetailsReport = activityDetailsReportRepository.findOne(activityDetailsReport.getId());
        // Disconnect from session so that the updates on updatedActivityDetailsReport are not directly saved in db
        em.detach(updatedActivityDetailsReport);
        updatedActivityDetailsReport
            .name(UPDATED_NAME);

        restActivityDetailsReportMockMvc.perform(put("/api/activity-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActivityDetailsReport)))
            .andExpect(status().isOk());

        // Validate the ActivityDetailsReport in the database
        List<ActivityDetailsReport> activityDetailsReportList = activityDetailsReportRepository.findAll();
        assertThat(activityDetailsReportList).hasSize(databaseSizeBeforeUpdate);
        ActivityDetailsReport testActivityDetailsReport = activityDetailsReportList.get(activityDetailsReportList.size() - 1);
        assertThat(testActivityDetailsReport.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityDetailsReport() throws Exception {
        int databaseSizeBeforeUpdate = activityDetailsReportRepository.findAll().size();

        // Create the ActivityDetailsReport

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityDetailsReportMockMvc.perform(put("/api/activity-details-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDetailsReport)))
            .andExpect(status().isCreated());

        // Validate the ActivityDetailsReport in the database
        List<ActivityDetailsReport> activityDetailsReportList = activityDetailsReportRepository.findAll();
        assertThat(activityDetailsReportList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActivityDetailsReport() throws Exception {
        // Initialize the database
        activityDetailsReportRepository.saveAndFlush(activityDetailsReport);
        int databaseSizeBeforeDelete = activityDetailsReportRepository.findAll().size();

        // Get the activityDetailsReport
        restActivityDetailsReportMockMvc.perform(delete("/api/activity-details-reports/{id}", activityDetailsReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityDetailsReport> activityDetailsReportList = activityDetailsReportRepository.findAll();
        assertThat(activityDetailsReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityDetailsReport.class);
        ActivityDetailsReport activityDetailsReport1 = new ActivityDetailsReport();
        activityDetailsReport1.setId(1L);
        ActivityDetailsReport activityDetailsReport2 = new ActivityDetailsReport();
        activityDetailsReport2.setId(activityDetailsReport1.getId());
        assertThat(activityDetailsReport1).isEqualTo(activityDetailsReport2);
        activityDetailsReport2.setId(2L);
        assertThat(activityDetailsReport1).isNotEqualTo(activityDetailsReport2);
        activityDetailsReport1.setId(null);
        assertThat(activityDetailsReport1).isNotEqualTo(activityDetailsReport2);
    }
}
