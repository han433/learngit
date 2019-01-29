package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.Vc_agreement;
import com.asiainfo.boss.repository.Vc_agreementRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.asiainfo.boss.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the Vc_agreementResource REST controller.
 *
 * @see Vc_agreementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class Vc_agreementResourceIntTest {

    private static final LocalDate DEFAULT_SIGN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SIGN_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private Vc_agreementRepository vc_agreementRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVc_agreementMockMvc;

    private Vc_agreement vc_agreement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Vc_agreementResource vc_agreementResource = new Vc_agreementResource(vc_agreementRepository);
        this.restVc_agreementMockMvc = MockMvcBuilders.standaloneSetup(vc_agreementResource)
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
    public static Vc_agreement createEntity(EntityManager em) {
        Vc_agreement vc_agreement = new Vc_agreement()
            .sign_date(DEFAULT_SIGN_DATE);
        return vc_agreement;
    }

    @Before
    public void initTest() {
        vc_agreement = createEntity(em);
    }

    @Test
    @Transactional
    public void createVc_agreement() throws Exception {
        int databaseSizeBeforeCreate = vc_agreementRepository.findAll().size();

        // Create the Vc_agreement
        restVc_agreementMockMvc.perform(post("/api/vc-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vc_agreement)))
            .andExpect(status().isCreated());

        // Validate the Vc_agreement in the database
        List<Vc_agreement> vc_agreementList = vc_agreementRepository.findAll();
        assertThat(vc_agreementList).hasSize(databaseSizeBeforeCreate + 1);
        Vc_agreement testVc_agreement = vc_agreementList.get(vc_agreementList.size() - 1);
        assertThat(testVc_agreement.getSign_date()).isEqualTo(DEFAULT_SIGN_DATE);
    }

    @Test
    @Transactional
    public void createVc_agreementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vc_agreementRepository.findAll().size();

        // Create the Vc_agreement with an existing ID
        vc_agreement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVc_agreementMockMvc.perform(post("/api/vc-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vc_agreement)))
            .andExpect(status().isBadRequest());

        // Validate the Vc_agreement in the database
        List<Vc_agreement> vc_agreementList = vc_agreementRepository.findAll();
        assertThat(vc_agreementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVc_agreements() throws Exception {
        // Initialize the database
        vc_agreementRepository.saveAndFlush(vc_agreement);

        // Get all the vc_agreementList
        restVc_agreementMockMvc.perform(get("/api/vc-agreements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vc_agreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].sign_date").value(hasItem(DEFAULT_SIGN_DATE.toString())));
    }

    @Test
    @Transactional
    public void getVc_agreement() throws Exception {
        // Initialize the database
        vc_agreementRepository.saveAndFlush(vc_agreement);

        // Get the vc_agreement
        restVc_agreementMockMvc.perform(get("/api/vc-agreements/{id}", vc_agreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vc_agreement.getId().intValue()))
            .andExpect(jsonPath("$.sign_date").value(DEFAULT_SIGN_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVc_agreement() throws Exception {
        // Get the vc_agreement
        restVc_agreementMockMvc.perform(get("/api/vc-agreements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVc_agreement() throws Exception {
        // Initialize the database
        vc_agreementRepository.saveAndFlush(vc_agreement);
        int databaseSizeBeforeUpdate = vc_agreementRepository.findAll().size();

        // Update the vc_agreement
        Vc_agreement updatedVc_agreement = vc_agreementRepository.findOne(vc_agreement.getId());
        // Disconnect from session so that the updates on updatedVc_agreement are not directly saved in db
        em.detach(updatedVc_agreement);
        updatedVc_agreement
            .sign_date(UPDATED_SIGN_DATE);

        restVc_agreementMockMvc.perform(put("/api/vc-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVc_agreement)))
            .andExpect(status().isOk());

        // Validate the Vc_agreement in the database
        List<Vc_agreement> vc_agreementList = vc_agreementRepository.findAll();
        assertThat(vc_agreementList).hasSize(databaseSizeBeforeUpdate);
        Vc_agreement testVc_agreement = vc_agreementList.get(vc_agreementList.size() - 1);
        assertThat(testVc_agreement.getSign_date()).isEqualTo(UPDATED_SIGN_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVc_agreement() throws Exception {
        int databaseSizeBeforeUpdate = vc_agreementRepository.findAll().size();

        // Create the Vc_agreement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVc_agreementMockMvc.perform(put("/api/vc-agreements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vc_agreement)))
            .andExpect(status().isCreated());

        // Validate the Vc_agreement in the database
        List<Vc_agreement> vc_agreementList = vc_agreementRepository.findAll();
        assertThat(vc_agreementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVc_agreement() throws Exception {
        // Initialize the database
        vc_agreementRepository.saveAndFlush(vc_agreement);
        int databaseSizeBeforeDelete = vc_agreementRepository.findAll().size();

        // Get the vc_agreement
        restVc_agreementMockMvc.perform(delete("/api/vc-agreements/{id}", vc_agreement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vc_agreement> vc_agreementList = vc_agreementRepository.findAll();
        assertThat(vc_agreementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vc_agreement.class);
        Vc_agreement vc_agreement1 = new Vc_agreement();
        vc_agreement1.setId(1L);
        Vc_agreement vc_agreement2 = new Vc_agreement();
        vc_agreement2.setId(vc_agreement1.getId());
        assertThat(vc_agreement1).isEqualTo(vc_agreement2);
        vc_agreement2.setId(2L);
        assertThat(vc_agreement1).isNotEqualTo(vc_agreement2);
        vc_agreement1.setId(null);
        assertThat(vc_agreement1).isNotEqualTo(vc_agreement2);
    }
}
