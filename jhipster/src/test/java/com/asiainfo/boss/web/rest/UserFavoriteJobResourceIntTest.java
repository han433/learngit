package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.UserFavoriteJob;
import com.asiainfo.boss.repository.UserFavoriteJobRepository;
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
 * Test class for the UserFavoriteJobResource REST controller.
 *
 * @see UserFavoriteJobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class UserFavoriteJobResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private UserFavoriteJobRepository userFavoriteJobRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserFavoriteJobMockMvc;

    private UserFavoriteJob userFavoriteJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserFavoriteJobResource userFavoriteJobResource = new UserFavoriteJobResource(userFavoriteJobRepository);
        this.restUserFavoriteJobMockMvc = MockMvcBuilders.standaloneSetup(userFavoriteJobResource)
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
    public static UserFavoriteJob createEntity(EntityManager em) {
        UserFavoriteJob userFavoriteJob = new UserFavoriteJob()
            .userId(DEFAULT_USER_ID);
        return userFavoriteJob;
    }

    @Before
    public void initTest() {
        userFavoriteJob = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserFavoriteJob() throws Exception {
        int databaseSizeBeforeCreate = userFavoriteJobRepository.findAll().size();

        // Create the UserFavoriteJob
        restUserFavoriteJobMockMvc.perform(post("/api/user-favorite-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavoriteJob)))
            .andExpect(status().isCreated());

        // Validate the UserFavoriteJob in the database
        List<UserFavoriteJob> userFavoriteJobList = userFavoriteJobRepository.findAll();
        assertThat(userFavoriteJobList).hasSize(databaseSizeBeforeCreate + 1);
        UserFavoriteJob testUserFavoriteJob = userFavoriteJobList.get(userFavoriteJobList.size() - 1);
        assertThat(testUserFavoriteJob.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createUserFavoriteJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userFavoriteJobRepository.findAll().size();

        // Create the UserFavoriteJob with an existing ID
        userFavoriteJob.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFavoriteJobMockMvc.perform(post("/api/user-favorite-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavoriteJob)))
            .andExpect(status().isBadRequest());

        // Validate the UserFavoriteJob in the database
        List<UserFavoriteJob> userFavoriteJobList = userFavoriteJobRepository.findAll();
        assertThat(userFavoriteJobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserFavoriteJobs() throws Exception {
        // Initialize the database
        userFavoriteJobRepository.saveAndFlush(userFavoriteJob);

        // Get all the userFavoriteJobList
        restUserFavoriteJobMockMvc.perform(get("/api/user-favorite-jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFavoriteJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserFavoriteJob() throws Exception {
        // Initialize the database
        userFavoriteJobRepository.saveAndFlush(userFavoriteJob);

        // Get the userFavoriteJob
        restUserFavoriteJobMockMvc.perform(get("/api/user-favorite-jobs/{id}", userFavoriteJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userFavoriteJob.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserFavoriteJob() throws Exception {
        // Get the userFavoriteJob
        restUserFavoriteJobMockMvc.perform(get("/api/user-favorite-jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserFavoriteJob() throws Exception {
        // Initialize the database
        userFavoriteJobRepository.saveAndFlush(userFavoriteJob);
        int databaseSizeBeforeUpdate = userFavoriteJobRepository.findAll().size();

        // Update the userFavoriteJob
        UserFavoriteJob updatedUserFavoriteJob = userFavoriteJobRepository.findOne(userFavoriteJob.getId());
        // Disconnect from session so that the updates on updatedUserFavoriteJob are not directly saved in db
        em.detach(updatedUserFavoriteJob);
        updatedUserFavoriteJob
            .userId(UPDATED_USER_ID);

        restUserFavoriteJobMockMvc.perform(put("/api/user-favorite-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserFavoriteJob)))
            .andExpect(status().isOk());

        // Validate the UserFavoriteJob in the database
        List<UserFavoriteJob> userFavoriteJobList = userFavoriteJobRepository.findAll();
        assertThat(userFavoriteJobList).hasSize(databaseSizeBeforeUpdate);
        UserFavoriteJob testUserFavoriteJob = userFavoriteJobList.get(userFavoriteJobList.size() - 1);
        assertThat(testUserFavoriteJob.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingUserFavoriteJob() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteJobRepository.findAll().size();

        // Create the UserFavoriteJob

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserFavoriteJobMockMvc.perform(put("/api/user-favorite-jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavoriteJob)))
            .andExpect(status().isCreated());

        // Validate the UserFavoriteJob in the database
        List<UserFavoriteJob> userFavoriteJobList = userFavoriteJobRepository.findAll();
        assertThat(userFavoriteJobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserFavoriteJob() throws Exception {
        // Initialize the database
        userFavoriteJobRepository.saveAndFlush(userFavoriteJob);
        int databaseSizeBeforeDelete = userFavoriteJobRepository.findAll().size();

        // Get the userFavoriteJob
        restUserFavoriteJobMockMvc.perform(delete("/api/user-favorite-jobs/{id}", userFavoriteJob.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserFavoriteJob> userFavoriteJobList = userFavoriteJobRepository.findAll();
        assertThat(userFavoriteJobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFavoriteJob.class);
        UserFavoriteJob userFavoriteJob1 = new UserFavoriteJob();
        userFavoriteJob1.setId(1L);
        UserFavoriteJob userFavoriteJob2 = new UserFavoriteJob();
        userFavoriteJob2.setId(userFavoriteJob1.getId());
        assertThat(userFavoriteJob1).isEqualTo(userFavoriteJob2);
        userFavoriteJob2.setId(2L);
        assertThat(userFavoriteJob1).isNotEqualTo(userFavoriteJob2);
        userFavoriteJob1.setId(null);
        assertThat(userFavoriteJob1).isNotEqualTo(userFavoriteJob2);
    }
}
