package com.neemshade.sniper.service;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.repository.SnFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing SnFile.
 */
@Service
@Transactional
public class SnFileService {

    private final Logger log = LoggerFactory.getLogger(SnFileService.class);

    private final SnFileRepository snFileRepository;

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
