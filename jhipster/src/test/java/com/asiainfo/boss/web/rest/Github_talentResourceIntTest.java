package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.Github_talent;
import com.asiainfo.boss.repository.Github_talentRepository;
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
 * Test class for the Github_talentResource REST controller.
 *
 * @see Github_talentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class Github_talentResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private Github_talentRepository github_talentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGithub_talentMockMvc;

    private Github_talent github_talent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Github_talentResource github_talentResource = new Github_talentResource(github_talentRepository);
        this.restGithub_talentMockMvc = MockMvcBuilders.standaloneSetup(github_talentResource)
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
    public static Github_talent createEntity(EntityManager em) {
        Github_talent github_talent = new Github_talent()
            .username(DEFAULT_USERNAME)
            .name(DEFAULT_NAME);
        return github_talent;
    }

    @Before
    public void initTest() {
        github_talent = createEntity(em);
    }

    @Test
    @Transactional
    public void createGithub_talent() throws Exception {
        int databaseSizeBeforeCreate = github_talentRepository.findAll().size();

        // Create the Github_talent
        restGithub_talentMockMvc.perform(post("/api/github-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(github_talent)))
            .andExpect(status().isCreated());

        // Validate the Github_talent in the database
        List<Github_talent> github_talentList = github_talentRepository.findAll();
        assertThat(github_talentList).hasSize(databaseSizeBeforeCreate + 1);
        Github_talent testGithub_talent = github_talentList.get(github_talentList.size() - 1);
        assertThat(testGithub_talent.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testGithub_talent.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createGithub_talentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = github_talentRepository.findAll().size();

        // Create the Github_talent with an existing ID
        github_talent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGithub_talentMockMvc.perform(post("/api/github-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(github_talent)))
            .andExpect(status().isBadRequest());

        // Validate the Github_talent in the database
        List<Github_talent> github_talentList = github_talentRepository.findAll();
        assertThat(github_talentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGithub_talents() throws Exception {
        // Initialize the database
        github_talentRepository.saveAndFlush(github_talent);

        // Get all the github_talentList
        restGithub_talentMockMvc.perform(get("/api/github-talents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(github_talent.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGithub_talent() throws Exception {
        // Initialize the database
        github_talentRepository.saveAndFlush(github_talent);

        // Get the github_talent
        restGithub_talentMockMvc.perform(get("/api/github-talents/{id}", github_talent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(github_talent.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGithub_talent() throws Exception {
        // Get the github_talent
        restGithub_talentMockMvc.perform(get("/api/github-talents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGithub_talent() throws Exception {
        // Initialize the database
        github_talentRepository.saveAndFlush(github_talent);
        int databaseSizeBeforeUpdate = github_talentRepository.findAll().size();

        // Update the github_talent
        Github_talent updatedGithub_talent = github_talentRepository.findOne(github_talent.getId());
        // Disconnect from session so that the updates on updatedGithub_talent are not directly saved in db
        em.detach(updatedGithub_talent);
        updatedGithub_talent
            .username(UPDATED_USERNAME)
            .name(UPDATED_NAME);

        restGithub_talentMockMvc.perform(put("/api/github-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGithub_talent)))
            .andExpect(status().isOk());

        // Validate the Github_talent in the database
        List<Github_talent> github_talentList = github_talentRepository.findAll();
        assertThat(github_talentList).hasSize(databaseSizeBeforeUpdate);
        Github_talent testGithub_talent = github_talentList.get(github_talentList.size() - 1);
        assertThat(testGithub_talent.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testGithub_talent.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingGithub_talent() throws Exception {
        int databaseSizeBeforeUpdate = github_talentRepository.findAll().size();

        // Create the Github_talent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGithub_talentMockMvc.perform(put("/api/github-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(github_talent)))
            .andExpect(status().isCreated());

        // Validate the Github_talent in the database
        List<Github_talent> github_talentList = github_talentRepository.findAll();
        assertThat(github_talentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGithub_talent() throws Exception {
        // Initialize the database
        github_talentRepository.saveAndFlush(github_talent);
        int databaseSizeBeforeDelete = github_talentRepository.findAll().size();

        // Get the github_talent
        restGithub_talentMockMvc.perform(delete("/api/github-talents/{id}", github_talent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Github_talent> github_talentList = github_talentRepository.findAll();
        assertThat(github_talentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Github_talent.class);
        Github_talent github_talent1 = new Github_talent();
        github_talent1.setId(1L);
        Github_talent github_talent2 = new Github_talent();
        github_talent2.setId(github_talent1.getId());
        assertThat(github_talent1).isEqualTo(github_talent2);
        github_talent2.setId(2L);
        assertThat(github_talent1).isNotEqualTo(github_talent2);
        github_talent1.setId(null);
        assertThat(github_talent1).isNotEqualTo(github_talent2);
    }
}
