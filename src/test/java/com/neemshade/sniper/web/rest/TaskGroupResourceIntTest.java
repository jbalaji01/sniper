package com.neemshade.sniper.web.rest;

import com.neemshade.sniper.SniperApp;

import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.repository.TaskGroupRepository;
import com.neemshade.sniper.service.TaskGroupService;
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

/**
 * Test class for the TaskGroupResource REST controller.
 *
 * @see TaskGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SniperApp.class)
public class TaskGroupResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TaskGroupRepository taskGroupRepository;

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskGroupMockMvc;

    private TaskGroup taskGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskGroupResource taskGroupResource = new TaskGroupResource(taskGroupService);
        this.restTaskGroupMockMvc = MockMvcBuilders.standaloneSetup(taskGroupResource)
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
    public static TaskGroup createEntity(EntityManager em) {
        TaskGroup taskGroup = new TaskGroup()
            .groupName(DEFAULT_GROUP_NAME)
            .createdTime(DEFAULT_CREATED_TIME);
        return taskGroup;
    }

    @Before
    public void initTest() {
        taskGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskGroup() throws Exception {
        int databaseSizeBeforeCreate = taskGroupRepository.findAll().size();

        // Create the TaskGroup
        restTaskGroupMockMvc.perform(post("/api/task-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskGroup)))
            .andExpect(status().isCreated());

        // Validate the TaskGroup in the database
        List<TaskGroup> taskGroupList = taskGroupRepository.findAll();
        assertThat(taskGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TaskGroup testTaskGroup = taskGroupList.get(taskGroupList.size() - 1);
        assertThat(testTaskGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testTaskGroup.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
    }

    @Test
    @Transactional
    public void createTaskGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskGroupRepository.findAll().size();

        // Create the TaskGroup with an existing ID
        taskGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskGroupMockMvc.perform(post("/api/task-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskGroup)))
            .andExpect(status().isBadRequest());

        // Validate the TaskGroup in the database
        List<TaskGroup> taskGroupList = taskGroupRepository.findAll();
        assertThat(taskGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTaskGroups() throws Exception {
        // Initialize the database
        taskGroupRepository.saveAndFlush(taskGroup);

        // Get all the taskGroupList
        restTaskGroupMockMvc.perform(get("/api/task-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())));
    }

    @Test
    @Transactional
    public void getTaskGroup() throws Exception {
        // Initialize the database
        taskGroupRepository.saveAndFlush(taskGroup);

        // Get the taskGroup
        restTaskGroupMockMvc.perform(get("/api/task-groups/{id}", taskGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskGroup() throws Exception {
        // Get the taskGroup
        restTaskGroupMockMvc.perform(get("/api/task-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskGroup() throws Exception {
        // Initialize the database
        taskGroupService.save(taskGroup);

        int databaseSizeBeforeUpdate = taskGroupRepository.findAll().size();

        // Update the taskGroup
        TaskGroup updatedTaskGroup = taskGroupRepository.findOne(taskGroup.getId());
        // Disconnect from session so that the updates on updatedTaskGroup are not directly saved in db
        em.detach(updatedTaskGroup);
        updatedTaskGroup
            .groupName(UPDATED_GROUP_NAME)
            .createdTime(UPDATED_CREATED_TIME);

        restTaskGroupMockMvc.perform(put("/api/task-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskGroup)))
            .andExpect(status().isOk());

        // Validate the TaskGroup in the database
        List<TaskGroup> taskGroupList = taskGroupRepository.findAll();
        assertThat(taskGroupList).hasSize(databaseSizeBeforeUpdate);
        TaskGroup testTaskGroup = taskGroupList.get(taskGroupList.size() - 1);
        assertThat(testTaskGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testTaskGroup.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskGroup() throws Exception {
        int databaseSizeBeforeUpdate = taskGroupRepository.findAll().size();

        // Create the TaskGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTaskGroupMockMvc.perform(put("/api/task-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskGroup)))
            .andExpect(status().isCreated());

        // Validate the TaskGroup in the database
        List<TaskGroup> taskGroupList = taskGroupRepository.findAll();
        assertThat(taskGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTaskGroup() throws Exception {
        // Initialize the database
        taskGroupService.save(taskGroup);

        int databaseSizeBeforeDelete = taskGroupRepository.findAll().size();

        // Get the taskGroup
        restTaskGroupMockMvc.perform(delete("/api/task-groups/{id}", taskGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaskGroup> taskGroupList = taskGroupRepository.findAll();
        assertThat(taskGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskGroup.class);
        TaskGroup taskGroup1 = new TaskGroup();
        taskGroup1.setId(1L);
        TaskGroup taskGroup2 = new TaskGroup();
        taskGroup2.setId(taskGroup1.getId());
        assertThat(taskGroup1).isEqualTo(taskGroup2);
        taskGroup2.setId(2L);
        assertThat(taskGroup1).isNotEqualTo(taskGroup2);
        taskGroup1.setId(null);
        assertThat(taskGroup1).isNotEqualTo(taskGroup2);
    }
}
