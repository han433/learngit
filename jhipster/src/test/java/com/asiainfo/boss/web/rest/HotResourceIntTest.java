package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.Hot;
import com.asiainfo.boss.repository.HotRepository;
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
 * Test class for the HotResource REST controller.
 *
 * @see HotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class HotResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private HotRepository hotRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHotMockMvc;

    private Hot hot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HotResource hotResource = new HotResource(hotRepository);
        this.restHotMockMvc = MockMvcBuilders.standaloneSetup(hotResource)
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
    public static Hot createEntity(EntityManager em) {
        Hot hot = new Hot()
            .title(DEFAULT_TITLE)
            .notes(DEFAULT_NOTES);
        return hot;
    }

    @Before
    public void initTest() {
        hot = createEntity(em);
    }

    @Test
    @Transactional
    public void createHot() throws Exception {
        int databaseSizeBeforeCreate = hotRepository.findAll().size();

        // Create the Hot
        restHotMockMvc.perform(post("/api/hots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isCreated());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeCreate + 1);
        Hot testHot = hotList.get(hotList.size() - 1);
        assertThat(testHot.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHot.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createHotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hotRepository.findAll().size();

        // Create the Hot with an existing ID
        hot.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotMockMvc.perform(post("/api/hots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isBadRequest());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHots() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        // Get all the hotList
        restHotMockMvc.perform(get("/api/hots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hot.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getHot() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        // Get the hot
        restHotMockMvc.perform(get("/api/hots/{id}", hot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hot.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHot() throws Exception {
        // Get the hot
        restHotMockMvc.perform(get("/api/hots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHot() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();

        // Update the hot
        Hot updatedHot = hotRepository.findOne(hot.getId());
        // Disconnect from session so that the updates on updatedHot are not directly saved in db
        em.detach(updatedHot);
        updatedHot
            .title(UPDATED_TITLE)
            .notes(UPDATED_NOTES);

        restHotMockMvc.perform(put("/api/hots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHot)))
            .andExpect(status().isOk());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
        Hot testHot = hotList.get(hotList.size() - 1);
        assertThat(testHot.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHot.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();

        // Create the Hot

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHotMockMvc.perform(put("/api/hots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isCreated());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHot() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);
        int databaseSizeBeforeDelete = hotRepository.findAll().size();

        // Get the hot
        restHotMockMvc.perform(delete("/api/hots/{id}", hot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hot.class);
        Hot hot1 = new Hot();
        hot1.setId(1L);
        Hot hot2 = new Hot();
        hot2.setId(hot1.getId());
        assertThat(hot1).isEqualTo(hot2);
        hot2.setId(2L);
        assertThat(hot1).isNotEqualTo(hot2);
        hot1.setId(null);
        assertThat(hot1).isNotEqualTo(hot2);
    }
}
