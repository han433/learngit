package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.Application_commission;
import com.asiainfo.boss.repository.Application_commissionRepository;
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
 * Test class for the Application_commissionResource REST controller.
 *
 * @see Application_commissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class Application_commissionResourceIntTest {

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    @Autowired
    private Application_commissionRepository application_commissionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplication_commissionMockMvc;

    private Application_commission application_commission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Application_commissionResource application_commissionResource = new Application_commissionResource(application_commissionRepository);
        this.restApplication_commissionMockMvc = MockMvcBuilders.standaloneSetup(application_commissionResource)
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
    public static Application_commission createEntity(EntityManager em) {
        Application_commission application_commission = new Application_commission()
            .application_id(DEFAULT_APPLICATION_ID);
        return application_commission;
    }

    @Before
    public void initTest() {
        application_commission = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplication_commission() throws Exception {
        int databaseSizeBeforeCreate = application_commissionRepository.findAll().size();

        // Create the Application_commission
        restApplication_commissionMockMvc.perform(post("/api/application-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application_commission)))
            .andExpect(status().isCreated());

        // Validate the Application_commission in the database
        List<Application_commission> application_commissionList = application_commissionRepository.findAll();
        assertThat(application_commissionList).hasSize(databaseSizeBeforeCreate + 1);
        Application_commission testApplication_commission = application_commissionList.get(application_commissionList.size() - 1);
        assertThat(testApplication_commission.getApplication_id()).isEqualTo(DEFAULT_APPLICATION_ID);
    }

    @Test
    @Transactional
    public void createApplication_commissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = application_commissionRepository.findAll().size();

        // Create the Application_commission with an existing ID
        application_commission.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplication_commissionMockMvc.perform(post("/api/application-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application_commission)))
            .andExpect(status().isBadRequest());

        // Validate the Application_commission in the database
        List<Application_commission> application_commissionList = application_commissionRepository.findAll();
        assertThat(application_commissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApplication_commissions() throws Exception {
        // Initialize the database
        application_commissionRepository.saveAndFlush(application_commission);

        // Get all the application_commissionList
        restApplication_commissionMockMvc.perform(get("/api/application-commissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application_commission.getId().intValue())))
            .andExpect(jsonPath("$.[*].application_id").value(hasItem(DEFAULT_APPLICATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getApplication_commission() throws Exception {
        // Initialize the database
        application_commissionRepository.saveAndFlush(application_commission);

        // Get the application_commission
        restApplication_commissionMockMvc.perform(get("/api/application-commissions/{id}", application_commission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(application_commission.getId().intValue()))
            .andExpect(jsonPath("$.application_id").value(DEFAULT_APPLICATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplication_commission() throws Exception {
        // Get the application_commission
        restApplication_commissionMockMvc.perform(get("/api/application-commissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication_commission() throws Exception {
        // Initialize the database
        application_commissionRepository.saveAndFlush(application_commission);
        int databaseSizeBeforeUpdate = application_commissionRepository.findAll().size();

        // Update the application_commission
        Application_commission updatedApplication_commission = application_commissionRepository.findOne(application_commission.getId());
        // Disconnect from session so that the updates on updatedApplication_commission are not directly saved in db
        em.detach(updatedApplication_commission);
        updatedApplication_commission
            .application_id(UPDATED_APPLICATION_ID);

        restApplication_commissionMockMvc.perform(put("/api/application-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplication_commission)))
            .andExpect(status().isOk());

        // Validate the Application_commission in the database
        List<Application_commission> application_commissionList = application_commissionRepository.findAll();
        assertThat(application_commissionList).hasSize(databaseSizeBeforeUpdate);
        Application_commission testApplication_commission = application_commissionList.get(application_commissionList.size() - 1);
        assertThat(testApplication_commission.getApplication_id()).isEqualTo(UPDATED_APPLICATION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingApplication_commission() throws Exception {
        int databaseSizeBeforeUpdate = application_commissionRepository.findAll().size();

        // Create the Application_commission

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplication_commissionMockMvc.perform(put("/api/application-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(application_commission)))
            .andExpect(status().isCreated());

        // Validate the Application_commission in the database
        List<Application_commission> application_commissionList = application_commissionRepository.findAll();
        assertThat(application_commissionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplication_commission() throws Exception {
        // Initialize the database
        application_commissionRepository.saveAndFlush(application_commission);
        int databaseSizeBeforeDelete = application_commissionRepository.findAll().size();

        // Get the application_commission
        restApplication_commissionMockMvc.perform(delete("/api/application-commissions/{id}", application_commission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Application_commission> application_commissionList = application_commissionRepository.findAll();
        assertThat(application_commissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Application_commission.class);
        Application_commission application_commission1 = new Application_commission();
        application_commission1.setId(1L);
        Application_commission application_commission2 = new Application_commission();
        application_commission2.setId(application_commission1.getId());
        assertThat(application_commission1).isEqualTo(application_commission2);
        application_commission2.setId(2L);
        assertThat(application_commission1).isNotEqualTo(application_commission2);
        application_commission1.setId(null);
        assertThat(application_commission1).isNotEqualTo(application_commission2);
    }
}
