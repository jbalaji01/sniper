package com.neemshade.sniper.service;

import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.repository.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TaskGroup.
 */
@Service
@Transactional
public class TaskGroupService {

    private final Logger log = LoggerFactory.getLogger(TaskGroupService.class);

    private final TaskGroupRepository taskGroupRepository;

    public TaskGroupService(TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    /**
     * Save a taskGroup.
     *
     * @param taskGroup the entity to save
     * @return the persisted entity
     */
    public TaskGroup save(TaskGroup taskGroup) {
        log.debug("Request to save TaskGroup : {}", taskGroup);
        return taskGroupRepository.save(taskGroup);
    }

    /**
     * Get all the taskGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TaskGroup> findAll(Pageable pageable) {
        log.debug("Request to get all TaskGroups");
        return taskGroupRepository.findAll(pageable);
    }

    /**
     * Get one taskGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TaskGroup findOne(Long id) {
        log.debug("Request to get TaskGroup : {}", id);
        return taskGroupRepository.findOne(id);
    }

    /**
     * Delete the taskGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskGroup : {}", id);
        taskGroupRepository.delete(id);
    }
}
