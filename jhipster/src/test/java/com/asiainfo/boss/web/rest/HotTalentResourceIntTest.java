package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.HotTalent;
import com.asiainfo.boss.repository.HotTalentRepository;
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
 * Test class for the HotTalentResource REST controller.
 *
 * @see HotTalentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class HotTalentResourceIntTest {

    private static final Long DEFAULT_HOTID = 1L;
    private static final Long UPDATED_HOTID = 2L;

    private static final Long DEFAULT_TALENTID = 1L;
    private static final Long UPDATED_TALENTID = 2L;

    @Autowired
    private HotTalentRepository hotTalentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHotTalentMockMvc;

    private HotTalent hotTalent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HotTalentResource hotTalentResource = new HotTalentResource(hotTalentRepository);
        this.restHotTalentMockMvc = MockMvcBuilders.standaloneSetup(hotTalentResource)
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
    public static HotTalent createEntity(EntityManager em) {
        HotTalent hotTalent = new HotTalent()
            .hotid(DEFAULT_HOTID)
            .talentid(DEFAULT_TALENTID);
        return hotTalent;
    }

    @Before
    public void initTest() {
        hotTalent = createEntity(em);
    }

    @Test
    @Transactional
    public void createHotTalent() throws Exception {
        int databaseSizeBeforeCreate = hotTalentRepository.findAll().size();

        // Create the HotTalent
        restHotTalentMockMvc.perform(post("/api/hot-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotTalent)))
            .andExpect(status().isCreated());

        // Validate the HotTalent in the database
        List<HotTalent> hotTalentList = hotTalentRepository.findAll();
        assertThat(hotTalentList).hasSize(databaseSizeBeforeCreate + 1);
        HotTalent testHotTalent = hotTalentList.get(hotTalentList.size() - 1);
        assertThat(testHotTalent.getHotid()).isEqualTo(DEFAULT_HOTID);
        assertThat(testHotTalent.getTalentid()).isEqualTo(DEFAULT_TALENTID);
    }

    @Test
    @Transactional
    public void createHotTalentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hotTalentRepository.findAll().size();

        // Create the HotTalent with an existing ID
        hotTalent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotTalentMockMvc.perform(post("/api/hot-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotTalent)))
            .andExpect(status().isBadRequest());

        // Validate the HotTalent in the database
        List<HotTalent> hotTalentList = hotTalentRepository.findAll();
        assertThat(hotTalentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHotTalents() throws Exception {
        // Initialize the database
        hotTalentRepository.saveAndFlush(hotTalent);

        // Get all the hotTalentList
        restHotTalentMockMvc.perform(get("/api/hot-talents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotTalent.getId().intValue())))
            .andExpect(jsonPath("$.[*].hotid").value(hasItem(DEFAULT_HOTID.intValue())))
            .andExpect(jsonPath("$.[*].talentid").value(hasItem(DEFAULT_TALENTID.intValue())));
    }

    @Test
    @Transactional
    public void getHotTalent() throws Exception {
        // Initialize the database
        hotTalentRepository.saveAndFlush(hotTalent);

        // Get the hotTalent
        restHotTalentMockMvc.perform(get("/api/hot-talents/{id}", hotTalent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hotTalent.getId().intValue()))
            .andExpect(jsonPath("$.hotid").value(DEFAULT_HOTID.intValue()))
            .andExpect(jsonPath("$.talentid").value(DEFAULT_TALENTID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHotTalent() throws Exception {
        // Get the hotTalent
        restHotTalentMockMvc.perform(get("/api/hot-talents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotTalent() throws Exception {
        // Initialize the database
        hotTalentRepository.saveAndFlush(hotTalent);
        int databaseSizeBeforeUpdate = hotTalentRepository.findAll().size();

        // Update the hotTalent
        HotTalent updatedHotTalent = hotTalentRepository.findOne(hotTalent.getId());
        // Disconnect from session so that the updates on updatedHotTalent are not directly saved in db
        em.detach(updatedHotTalent);
        updatedHotTalent
            .hotid(UPDATED_HOTID)
            .talentid(UPDATED_TALENTID);

        restHotTalentMockMvc.perform(put("/api/hot-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHotTalent)))
            .andExpect(status().isOk());

        // Validate the HotTalent in the database
        List<HotTalent> hotTalentList = hotTalentRepository.findAll();
        assertThat(hotTalentList).hasSize(databaseSizeBeforeUpdate);
        HotTalent testHotTalent = hotTalentList.get(hotTalentList.size() - 1);
        assertThat(testHotTalent.getHotid()).isEqualTo(UPDATED_HOTID);
        assertThat(testHotTalent.getTalentid()).isEqualTo(UPDATED_TALENTID);
    }

    @Test
    @Transactional
    public void updateNonExistingHotTalent() throws Exception {
        int databaseSizeBeforeUpdate = hotTalentRepository.findAll().size();

        // Create the HotTalent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHotTalentMockMvc.perform(put("/api/hot-talents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotTalent)))
            .andExpect(status().isCreated());

        // Validate the HotTalent in the database
        List<HotTalent> hotTalentList = hotTalentRepository.findAll();
        assertThat(hotTalentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHotTalent() throws Exception {
        // Initialize the database
        hotTalentRepository.saveAndFlush(hotTalent);
        int databaseSizeBeforeDelete = hotTalentRepository.findAll().size();

        // Get the hotTalent
        restHotTalentMockMvc.perform(delete("/api/hot-talents/{id}", hotTalent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HotTalent> hotTalentList = hotTalentRepository.findAll();
        assertThat(hotTalentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotTalent.class);
        HotTalent hotTalent1 = new HotTalent();
        hotTalent1.setId(1L);
        HotTalent hotTalent2 = new HotTalent();
        hotTalent2.setId(hotTalent1.getId());
        assertThat(hotTalent1).isEqualTo(hotTalent2);
        hotTalent2.setId(2L);
        assertThat(hotTalent1).isNotEqualTo(hotTalent2);
        hotTalent1.setId(null);
        assertThat(hotTalent1).isNotEqualTo(hotTalent2);
    }
}
