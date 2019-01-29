package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.Sequence;
import com.asiainfo.boss.repository.SequenceRepository;
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
 * Test class for the SequenceResource REST controller.
 *
 * @see SequenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class SequenceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_INCREMENT = "AAAAAAAAAA";
    private static final String UPDATED_INCREMENT = "BBBBBBBBBB";

    private static final String DEFAULT_MAX_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_MAX_VALUE = "BBBBBBBBBB";

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSequenceMockMvc;

    private Sequence sequence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SequenceResource sequenceResource = new SequenceResource(sequenceRepository);
        this.restSequenceMockMvc = MockMvcBuilders.standaloneSetup(sequenceResource)
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
    public static Sequence createEntity(EntityManager em) {
        Sequence sequence = new Sequence()
            .name(DEFAULT_NAME)
            .currentValue(DEFAULT_CURRENT_VALUE)
            .increment(DEFAULT_INCREMENT)
            .maxValue(DEFAULT_MAX_VALUE);
        return sequence;
    }

    @Before
    public void initTest() {
        sequence = createEntity(em);
    }

    @Test
    @Transactional
    public void createSequence() throws Exception {
        int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

        // Create the Sequence
        restSequenceMockMvc.perform(post("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequence)))
            .andExpect(status().isCreated());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeCreate + 1);
        Sequence testSequence = sequenceList.get(sequenceList.size() - 1);
        assertThat(testSequence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSequence.getCurrentValue()).isEqualTo(DEFAULT_CURRENT_VALUE);
        assertThat(testSequence.getIncrement()).isEqualTo(DEFAULT_INCREMENT);
        assertThat(testSequence.getMaxValue()).isEqualTo(DEFAULT_MAX_VALUE);
    }

    @Test
    @Transactional
    public void createSequenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

        // Create the Sequence with an existing ID
        sequence.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSequenceMockMvc.perform(post("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequence)))
            .andExpect(status().isBadRequest());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSequences() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList
        restSequenceMockMvc.perform(get("/api/sequences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].increment").value(hasItem(DEFAULT_INCREMENT.toString())))
            .andExpect(jsonPath("$.[*].maxValue").value(hasItem(DEFAULT_MAX_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", sequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sequence.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.toString()))
            .andExpect(jsonPath("$.increment").value(DEFAULT_INCREMENT.toString()))
            .andExpect(jsonPath("$.maxValue").value(DEFAULT_MAX_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSequence() throws Exception {
        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

        // Update the sequence
        Sequence updatedSequence = sequenceRepository.findOne(sequence.getId());
        // Disconnect from session so that the updates on updatedSequence are not directly saved in db
        em.detach(updatedSequence);
        updatedSequence
            .name(UPDATED_NAME)
            .currentValue(UPDATED_CURRENT_VALUE)
            .increment(UPDATED_INCREMENT)
            .maxValue(UPDATED_MAX_VALUE);

        restSequenceMockMvc.perform(put("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSequence)))
            .andExpect(status().isOk());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeUpdate);
        Sequence testSequence = sequenceList.get(sequenceList.size() - 1);
        assertThat(testSequence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSequence.getCurrentValue()).isEqualTo(UPDATED_CURRENT_VALUE);
        assertThat(testSequence.getIncrement()).isEqualTo(UPDATED_INCREMENT);
        assertThat(testSequence.getMaxValue()).isEqualTo(UPDATED_MAX_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSequence() throws Exception {
        int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

        // Create the Sequence

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSequenceMockMvc.perform(put("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequence)))
            .andExpect(status().isCreated());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        int databaseSizeBeforeDelete = sequenceRepository.findAll().size();

        // Get the sequence
        restSequenceMockMvc.perform(delete("/api/sequences/{id}", sequence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sequence.class);
        Sequence sequence1 = new Sequence();
        sequence1.setId(1L);
        Sequence sequence2 = new Sequence();
        sequence2.setId(sequence1.getId());
        assertThat(sequence1).isEqualTo(sequence2);
        sequence2.setId(2L);
        assertThat(sequence1).isNotEqualTo(sequence2);
        sequence1.setId(null);
        assertThat(sequence1).isNotEqualTo(sequence2);
    }
}
