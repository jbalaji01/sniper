package com.neemshade.sniper.service;

import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.repository.TaskRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Task.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Save a task.
     *
     * @param task the entity to save
     * @return the persisted entity
     */
    public Task save(Task task) {
        log.debug("Request to save Task : {}", task);
        return taskRepository.save(task);
    }

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable);
    }

    /**
     * Get one task by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Task findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findOne(id);
    }

    /**
     * Delete the task by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.delete(id);
    }

	public Page<Task> findTasksOfTaskGroup(Long taskGroupId, Instant fromDate, Instant toDate, Pageable pageable) {

		return taskRepository.findByTaskGroup_IdAndCreatedTimeBetween(taskGroupId, fromDate, toDate, pageable);
	}

	public Page<Task> findActiveTasksOfUser(Instant fromDate, Instant toDate, Pageable pageable) {
		return taskRepository.findActiveTasksOfUser(fromDate, toDate, pageable);
	}


	public Page<Task> findAllTasksOfUser(Instant fromDate, Instant toDate, Pageable pageable) {
		return taskRepository.findAllTasksOfUser(fromDate, toDate, pageable);
	}

	public List<Task> findTasksOfTaskGroup(Long taskGroupId) {
		return taskRepository.findByTaskGroupIdOrderByPeckOrder(taskGroupId);
	}

	public List<?> findStatusCount(Long taskGroupId) {
		return taskRepository.findStatusCount(taskGroupId);
	}

	public Set<Task> findBySnFilesId(Long snFileId) {
		return taskRepository.findBySnFilesId(snFileId);
	}
}
