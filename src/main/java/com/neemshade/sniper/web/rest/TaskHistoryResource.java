package com.neemshade.sniper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.TaskHistory;

import com.neemshade.sniper.repository.TaskHistoryRepository;
import com.neemshade.sniper.web.rest.errors.BadRequestAlertException;
import com.neemshade.sniper.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TaskHistory.
 */
@RestController
@RequestMapping("/api")
public class TaskHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TaskHistoryResource.class);

    private static final String ENTITY_NAME = "taskHistory";

    private final TaskHistoryRepository taskHistoryRepository;

    public TaskHistoryResource(TaskHistoryRepository taskHistoryRepository) {
        this.taskHistoryRepository = taskHistoryRepository;
    }

    /**
     * POST  /task-histories : Create a new taskHistory.
     *
     * @param taskHistory the taskHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskHistory, or with status 400 (Bad Request) if the taskHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-histories")
    @Timed
    public ResponseEntity<TaskHistory> createTaskHistory(@RequestBody TaskHistory taskHistory) throws URISyntaxException {
        log.debug("REST request to save TaskHistory : {}", taskHistory);
        if (taskHistory.getId() != null) {
            throw new BadRequestAlertException("A new taskHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskHistory result = taskHistoryRepository.save(taskHistory);
        return ResponseEntity.created(new URI("/api/task-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-histories : Updates an existing taskHistory.
     *
     * @param taskHistory the taskHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskHistory,
     * or with status 400 (Bad Request) if the taskHistory is not valid,
     * or with status 500 (Internal Server Error) if the taskHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-histories")
    @Timed
    public ResponseEntity<TaskHistory> updateTaskHistory(@RequestBody TaskHistory taskHistory) throws URISyntaxException {
        log.debug("REST request to update TaskHistory : {}", taskHistory);
        if (taskHistory.getId() == null) {
            return createTaskHistory(taskHistory);
        }
        TaskHistory result = taskHistoryRepository.save(taskHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-histories : get all the taskHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taskHistories in body
     */
    @GetMapping("/task-histories")
    @Timed
    public List<TaskHistory> getAllTaskHistories() {
        log.debug("REST request to get all TaskHistories");
        return taskHistoryRepository.findAll();
        }

    /**
     * GET  /task-histories/:id : get the "id" taskHistory.
     *
     * @param id the id of the taskHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskHistory, or with status 404 (Not Found)
     */
    @GetMapping("/task-histories/{id}")
    @Timed
    public ResponseEntity<TaskHistory> getTaskHistory(@PathVariable Long id) {
        log.debug("REST request to get TaskHistory : {}", id);
        TaskHistory taskHistory = taskHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskHistory));
    }

    /**
     * DELETE  /task-histories/:id : delete the "id" taskHistory.
     *
     * @param id the id of the taskHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskHistory(@PathVariable Long id) {
        log.debug("REST request to delete TaskHistory : {}", id);
        taskHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
