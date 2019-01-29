package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.UserAlert;
import com.asiainfo.boss.repository.UserAlertRepository;
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
 * Test class for the UserAlertResource REST controller.
 *
 * @see UserAlertResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class UserAlertResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private UserAlertRepository userAlertRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserAlertMockMvc;

    private UserAlert userAlert;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserAlertResource userAlertResource = new UserAlertResource(userAlertRepository);
        this.restUserAlertMockMvc = MockMvcBuilders.standaloneSetup(userAlertResource)
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
    public static UserAlert createEntity(EntityManager em) {
        UserAlert userAlert = new UserAlert()
            .userId(DEFAULT_USER_ID);
        return userAlert;
    }

    @Before
    public void initTest() {
        userAlert = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserAlert() throws Exception {
        int databaseSizeBeforeCreate = userAlertRepository.findAll().size();

        // Create the UserAlert
        restUserAlertMockMvc.perform(post("/api/user-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAlert)))
            .andExpect(status().isCreated());

        // Validate the UserAlert in the database
        List<UserAlert> userAlertList = userAlertRepository.findAll();
        assertThat(userAlertList).hasSize(databaseSizeBeforeCreate + 1);
        UserAlert testUserAlert = userAlertList.get(userAlertList.size() - 1);
        assertThat(testUserAlert.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createUserAlertWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userAlertRepository.findAll().size();

        // Create the UserAlert with an existing ID
        userAlert.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAlertMockMvc.perform(post("/api/user-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAlert)))
            .andExpect(status().isBadRequest());

        // Validate the UserAlert in the database
        List<UserAlert> userAlertList = userAlertRepository.findAll();
        assertThat(userAlertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserAlerts() throws Exception {
        // Initialize the database
        userAlertRepository.saveAndFlush(userAlert);

        // Get all the userAlertList
        restUserAlertMockMvc.perform(get("/api/user-alerts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserAlert() throws Exception {
        // Initialize the database
        userAlertRepository.saveAndFlush(userAlert);

        // Get the userAlert
        restUserAlertMockMvc.perform(get("/api/user-alerts/{id}", userAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userAlert.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserAlert() throws Exception {
        // Get the userAlert
        restUserAlertMockMvc.perform(get("/api/user-alerts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserAlert() throws Exception {
        // Initialize the database
        userAlertRepository.saveAndFlush(userAlert);
        int databaseSizeBeforeUpdate = userAlertRepository.findAll().size();

        // Update the userAlert
        UserAlert updatedUserAlert = userAlertRepository.findOne(userAlert.getId());
        // Disconnect from session so that the updates on updatedUserAlert are not directly saved in db
        em.detach(updatedUserAlert);
        updatedUserAlert
            .userId(UPDATED_USER_ID);

        restUserAlertMockMvc.perform(put("/api/user-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserAlert)))
            .andExpect(status().isOk());

        // Validate the UserAlert in the database
        List<UserAlert> userAlertList = userAlertRepository.findAll();
        assertThat(userAlertList).hasSize(databaseSizeBeforeUpdate);
        UserAlert testUserAlert = userAlertList.get(userAlertList.size() - 1);
        assertThat(testUserAlert.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingUserAlert() throws Exception {
        int databaseSizeBeforeUpdate = userAlertRepository.findAll().size();

        // Create the UserAlert

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserAlertMockMvc.perform(put("/api/user-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAlert)))
            .andExpect(status().isCreated());

        // Validate the UserAlert in the database
        List<UserAlert> userAlertList = userAlertRepository.findAll();
        assertThat(userAlertList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserAlert() throws Exception {
        // Initialize the database
        userAlertRepository.saveAndFlush(userAlert);
        int databaseSizeBeforeDelete = userAlertRepository.findAll().size();

        // Get the userAlert
        restUserAlertMockMvc.perform(delete("/api/user-alerts/{id}", userAlert.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserAlert> userAlertList = userAlertRepository.findAll();
        assertThat(userAlertList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAlert.class);
        UserAlert userAlert1 = new UserAlert();
        userAlert1.setId(1L);
        UserAlert userAlert2 = new UserAlert();
        userAlert2.setId(userAlert1.getId());
        assertThat(userAlert1).isEqualTo(userAlert2);
        userAlert2.setId(2L);
        assertThat(userAlert1).isNotEqualTo(userAlert2);
        userAlert1.setId(null);
        assertThat(userAlert1).isNotEqualTo(userAlert2);
    }
}
