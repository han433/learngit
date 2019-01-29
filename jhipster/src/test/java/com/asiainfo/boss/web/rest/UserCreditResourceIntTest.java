package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.UserCredit;
import com.asiainfo.boss.repository.UserCreditRepository;
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
 * Test class for the UserCreditResource REST controller.
 *
 * @see UserCreditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class UserCreditResourceIntTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    @Autowired
    private UserCreditRepository userCreditRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserCreditMockMvc;

    private UserCredit userCredit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserCreditResource userCreditResource = new UserCreditResource(userCreditRepository);
        this.restUserCreditMockMvc = MockMvcBuilders.standaloneSetup(userCreditResource)
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
    public static UserCredit createEntity(EntityManager em) {
        UserCredit userCredit = new UserCredit()
            .userId(DEFAULT_USER_ID)
            .credit(DEFAULT_CREDIT);
        return userCredit;
    }

    @Before
    public void initTest() {
        userCredit = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserCredit() throws Exception {
        int databaseSizeBeforeCreate = userCreditRepository.findAll().size();

        // Create the UserCredit
        restUserCreditMockMvc.perform(post("/api/user-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCredit)))
            .andExpect(status().isCreated());

        // Validate the UserCredit in the database
        List<UserCredit> userCreditList = userCreditRepository.findAll();
        assertThat(userCreditList).hasSize(databaseSizeBeforeCreate + 1);
        UserCredit testUserCredit = userCreditList.get(userCreditList.size() - 1);
        assertThat(testUserCredit.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserCredit.getCredit()).isEqualTo(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    public void createUserCreditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userCreditRepository.findAll().size();

        // Create the UserCredit with an existing ID
        userCredit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCreditMockMvc.perform(post("/api/user-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCredit)))
            .andExpect(status().isBadRequest());

        // Validate the UserCredit in the database
        List<UserCredit> userCreditList = userCreditRepository.findAll();
        assertThat(userCreditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserCredits() throws Exception {
        // Initialize the database
        userCreditRepository.saveAndFlush(userCredit);

        // Get all the userCreditList
        restUserCreditMockMvc.perform(get("/api/user-credits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCredit.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    public void getUserCredit() throws Exception {
        // Initialize the database
        userCreditRepository.saveAndFlush(userCredit);

        // Get the userCredit
        restUserCreditMockMvc.perform(get("/api/user-credits/{id}", userCredit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userCredit.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT));
    }

    @Test
    @Transactional
    public void getNonExistingUserCredit() throws Exception {
        // Get the userCredit
        restUserCreditMockMvc.perform(get("/api/user-credits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserCredit() throws Exception {
        // Initialize the database
        userCreditRepository.saveAndFlush(userCredit);
        int databaseSizeBeforeUpdate = userCreditRepository.findAll().size();

        // Update the userCredit
        UserCredit updatedUserCredit = userCreditRepository.findOne(userCredit.getId());
        // Disconnect from session so that the updates on updatedUserCredit are not directly saved in db
        em.detach(updatedUserCredit);
        updatedUserCredit
            .userId(UPDATED_USER_ID)
            .credit(UPDATED_CREDIT);

        restUserCreditMockMvc.perform(put("/api/user-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserCredit)))
            .andExpect(status().isOk());

        // Validate the UserCredit in the database
        List<UserCredit> userCreditList = userCreditRepository.findAll();
        assertThat(userCreditList).hasSize(databaseSizeBeforeUpdate);
        UserCredit testUserCredit = userCreditList.get(userCreditList.size() - 1);
        assertThat(testUserCredit.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserCredit.getCredit()).isEqualTo(UPDATED_CREDIT);
    }

    @Test
    @Transactional
    public void updateNonExistingUserCredit() throws Exception {
        int databaseSizeBeforeUpdate = userCreditRepository.findAll().size();

        // Create the UserCredit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserCreditMockMvc.perform(put("/api/user-credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCredit)))
            .andExpect(status().isCreated());

        // Validate the UserCredit in the database
        List<UserCredit> userCreditList = userCreditRepository.findAll();
        assertThat(userCreditList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserCredit() throws Exception {
        // Initialize the database
        userCreditRepository.saveAndFlush(userCredit);
        int databaseSizeBeforeDelete = userCreditRepository.findAll().size();

        // Get the userCredit
        restUserCreditMockMvc.perform(delete("/api/user-credits/{id}", userCredit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserCredit> userCreditList = userCreditRepository.findAll();
        assertThat(userCreditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCredit.class);
        UserCredit userCredit1 = new UserCredit();
        userCredit1.setId(1L);
        UserCredit userCredit2 = new UserCredit();
        userCredit2.setId(userCredit1.getId());
        assertThat(userCredit1).isEqualTo(userCredit2);
        userCredit2.setId(2L);
        assertThat(userCredit1).isNotEqualTo(userCredit2);
        userCredit1.setId(null);
        assertThat(userCredit1).isNotEqualTo(userCredit2);
    }
}
