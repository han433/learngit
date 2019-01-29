package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.ApnParam;
import com.asiainfo.boss.repository.ApnParamRepository;
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
 * Test class for the ApnParamResource REST controller.
 *
 * @see ApnParamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class ApnParamResourceIntTest {

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_PARAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_NAME = "BBBBBBBBBB";

    @Autowired
    private ApnParamRepository apnParamRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApnParamMockMvc;

    private ApnParam apnParam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApnParamResource apnParamResource = new ApnParamResource(apnParamRepository);
        this.restApnParamMockMvc = MockMvcBuilders.standaloneSetup(apnParamResource)
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
    public static ApnParam createEntity(EntityManager em) {
        ApnParam apnParam = new ApnParam()
            .tenantId(DEFAULT_TENANT_ID)
            .paramName(DEFAULT_PARAM_NAME);
        return apnParam;
    }

    @Before
    public void initTest() {
        apnParam = createEntity(em);
    }

    @Test
    @Transactional
    public void createApnParam() throws Exception {
        int databaseSizeBeforeCreate = apnParamRepository.findAll().size();

        // Create the ApnParam
        restApnParamMockMvc.perform(post("/api/apn-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apnParam)))
            .andExpect(status().isCreated());

        // Validate the ApnParam in the database
        List<ApnParam> apnParamList = apnParamRepository.findAll();
        assertThat(apnParamList).hasSize(databaseSizeBeforeCreate + 1);
        ApnParam testApnParam = apnParamList.get(apnParamList.size() - 1);
        assertThat(testApnParam.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testApnParam.getParamName()).isEqualTo(DEFAULT_PARAM_NAME);
    }

    @Test
    @Transactional
    public void createApnParamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apnParamRepository.findAll().size();

        // Create the ApnParam with an existing ID
        apnParam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApnParamMockMvc.perform(post("/api/apn-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apnParam)))
            .andExpect(status().isBadRequest());

        // Validate the ApnParam in the database
        List<ApnParam> apnParamList = apnParamRepository.findAll();
        assertThat(apnParamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApnParams() throws Exception {
        // Initialize the database
        apnParamRepository.saveAndFlush(apnParam);

        // Get all the apnParamList
        restApnParamMockMvc.perform(get("/api/apn-params?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apnParam.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].paramName").value(hasItem(DEFAULT_PARAM_NAME.toString())));
    }

    @Test
    @Transactional
    public void getApnParam() throws Exception {
        // Initialize the database
        apnParamRepository.saveAndFlush(apnParam);

        // Get the apnParam
        restApnParamMockMvc.perform(get("/api/apn-params/{id}", apnParam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apnParam.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.paramName").value(DEFAULT_PARAM_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApnParam() throws Exception {
        // Get the apnParam
        restApnParamMockMvc.perform(get("/api/apn-params/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApnParam() throws Exception {
        // Initialize the database
        apnParamRepository.saveAndFlush(apnParam);
        int databaseSizeBeforeUpdate = apnParamRepository.findAll().size();

        // Update the apnParam
        ApnParam updatedApnParam = apnParamRepository.findOne(apnParam.getId());
        // Disconnect from session so that the updates on updatedApnParam are not directly saved in db
        em.detach(updatedApnParam);
        updatedApnParam
            .tenantId(UPDATED_TENANT_ID)
            .paramName(UPDATED_PARAM_NAME);

        restApnParamMockMvc.perform(put("/api/apn-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApnParam)))
            .andExpect(status().isOk());

        // Validate the ApnParam in the database
        List<ApnParam> apnParamList = apnParamRepository.findAll();
        assertThat(apnParamList).hasSize(databaseSizeBeforeUpdate);
        ApnParam testApnParam = apnParamList.get(apnParamList.size() - 1);
        assertThat(testApnParam.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testApnParam.getParamName()).isEqualTo(UPDATED_PARAM_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingApnParam() throws Exception {
        int databaseSizeBeforeUpdate = apnParamRepository.findAll().size();

        // Create the ApnParam

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApnParamMockMvc.perform(put("/api/apn-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apnParam)))
            .andExpect(status().isCreated());

        // Validate the ApnParam in the database
        List<ApnParam> apnParamList = apnParamRepository.findAll();
        assertThat(apnParamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApnParam() throws Exception {
        // Initialize the database
        apnParamRepository.saveAndFlush(apnParam);
        int databaseSizeBeforeDelete = apnParamRepository.findAll().size();

        // Get the apnParam
        restApnParamMockMvc.perform(delete("/api/apn-params/{id}", apnParam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApnParam> apnParamList = apnParamRepository.findAll();
        assertThat(apnParamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApnParam.class);
        ApnParam apnParam1 = new ApnParam();
        apnParam1.setId(1L);
        ApnParam apnParam2 = new ApnParam();
        apnParam2.setId(apnParam1.getId());
        assertThat(apnParam1).isEqualTo(apnParam2);
        apnParam2.setId(2L);
        assertThat(apnParam1).isNotEqualTo(apnParam2);
        apnParam1.setId(null);
        assertThat(apnParam1).isNotEqualTo(apnParam2);
    }
}
