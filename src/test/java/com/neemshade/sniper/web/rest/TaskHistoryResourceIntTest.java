package com.neemshade.sniper.web.rest;

import com.neemshade.sniper.SniperApp;

import com.neemshade.sniper.domain.TaskHistory;
import com.neemshade.sniper.repository.TaskHistoryRepository;
import com.neemshade.sniper.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.neemshade.sniper.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neemshade.sniper.domain.enumeration.TaskStatus;
/**
 * Test class for the TaskHistoryResource REST controller.
 *
 * @see TaskHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SniperApp.class)
public class TaskHistoryResourceIntTest {

    private static final TaskStatus DEFAULT_TASK_STATUS = TaskStatus.CREATED;
    private static final TaskStatus UPDATED_TASK_STATUS = TaskStatus.ASSIGNED;

    private static final Instant DEFAULT_PUNCH_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUNCH_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskHistoryMockMvc;

    private TaskHistory taskHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskHistoryResource taskHistoryResource = new TaskHistoryResource(taskHistoryRepository);
        this.restTaskHistoryMockMvc = MockMvcBuilders.standaloneSetup(taskHistoryResource)
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
    public static TaskHistory createEntity(EntityManager em) {
        TaskHistory taskHistory = new TaskHistory()
            .taskStatus(DEFAULT_TASK_STATUS)
            .punchTime(DEFAULT_PUNCH_TIME)
            .notes(DEFAULT_NOTES);
        return taskHistory;
    }

    @Before
    public void initTest() {
        taskHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskHistory() throws Exception {
        int databaseSizeBeforeCreate = taskHistoryRepository.findAll().size();

        // Create the TaskHistory
        restTaskHistoryMockMvc.perform(post("/api/task-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskHistory)))
            .andExpect(status().isCreated());

        // Validate the TaskHistory in the database
        List<TaskHistory> taskHistoryList = taskHistoryRepository.findAll();
        assertThat(taskHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TaskHistory testTaskHistory = taskHistoryList.get(taskHistoryList.size() - 1);
        assertThat(testTaskHistory.getTaskStatus()).isEqualTo(DEFAULT_TASK_STATUS);
        assertThat(testTaskHistory.getPunchTime()).isEqualTo(DEFAULT_PUNCH_TIME);
        assertThat(testTaskHistory.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createTaskHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskHistoryRepository.findAll().size();

        // Create the TaskHistory with an existing ID
        taskHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskHistoryMockMvc.perform(post("/api/task-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskHistory)))
            .andExpect(status().isBadRequest());

        // Validate the TaskHistory in the database
        List<TaskHistory> taskHistoryList = taskHistoryRepository.findAll();
        assertThat(taskHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTaskHistories() throws Exception {
        // Initialize the database
        taskHistoryRepository.saveAndFlush(taskHistory);

        // Get all the taskHistoryList
        restTaskHistoryMockMvc.perform(get("/api/task-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskStatus").value(hasItem(DEFAULT_TASK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].punchTime").value(hasItem(DEFAULT_PUNCH_TIME.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getTaskHistory() throws Exception {
        // Initialize the database
        taskHistoryRepository.saveAndFlush(taskHistory);

        // Get the taskHistory
        restTaskHistoryMockMvc.perform(get("/api/task-histories/{id}", taskHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskHistory.getId().intValue()))
            .andExpect(jsonPath("$.taskStatus").value(DEFAULT_TASK_STATUS.toString()))
            .andExpect(jsonPath("$.punchTime").value(DEFAULT_PUNCH_TIME.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskHistory() throws Exception {
        // Get the taskHistory
        restTaskHistoryMockMvc.perform(get("/api/task-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskHistory() throws Exception {
        // Initialize the database
        taskHistoryRepository.saveAndFlush(taskHistory);
        int databaseSizeBeforeUpdate = taskHistoryRepository.findAll().size();

        // Update the taskHistory
        TaskHistory updatedTaskHistory = taskHistoryRepository.findOne(taskHistory.getId());
        // Disconnect from session so that the updates on updatedTaskHistory are not directly saved in db
        em.detach(updatedTaskHistory);
        updatedTaskHistory
            .taskStatus(UPDATED_TASK_STATUS)
            .punchTime(UPDATED_PUNCH_TIME)
            .notes(UPDATED_NOTES);

        restTaskHistoryMockMvc.perform(put("/api/task-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskHistory)))
            .andExpect(status().isOk());

        // Validate the TaskHistory in the database
        List<TaskHistory> taskHistoryList = taskHistoryRepository.findAll();
        assertThat(taskHistoryList).hasSize(databaseSizeBeforeUpdate);
        TaskHistory testTaskHistory = taskHistoryList.get(taskHistoryList.size() - 1);
        assertThat(testTaskHistory.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
        assertThat(testTaskHistory.getPunchTime()).isEqualTo(UPDATED_PUNCH_TIME);
        assertThat(testTaskHistory.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskHistory() throws Exception {
        int databaseSizeBeforeUpdate = taskHistoryRepository.findAll().size();

        // Create the TaskHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskHistoryMockMvc.perform(put("/api/task-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskHistory)))
            .andExpect(status().isCreated());

        // Validate the TaskHistory in the database
        List<TaskHistory> taskHistoryList = taskHistoryRepository.findAll();
        assertThat(taskHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskHistory() throws Exception {
        // Initialize the database
        taskHistoryRepository.saveAndFlush(taskHistory);
        int databaseSizeBeforeDelete = taskHistoryRepository.findAll().size();

        // Get the taskHistory
        restTaskHistoryMockMvc.perform(delete("/api/task-histories/{id}", taskHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskHistory> taskHistoryList = taskHistoryRepository.findAll();
        assertThat(taskHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskHistory.class);
        TaskHistory taskHistory1 = new TaskHistory();
        taskHistory1.setId(1L);
        TaskHistory taskHistory2 = new TaskHistory();
        taskHistory2.setId(taskHistory1.getId());
        assertThat(taskHistory1).isEqualTo(taskHistory2);
        taskHistory2.setId(2L);
        assertThat(taskHistory1).isNotEqualTo(taskHistory2);
        taskHistory1.setId(null);
        assertThat(taskHistory1).isNotEqualTo(taskHistory2);
    }
}
