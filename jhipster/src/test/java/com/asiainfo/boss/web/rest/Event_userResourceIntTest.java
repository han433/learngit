package com.asiainfo.boss.web.rest;

import com.asiainfo.boss.JhipsterApp;

import com.asiainfo.boss.domain.Event_user;
import com.asiainfo.boss.repository.Event_userRepository;
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
 * Test class for the Event_userResource REST controller.
 *
 * @see Event_userResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class Event_userResourceIntTest {

    @Autowired
    private Event_userRepository event_userRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEvent_userMockMvc;

    private Event_user event_user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Event_userResource event_userResource = new Event_userResource(event_userRepository);
        this.restEvent_userMockMvc = MockMvcBuilders.standaloneSetup(event_userResource)
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
    public static Event_user createEntity(EntityManager em) {
        Event_user event_user = new Event_user();
        return event_user;
    }

    @Before
    public void initTest() {
        event_user = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent_user() throws Exception {
        int databaseSizeBeforeCreate = event_userRepository.findAll().size();

        // Create the Event_user
        restEvent_userMockMvc.perform(post("/api/event-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event_user)))
            .andExpect(status().isCreated());

        // Validate the Event_user in the database
        List<Event_user> event_userList = event_userRepository.findAll();
        assertThat(event_userList).hasSize(databaseSizeBeforeCreate + 1);
        Event_user testEvent_user = event_userList.get(event_userList.size() - 1);
    }

    @Test
    @Transactional
    public void createEvent_userWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = event_userRepository.findAll().size();

        // Create the Event_user with an existing ID
        event_user.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvent_userMockMvc.perform(post("/api/event-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event_user)))
            .andExpect(status().isBadRequest());

        // Validate the Event_user in the database
        List<Event_user> event_userList = event_userRepository.findAll();
        assertThat(event_userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEvent_users() throws Exception {
        // Initialize the database
        event_userRepository.saveAndFlush(event_user);

        // Get all the event_userList
        restEvent_userMockMvc.perform(get("/api/event-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event_user.getId().intValue())));
    }

    @Test
    @Transactional
    public void getEvent_user() throws Exception {
        // Initialize the database
        event_userRepository.saveAndFlush(event_user);

        // Get the event_user
        restEvent_userMockMvc.perform(get("/api/event-users/{id}", event_user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event_user.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEvent_user() throws Exception {
        // Get the event_user
        restEvent_userMockMvc.perform(get("/api/event-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent_user() throws Exception {
        // Initialize the database
        event_userRepository.saveAndFlush(event_user);
        int databaseSizeBeforeUpdate = event_userRepository.findAll().size();

        // Update the event_user
        Event_user updatedEvent_user = event_userRepository.findOne(event_user.getId());
        // Disconnect from session so that the updates on updatedEvent_user are not directly saved in db
        em.detach(updatedEvent_user);

        restEvent_userMockMvc.perform(put("/api/event-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvent_user)))
            .andExpect(status().isOk());

        // Validate the Event_user in the database
        List<Event_user> event_userList = event_userRepository.findAll();
        assertThat(event_userList).hasSize(databaseSizeBeforeUpdate);
        Event_user testEvent_user = event_userList.get(event_userList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent_user() throws Exception {
        int databaseSizeBeforeUpdate = event_userRepository.findAll().size();

        // Create the Event_user

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEvent_userMockMvc.perform(put("/api/event-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event_user)))
            .andExpect(status().isCreated());

        // Validate the Event_user in the database
        List<Event_user> event_userList = event_userRepository.findAll();
        assertThat(event_userList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvent_user() throws Exception {
        // Initialize the database
        event_userRepository.saveAndFlush(event_user);
        int databaseSizeBeforeDelete = event_userRepository.findAll().size();

        // Get the event_user
        restEvent_userMockMvc.perform(delete("/api/event-users/{id}", event_user.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Event_user> event_userList = event_userRepository.findAll();
        assertThat(event_userList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event_user.class);
        Event_user event_user1 = new Event_user();
        event_user1.setId(1L);
        Event_user event_user2 = new Event_user();
        event_user2.setId(event_user1.getId());
        assertThat(event_user1).isEqualTo(event_user2);
        event_user2.setId(2L);
        assertThat(event_user1).isNotEqualTo(event_user2);
        event_user1.setId(null);
        assertThat(event_user1).isNotEqualTo(event_user2);
    }
}
