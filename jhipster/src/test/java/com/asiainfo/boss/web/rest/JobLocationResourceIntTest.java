package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.JobLocation;
import com.asiainfo.boss.repository.JobLocationRepository;
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
 * Test class for the JobLocationResource REST controller.
 *
 * @see JobLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class JobLocationResourceIntTest {

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    @Autowired
    private JobLocationRepository jobLocationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobLocationMockMvc;

    private JobLocation jobLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobLocationResource jobLocationResource = new JobLocationResource(jobLocationRepository);
        this.restJobLocationMockMvc = MockMvcBuilders.standaloneSetup(jobLocationResource)
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
    public static JobLocation createEntity(EntityManager em) {
        JobLocation jobLocation = new JobLocation()
            .city(DEFAULT_CITY);
        return jobLocation;
    }

    @Before
    public void initTest() {
        jobLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobLocation() throws Exception {
        int databaseSizeBeforeCreate = jobLocationRepository.findAll().size();

        // Create the JobLocation
        restJobLocationMockMvc.perform(post("/api/job-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobLocation)))
            .andExpect(status().isCreated());

        // Validate the JobLocation in the database
        List<JobLocation> jobLocationList = jobLocationRepository.findAll();
        assertThat(jobLocationList).hasSize(databaseSizeBeforeCreate + 1);
        JobLocation testJobLocation = jobLocationList.get(jobLocationList.size() - 1);
        assertThat(testJobLocation.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    public void createJobLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobLocationRepository.findAll().size();

        // Create the JobLocation with an existing ID
        jobLocation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobLocationMockMvc.perform(post("/api/job-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobLocation)))
            .andExpect(status().isBadRequest());

        // Validate the JobLocation in the database
        List<JobLocation> jobLocationList = jobLocationRepository.findAll();
        assertThat(jobLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobLocations() throws Exception {
        // Initialize the database
        jobLocationRepository.saveAndFlush(jobLocation);

        // Get all the jobLocationList
        restJobLocationMockMvc.perform(get("/api/job-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())));
    }

    @Test
    @Transactional
    public void getJobLocation() throws Exception {
        // Initialize the database
        jobLocationRepository.saveAndFlush(jobLocation);

        // Get the jobLocation
        restJobLocationMockMvc.perform(get("/api/job-locations/{id}", jobLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobLocation.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobLocation() throws Exception {
        // Get the jobLocation
        restJobLocationMockMvc.perform(get("/api/job-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobLocation() throws Exception {
        // Initialize the database
        jobLocationRepository.saveAndFlush(jobLocation);
        int databaseSizeBeforeUpdate = jobLocationRepository.findAll().size();

        // Update the jobLocation
        JobLocation updatedJobLocation = jobLocationRepository.findOne(jobLocation.getId());
        // Disconnect from session so that the updates on updatedJobLocation are not directly saved in db
        em.detach(updatedJobLocation);
        updatedJobLocation
            .city(UPDATED_CITY);

        restJobLocationMockMvc.perform(put("/api/job-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobLocation)))
            .andExpect(status().isOk());

        // Validate the JobLocation in the database
        List<JobLocation> jobLocationList = jobLocationRepository.findAll();
        assertThat(jobLocationList).hasSize(databaseSizeBeforeUpdate);
        JobLocation testJobLocation = jobLocationList.get(jobLocationList.size() - 1);
        assertThat(testJobLocation.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    public void updateNonExistingJobLocation() throws Exception {
        int databaseSizeBeforeUpdate = jobLocationRepository.findAll().size();

        // Create the JobLocation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobLocationMockMvc.perform(put("/api/job-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobLocation)))
            .andExpect(status().isCreated());

        // Validate the JobLocation in the database
        List<JobLocation> jobLocationList = jobLocationRepository.findAll();
        assertThat(jobLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobLocation() throws Exception {
        // Initialize the database
        jobLocationRepository.saveAndFlush(jobLocation);
        int databaseSizeBeforeDelete = jobLocationRepository.findAll().size();

        // Get the jobLocation
        restJobLocationMockMvc.perform(delete("/api/job-locations/{id}", jobLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobLocation> jobLocationList = jobLocationRepository.findAll();
        assertThat(jobLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobLocation.class);
        JobLocation jobLocation1 = new JobLocation();
        jobLocation1.setId(1L);
        JobLocation jobLocation2 = new JobLocation();
        jobLocation2.setId(jobLocation1.getId());
        assertThat(jobLocation1).isEqualTo(jobLocation2);
        jobLocation2.setId(2L);
        assertThat(jobLocation1).isNotEqualTo(jobLocation2);
        jobLocation1.setId(null);
        assertThat(jobLocation1).isNotEqualTo(jobLocation2);
    }
}
