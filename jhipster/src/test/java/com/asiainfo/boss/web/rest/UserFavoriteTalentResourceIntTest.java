package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.UserFavoriteTalent;
import com.asiainfo.boss.repository.UserFavoriteTalentRepository;
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
 * Test class for the UserFavoriteTalentResource REST controller.
 *
 * @see UserFavoriteTalentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class UserFavoriteTalentResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private UserFavoriteTalentRepository userFavoriteTalentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserFavoriteTalentMockMvc;

    private UserFavoriteTalent userFavoriteTalent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserFavoriteTalentResource userFavoriteTalentResource = new UserFavoriteTalentResource(userFavoriteTalentRepository);
        this.restUserFavoriteTalentMockMvc = MockMvcBuilders.standaloneSetup(userFavoriteTalentResource)
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
    public static UserFavoriteTalent createEntity(EntityManager em) {
        UserFavoriteTalent userFavoriteTalent = new UserFavoriteTalent()
            .userId(DEFAULT_USER_ID);
        return userFavoriteTalent;
    }

    @Before
    public void initTest() {
        userFavoriteTalent = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserFavoriteTalent() throws Exception {
        int databaseSizeBeforeCreate = userFavoriteTalentRepository.findAll().size();

        // Create the UserFavoriteTalent
        restUserFavoriteTalentMockMvc.perform(post("/api/user-favorite-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavoriteTalent)))
            .andExpect(status().isCreated());

        // Validate the UserFavoriteTalent in the database
        List<UserFavoriteTalent> userFavoriteTalentList = userFavoriteTalentRepository.findAll();
        assertThat(userFavoriteTalentList).hasSize(databaseSizeBeforeCreate + 1);
        UserFavoriteTalent testUserFavoriteTalent = userFavoriteTalentList.get(userFavoriteTalentList.size() - 1);
        assertThat(testUserFavoriteTalent.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createUserFavoriteTalentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userFavoriteTalentRepository.findAll().size();

        // Create the UserFavoriteTalent with an existing ID
        userFavoriteTalent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFavoriteTalentMockMvc.perform(post("/api/user-favorite-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavoriteTalent)))
            .andExpect(status().isBadRequest());

        // Validate the UserFavoriteTalent in the database
        List<UserFavoriteTalent> userFavoriteTalentList = userFavoriteTalentRepository.findAll();
        assertThat(userFavoriteTalentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserFavoriteTalents() throws Exception {
        // Initialize the database
        userFavoriteTalentRepository.saveAndFlush(userFavoriteTalent);

        // Get all the userFavoriteTalentList
        restUserFavoriteTalentMockMvc.perform(get("/api/user-favorite-talents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFavoriteTalent.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserFavoriteTalent() throws Exception {
        // Initialize the database
        userFavoriteTalentRepository.saveAndFlush(userFavoriteTalent);

        // Get the userFavoriteTalent
        restUserFavoriteTalentMockMvc.perform(get("/api/user-favorite-talents/{id}", userFavoriteTalent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userFavoriteTalent.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserFavoriteTalent() throws Exception {
        // Get the userFavoriteTalent
        restUserFavoriteTalentMockMvc.perform(get("/api/user-favorite-talents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserFavoriteTalent() throws Exception {
        // Initialize the database
        userFavoriteTalentRepository.saveAndFlush(userFavoriteTalent);
        int databaseSizeBeforeUpdate = userFavoriteTalentRepository.findAll().size();

        // Update the userFavoriteTalent
        UserFavoriteTalent updatedUserFavoriteTalent = userFavoriteTalentRepository.findOne(userFavoriteTalent.getId());
        // Disconnect from session so that the updates on updatedUserFavoriteTalent are not directly saved in db
        em.detach(updatedUserFavoriteTalent);
        updatedUserFavoriteTalent
            .userId(UPDATED_USER_ID);

        restUserFavoriteTalentMockMvc.perform(put("/api/user-favorite-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserFavoriteTalent)))
            .andExpect(status().isOk());

        // Validate the UserFavoriteTalent in the database
        List<UserFavoriteTalent> userFavoriteTalentList = userFavoriteTalentRepository.findAll();
        assertThat(userFavoriteTalentList).hasSize(databaseSizeBeforeUpdate);
        UserFavoriteTalent testUserFavoriteTalent = userFavoriteTalentList.get(userFavoriteTalentList.size() - 1);
        assertThat(testUserFavoriteTalent.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingUserFavoriteTalent() throws Exception {
        int databaseSizeBeforeUpdate = userFavoriteTalentRepository.findAll().size();

        // Create the UserFavoriteTalent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserFavoriteTalentMockMvc.perform(put("/api/user-favorite-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavoriteTalent)))
            .andExpect(status().isCreated());

        // Validate the UserFavoriteTalent in the database
        List<UserFavoriteTalent> userFavoriteTalentList = userFavoriteTalentRepository.findAll();
        assertThat(userFavoriteTalentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserFavoriteTalent() throws Exception {
        // Initialize the database
        userFavoriteTalentRepository.saveAndFlush(userFavoriteTalent);
        int databaseSizeBeforeDelete = userFavoriteTalentRepository.findAll().size();

        // Get the userFavoriteTalent
        restUserFavoriteTalentMockMvc.perform(delete("/api/user-favorite-talents/{id}", userFavoriteTalent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserFavoriteTalent> userFavoriteTalentList = userFavoriteTalentRepository.findAll();
        assertThat(userFavoriteTalentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserFavoriteTalent.class);
        UserFavoriteTalent userFavoriteTalent1 = new UserFavoriteTalent();
        userFavoriteTalent1.setId(1L);
        UserFavoriteTalent userFavoriteTalent2 = new UserFavoriteTalent();
        userFavoriteTalent2.setId(userFavoriteTalent1.getId());
        assertThat(userFavoriteTalent1).isEqualTo(userFavoriteTalent2);
        userFavoriteTalent2.setId(2L);
        assertThat(userFavoriteTalent1).isNotEqualTo(userFavoriteTalent2);
        userFavoriteTalent1.setId(null);
        assertThat(userFavoriteTalent1).isNotEqualTo(userFavoriteTalent2);
    }
}
