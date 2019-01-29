package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.HotListUser;
import com.asiainfo.boss.repository.HotListUserRepository;
import com.asiainfo.boss.service.dto.HotListUserDTO;
import com.asiainfo.boss.service.mapper.HotListUserMapper;
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
 * Test class for the HotListUserResource REST controller.
 *
 * @see HotListUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class HotListUserResourceIntTest {

    private static final Long DEFAULT_HOT_LIST_ID = 1L;
    private static final Long UPDATED_HOT_LIST_ID = 2L;

    @Autowired
    private HotListUserRepository hotListUserRepository;

    @Autowired
    private HotListUserMapper hotListUserMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHotListUserMockMvc;

    private HotListUser hotListUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HotListUserResource hotListUserResource = new HotListUserResource(hotListUserRepository, hotListUserMapper);
        this.restHotListUserMockMvc = MockMvcBuilders.standaloneSetup(hotListUserResource)
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
    public static HotListUser createEntity(EntityManager em) {
        HotListUser hotListUser = new HotListUser()
            .hotListId(DEFAULT_HOT_LIST_ID);
        return hotListUser;
    }

    @Before
    public void initTest() {
        hotListUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createHotListUser() throws Exception {
        int databaseSizeBeforeCreate = hotListUserRepository.findAll().size();

        // Create the HotListUser
        HotListUserDTO hotListUserDTO = hotListUserMapper.toDto(hotListUser);
        restHotListUserMockMvc.perform(post("/api/hot-list-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotListUserDTO)))
            .andExpect(status().isCreated());

        // Validate the HotListUser in the database
        List<HotListUser> hotListUserList = hotListUserRepository.findAll();
        assertThat(hotListUserList).hasSize(databaseSizeBeforeCreate + 1);
        HotListUser testHotListUser = hotListUserList.get(hotListUserList.size() - 1);
        assertThat(testHotListUser.getHotListId()).isEqualTo(DEFAULT_HOT_LIST_ID);
    }

    @Test
    @Transactional
    public void createHotListUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hotListUserRepository.findAll().size();

        // Create the HotListUser with an existing ID
        hotListUser.setId(1L);
        HotListUserDTO hotListUserDTO = hotListUserMapper.toDto(hotListUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotListUserMockMvc.perform(post("/api/hot-list-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotListUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HotListUser in the database
        List<HotListUser> hotListUserList = hotListUserRepository.findAll();
        assertThat(hotListUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHotListUsers() throws Exception {
        // Initialize the database
        hotListUserRepository.saveAndFlush(hotListUser);

        // Get all the hotListUserList
        restHotListUserMockMvc.perform(get("/api/hot-list-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotListUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].hotListId").value(hasItem(DEFAULT_HOT_LIST_ID.intValue())));
    }

    @Test
    @Transactional
    public void getHotListUser() throws Exception {
        // Initialize the database
        hotListUserRepository.saveAndFlush(hotListUser);

        // Get the hotListUser
        restHotListUserMockMvc.perform(get("/api/hot-list-users/{id}", hotListUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hotListUser.getId().intValue()))
            .andExpect(jsonPath("$.hotListId").value(DEFAULT_HOT_LIST_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHotListUser() throws Exception {
        // Get the hotListUser
        restHotListUserMockMvc.perform(get("/api/hot-list-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotListUser() throws Exception {
        // Initialize the database
        hotListUserRepository.saveAndFlush(hotListUser);
        int databaseSizeBeforeUpdate = hotListUserRepository.findAll().size();

        // Update the hotListUser
        HotListUser updatedHotListUser = hotListUserRepository.findOne(hotListUser.getId());
        // Disconnect from session so that the updates on updatedHotListUser are not directly saved in db
        em.detach(updatedHotListUser);
        updatedHotListUser
            .hotListId(UPDATED_HOT_LIST_ID);
        HotListUserDTO hotListUserDTO = hotListUserMapper.toDto(updatedHotListUser);

        restHotListUserMockMvc.perform(put("/api/hot-list-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotListUserDTO)))
            .andExpect(status().isOk());

        // Validate the HotListUser in the database
        List<HotListUser> hotListUserList = hotListUserRepository.findAll();
        assertThat(hotListUserList).hasSize(databaseSizeBeforeUpdate);
        HotListUser testHotListUser = hotListUserList.get(hotListUserList.size() - 1);
        assertThat(testHotListUser.getHotListId()).isEqualTo(UPDATED_HOT_LIST_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingHotListUser() throws Exception {
        int databaseSizeBeforeUpdate = hotListUserRepository.findAll().size();

        // Create the HotListUser
        HotListUserDTO hotListUserDTO = hotListUserMapper.toDto(hotListUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHotListUserMockMvc.perform(put("/api/hot-list-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotListUserDTO)))
            .andExpect(status().isCreated());

        // Validate the HotListUser in the database
        List<HotListUser> hotListUserList = hotListUserRepository.findAll();
        assertThat(hotListUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHotListUser() throws Exception {
        // Initialize the database
        hotListUserRepository.saveAndFlush(hotListUser);
        int databaseSizeBeforeDelete = hotListUserRepository.findAll().size();

        // Get the hotListUser
        restHotListUserMockMvc.perform(delete("/api/hot-list-users/{id}", hotListUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HotListUser> hotListUserList = hotListUserRepository.findAll();
        assertThat(hotListUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotListUser.class);
        HotListUser hotListUser1 = new HotListUser();
        hotListUser1.setId(1L);
        HotListUser hotListUser2 = new HotListUser();
        hotListUser2.setId(hotListUser1.getId());
        assertThat(hotListUser1).isEqualTo(hotListUser2);
        hotListUser2.setId(2L);
        assertThat(hotListUser1).isNotEqualTo(hotListUser2);
        hotListUser1.setId(null);
        assertThat(hotListUser1).isNotEqualTo(hotListUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotListUserDTO.class);
        HotListUserDTO hotListUserDTO1 = new HotListUserDTO();
        hotListUserDTO1.setId(1L);
        HotListUserDTO hotListUserDTO2 = new HotListUserDTO();
        assertThat(hotListUserDTO1).isNotEqualTo(hotListUserDTO2);
        hotListUserDTO2.setId(hotListUserDTO1.getId());
        assertThat(hotListUserDTO1).isEqualTo(hotListUserDTO2);
        hotListUserDTO2.setId(2L);
        assertThat(hotListUserDTO1).isNotEqualTo(hotListUserDTO2);
        hotListUserDTO1.setId(null);
        assertThat(hotListUserDTO1).isNotEqualTo(hotListUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(hotListUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(hotListUserMapper.fromId(null)).isNull();
    }
}
