package com.neemshade.sniper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.service.TaskGroupService;
import com.neemshade.sniper.web.rest.errors.BadRequestAlertException;
import com.neemshade.sniper.web.rest.util.HeaderUtil;
import com.neemshade.sniper.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TaskGroup.
 */
@RestController
@RequestMapping("/api")
public class TaskGroupResource {

    private final Logger log = LoggerFactory.getLogger(TaskGroupResource.class);

    private static final String ENTITY_NAME = "taskGroup";

    private final TaskGroupService taskGroupService;

    public TaskGroupResource(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    /**
     * POST  /task-groups : Create a new taskGroup.
     *
     * @param taskGroup the taskGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskGroup, or with status 400 (Bad Request) if the taskGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/task-groups")
    @Timed
    public ResponseEntity<TaskGroup> createTaskGroup(@RequestBody TaskGroup taskGroup) throws URISyntaxException {
        log.debug("REST request to save TaskGroup : {}", taskGroup);
        if (taskGroup.getId() != null) {
            throw new BadRequestAlertException("A new taskGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskGroup result = taskGroupService.save(taskGroup);
        return ResponseEntity.created(new URI("/api/task-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /task-groups : Updates an existing taskGroup.
     *
     * @param taskGroup the taskGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskGroup,
     * or with status 400 (Bad Request) if the taskGroup is not valid,
     * or with status 500 (Internal Server Error) if the taskGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/task-groups")
    @Timed
    public ResponseEntity<TaskGroup> updateTaskGroup(@RequestBody TaskGroup taskGroup) throws URISyntaxException {
        log.debug("REST request to update TaskGroup : {}", taskGroup);
        if (taskGroup.getId() == null) {
            return createTaskGroup(taskGroup);
        }
        TaskGroup result = taskGroupService.save(taskGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taskGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /task-groups : get all the taskGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taskGroups in body
     */
    @GetMapping("/task-groups")
    @Timed
    public ResponseEntity<List<TaskGroup>> getAllTaskGroups(Pageable pageable) {
        log.debug("REST request to get a page of TaskGroups");
        Page<TaskGroup> page = taskGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/task-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /task-groups/:id : get the "id" taskGroup.
     *
     * @param id the id of the taskGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskGroup, or with status 404 (Not Found)
     */
    @GetMapping("/task-groups/{id}")
    @Timed
    public ResponseEntity<TaskGroup> getTaskGroup(@PathVariable Long id) {
        log.debug("REST request to get TaskGroup : {}", id);
        TaskGroup taskGroup = taskGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(taskGroup));
    }

    /**
     * DELETE  /task-groups/:id : delete the "id" taskGroup.
     *
     * @param id the id of the taskGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/task-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaskGroup(@PathVariable Long id) {
        log.debug("REST request to delete TaskGroup : {}", id);
        taskGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
