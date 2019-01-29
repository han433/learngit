package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.HotUser;
import com.asiainfo.boss.repository.HotUserRepository;
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
 * Test class for the HotUserResource REST controller.
 *
 * @see HotUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class HotUserResourceIntTest {

    private static final Long DEFAULT_HOTID = 1L;
    private static final Long UPDATED_HOTID = 2L;

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    @Autowired
    private HotUserRepository hotUserRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHotUserMockMvc;

    private HotUser hotUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HotUserResource hotUserResource = new HotUserResource(hotUserRepository);
        this.restHotUserMockMvc = MockMvcBuilders.standaloneSetup(hotUserResource)
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
    public static HotUser createEntity(EntityManager em) {
        HotUser hotUser = new HotUser()
            .hotid(DEFAULT_HOTID)
            .userid(DEFAULT_USERID);
        return hotUser;
    }

    @Before
    public void initTest() {
        hotUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createHotUser() throws Exception {
        int databaseSizeBeforeCreate = hotUserRepository.findAll().size();

        // Create the HotUser
        restHotUserMockMvc.perform(post("/api/hot-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotUser)))
            .andExpect(status().isCreated());

        // Validate the HotUser in the database
        List<HotUser> hotUserList = hotUserRepository.findAll();
        assertThat(hotUserList).hasSize(databaseSizeBeforeCreate + 1);
        HotUser testHotUser = hotUserList.get(hotUserList.size() - 1);
        assertThat(testHotUser.getHotid()).isEqualTo(DEFAULT_HOTID);
        assertThat(testHotUser.getUserid()).isEqualTo(DEFAULT_USERID);
    }

    @Test
    @Transactional
    public void createHotUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hotUserRepository.findAll().size();

        // Create the HotUser with an existing ID
        hotUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotUserMockMvc.perform(post("/api/hot-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotUser)))
            .andExpect(status().isBadRequest());

        // Validate the HotUser in the database
        List<HotUser> hotUserList = hotUserRepository.findAll();
        assertThat(hotUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHotUsers() throws Exception {
        // Initialize the database
        hotUserRepository.saveAndFlush(hotUser);

        // Get all the hotUserList
        restHotUserMockMvc.perform(get("/api/hot-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].hotid").value(hasItem(DEFAULT_HOTID.intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())));
    }

    @Test
    @Transactional
    public void getHotUser() throws Exception {
        // Initialize the database
        hotUserRepository.saveAndFlush(hotUser);

        // Get the hotUser
        restHotUserMockMvc.perform(get("/api/hot-users/{id}", hotUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hotUser.getId().intValue()))
            .andExpect(jsonPath("$.hotid").value(DEFAULT_HOTID.intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHotUser() throws Exception {
        // Get the hotUser
        restHotUserMockMvc.perform(get("/api/hot-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotUser() throws Exception {
        // Initialize the database
        hotUserRepository.saveAndFlush(hotUser);
        int databaseSizeBeforeUpdate = hotUserRepository.findAll().size();

        // Update the hotUser
        HotUser updatedHotUser = hotUserRepository.findOne(hotUser.getId());
        // Disconnect from session so that the updates on updatedHotUser are not directly saved in db
        em.detach(updatedHotUser);
        updatedHotUser
            .hotid(UPDATED_HOTID)
            .userid(UPDATED_USERID);

        restHotUserMockMvc.perform(put("/api/hot-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHotUser)))
            .andExpect(status().isOk());

        // Validate the HotUser in the database
        List<HotUser> hotUserList = hotUserRepository.findAll();
        assertThat(hotUserList).hasSize(databaseSizeBeforeUpdate);
        HotUser testHotUser = hotUserList.get(hotUserList.size() - 1);
        assertThat(testHotUser.getHotid()).isEqualTo(UPDATED_HOTID);
        assertThat(testHotUser.getUserid()).isEqualTo(UPDATED_USERID);
    }

    @Test
    @Transactional
    public void updateNonExistingHotUser() throws Exception {
        int databaseSizeBeforeUpdate = hotUserRepository.findAll().size();

        // Create the HotUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHotUserMockMvc.perform(put("/api/hot-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotUser)))
            .andExpect(status().isCreated());

        // Validate the HotUser in the database
        List<HotUser> hotUserList = hotUserRepository.findAll();
        assertThat(hotUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHotUser() throws Exception {
        // Initialize the database
        hotUserRepository.saveAndFlush(hotUser);
        int databaseSizeBeforeDelete = hotUserRepository.findAll().size();

        // Get the hotUser
        restHotUserMockMvc.perform(delete("/api/hot-users/{id}", hotUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HotUser> hotUserList = hotUserRepository.findAll();
        assertThat(hotUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotUser.class);
        HotUser hotUser1 = new HotUser();
        hotUser1.setId(1L);
        HotUser hotUser2 = new HotUser();
        hotUser2.setId(hotUser1.getId());
        assertThat(hotUser1).isEqualTo(hotUser2);
        hotUser2.setId(2L);
        assertThat(hotUser1).isNotEqualTo(hotUser2);
        hotUser1.setId(null);
        assertThat(hotUser1).isNotEqualTo(hotUser2);
    }
}
