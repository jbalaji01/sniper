package com.neemshade.sniper.service;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.repository.SnFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Service Implementation for managing SnFile.
 */
@Service
@Transactional
public class SnFileService {

    private final Logger log = LoggerFactory.getLogger(SnFileService.class);

	@Autowired
	private TaskService taskService;
    
    private final SnFileRepository snFileRepository;
    
    @PersistenceContext
    private EntityManager em;

    public SnFileService(SnFileRepository snFileRepository) {
        this.snFileRepository = snFileRepository;
    }

    /**
     * Save a snFile.
     *
     * @param snFile the entity to save
     * @return the persisted entity
     */
    public SnFile save(SnFile snFile) {
        log.debug("Request to save SnFile : {}", snFile);
        return snFileRepository.save(snFile);
    }
    
    /**
     * merge a snFile.
     *
     * @param snFile the entity to merge
     * @return the persisted entity
     */
    public SnFile merge(SnFile snFile) {
        log.debug("Request to merge SnFile : {}", snFile);
		
		Set<Task> tasks = taskService.findBySnFilesId(snFile.getId());
		if(snFile.getTasks() != null) {
			snFile.getTasks().clear();
		}
		em.merge(snFile);
		em.flush();
		if(tasks != null) {
			if(snFile.getTasks() != null)
				snFile.getTasks().addAll(tasks);
			else
				snFile.setTasks(tasks);
		}
		
        return em.merge(snFile);
    }

    /**
     * Get all the snFiles.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SnFile> findAll() {
        log.debug("Request to get all SnFiles");
        return snFileRepository.findAllWithEagerRelationships();
    }

    /**
     * Get one snFile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SnFile findOne(Long id) {
        log.debug("Request to get SnFile : {}", id);
        return snFileRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the snFile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SnFile : {}", id);
        snFileRepository.delete(id);
    }

	public List<SnFile> findSnFilesOfTask(Long taskId) {
		return snFileRepository.findByTasksIdOrderByIsInputDescPeckOrderAsc(taskId);
	}
}
