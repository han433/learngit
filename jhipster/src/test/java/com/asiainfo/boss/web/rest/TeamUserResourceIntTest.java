package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.TeamUser;
import com.asiainfo.boss.repository.TeamUserRepository;
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
 * Test class for the TeamUserResource REST controller.
 *
 * @see TeamUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class TeamUserResourceIntTest {

    private static final Integer DEFAULT_TEAM_ID = 1;
    private static final Integer UPDATED_TEAM_ID = 2;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamUserMockMvc;

    private TeamUser teamUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamUserResource teamUserResource = new TeamUserResource(teamUserRepository);
        this.restTeamUserMockMvc = MockMvcBuilders.standaloneSetup(teamUserResource)
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
    public static TeamUser createEntity(EntityManager em) {
        TeamUser teamUser = new TeamUser()
            .teamId(DEFAULT_TEAM_ID);
        return teamUser;
    }

    @Before
    public void initTest() {
        teamUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamUser() throws Exception {
        int databaseSizeBeforeCreate = teamUserRepository.findAll().size();

        // Create the TeamUser
        restTeamUserMockMvc.perform(post("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isCreated());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeCreate + 1);
        TeamUser testTeamUser = teamUserList.get(teamUserList.size() - 1);
        assertThat(testTeamUser.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
    }

    @Test
    @Transactional
    public void createTeamUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamUserRepository.findAll().size();

        // Create the TeamUser with an existing ID
        teamUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamUserMockMvc.perform(post("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isBadRequest());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamUsers() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get all the teamUserList
        restTeamUserMockMvc.perform(get("/api/team-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID)));
    }

    @Test
    @Transactional
    public void getTeamUser() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);

        // Get the teamUser
        restTeamUserMockMvc.perform(get("/api/team-users/{id}", teamUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamUser.getId().intValue()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID));
    }

    @Test
    @Transactional
    public void getNonExistingTeamUser() throws Exception {
        // Get the teamUser
        restTeamUserMockMvc.perform(get("/api/team-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamUser() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);
        int databaseSizeBeforeUpdate = teamUserRepository.findAll().size();

        // Update the teamUser
        TeamUser updatedTeamUser = teamUserRepository.findOne(teamUser.getId());
        // Disconnect from session so that the updates on updatedTeamUser are not directly saved in db
        em.detach(updatedTeamUser);
        updatedTeamUser
            .teamId(UPDATED_TEAM_ID);

        restTeamUserMockMvc.perform(put("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamUser)))
            .andExpect(status().isOk());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeUpdate);
        TeamUser testTeamUser = teamUserList.get(teamUserList.size() - 1);
        assertThat(testTeamUser.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamUser() throws Exception {
        int databaseSizeBeforeUpdate = teamUserRepository.findAll().size();

        // Create the TeamUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamUserMockMvc.perform(put("/api/team-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamUser)))
            .andExpect(status().isCreated());

        // Validate the TeamUser in the database
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamUser() throws Exception {
        // Initialize the database
        teamUserRepository.saveAndFlush(teamUser);
        int databaseSizeBeforeDelete = teamUserRepository.findAll().size();

        // Get the teamUser
        restTeamUserMockMvc.perform(delete("/api/team-users/{id}", teamUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamUser> teamUserList = teamUserRepository.findAll();
        assertThat(teamUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamUser.class);
        TeamUser teamUser1 = new TeamUser();
        teamUser1.setId(1L);
        TeamUser teamUser2 = new TeamUser();
        teamUser2.setId(teamUser1.getId());
        assertThat(teamUser1).isEqualTo(teamUser2);
        teamUser2.setId(2L);
        assertThat(teamUser1).isNotEqualTo(teamUser2);
        teamUser1.setId(null);
        assertThat(teamUser1).isNotEqualTo(teamUser2);
    }
}
