package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.TalentLocation;
import com.asiainfo.boss.repository.TalentLocationRepository;
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
 * Test class for the TalentLocationResource REST controller.
 *
 * @see TalentLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class TalentLocationResourceIntTest {

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    @Autowired
    private TalentLocationRepository talentLocationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTalentLocationMockMvc;

    private TalentLocation talentLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TalentLocationResource talentLocationResource = new TalentLocationResource(talentLocationRepository);
        this.restTalentLocationMockMvc = MockMvcBuilders.standaloneSetup(talentLocationResource)
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
    public static TalentLocation createEntity(EntityManager em) {
        TalentLocation talentLocation = new TalentLocation()
            .city(DEFAULT_CITY);
        return talentLocation;
    }

    @Before
    public void initTest() {
        talentLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTalentLocation() throws Exception {
        int databaseSizeBeforeCreate = talentLocationRepository.findAll().size();

        // Create the TalentLocation
        restTalentLocationMockMvc.perform(post("/api/talent-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(talentLocation)))
            .andExpect(status().isCreated());

        // Validate the TalentLocation in the database
        List<TalentLocation> talentLocationList = talentLocationRepository.findAll();
        assertThat(talentLocationList).hasSize(databaseSizeBeforeCreate + 1);
        TalentLocation testTalentLocation = talentLocationList.get(talentLocationList.size() - 1);
        assertThat(testTalentLocation.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    public void createTalentLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = talentLocationRepository.findAll().size();

        // Create the TalentLocation with an existing ID
        talentLocation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTalentLocationMockMvc.perform(post("/api/talent-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(talentLocation)))
            .andExpect(status().isBadRequest());

        // Validate the TalentLocation in the database
        List<TalentLocation> talentLocationList = talentLocationRepository.findAll();
        assertThat(talentLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTalentLocations() throws Exception {
        // Initialize the database
        talentLocationRepository.saveAndFlush(talentLocation);

        // Get all the talentLocationList
        restTalentLocationMockMvc.perform(get("/api/talent-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(talentLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())));
    }

    @Test
    @Transactional
    public void getTalentLocation() throws Exception {
        // Initialize the database
        talentLocationRepository.saveAndFlush(talentLocation);

        // Get the talentLocation
        restTalentLocationMockMvc.perform(get("/api/talent-locations/{id}", talentLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(talentLocation.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTalentLocation() throws Exception {
        // Get the talentLocation
        restTalentLocationMockMvc.perform(get("/api/talent-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTalentLocation() throws Exception {
        // Initialize the database
        talentLocationRepository.saveAndFlush(talentLocation);
        int databaseSizeBeforeUpdate = talentLocationRepository.findAll().size();

        // Update the talentLocation
        TalentLocation updatedTalentLocation = talentLocationRepository.findOne(talentLocation.getId());
        // Disconnect from session so that the updates on updatedTalentLocation are not directly saved in db
        em.detach(updatedTalentLocation);
        updatedTalentLocation
            .city(UPDATED_CITY);

        restTalentLocationMockMvc.perform(put("/api/talent-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTalentLocation)))
            .andExpect(status().isOk());

        // Validate the TalentLocation in the database
        List<TalentLocation> talentLocationList = talentLocationRepository.findAll();
        assertThat(talentLocationList).hasSize(databaseSizeBeforeUpdate);
        TalentLocation testTalentLocation = talentLocationList.get(talentLocationList.size() - 1);
        assertThat(testTalentLocation.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    public void updateNonExistingTalentLocation() throws Exception {
        int databaseSizeBeforeUpdate = talentLocationRepository.findAll().size();

        // Create the TalentLocation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTalentLocationMockMvc.perform(put("/api/talent-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(talentLocation)))
            .andExpect(status().isCreated());

        // Validate the TalentLocation in the database
        List<TalentLocation> talentLocationList = talentLocationRepository.findAll();
        assertThat(talentLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTalentLocation() throws Exception {
        // Initialize the database
        talentLocationRepository.saveAndFlush(talentLocation);
        int databaseSizeBeforeDelete = talentLocationRepository.findAll().size();

        // Get the talentLocation
        restTalentLocationMockMvc.perform(delete("/api/talent-locations/{id}", talentLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TalentLocation> talentLocationList = talentLocationRepository.findAll();
        assertThat(talentLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TalentLocation.class);
        TalentLocation talentLocation1 = new TalentLocation();
        talentLocation1.setId(1L);
        TalentLocation talentLocation2 = new TalentLocation();
        talentLocation2.setId(talentLocation1.getId());
        assertThat(talentLocation1).isEqualTo(talentLocation2);
        talentLocation2.setId(2L);
        assertThat(talentLocation1).isNotEqualTo(talentLocation2);
        talentLocation1.setId(null);
        assertThat(talentLocation1).isNotEqualTo(talentLocation2);
    }
}
