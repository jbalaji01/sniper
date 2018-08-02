package com.neemshade.sniper.service;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.repository.SnFileRepository;
import com.neemshade.sniper.service.dto.SnFileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	@Autowired
    private SnFileBlobService snFileBlobService;

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

	public List<SnFileDTO> findSnFilesOfTask(Long taskId) {
        return snFileRepository.findByTasksIdOrderByIsInputDescPeckOrderAsc(taskId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
	}

    public SnFileDTO toDto(SnFile snFile) {
        SnFileDTO snFileDTO = new SnFileDTO();
        snFileDTO.setId(snFile.getId());
        snFileDTO.setFilePath(snFile.getFilePath());
        snFileDTO.setFileName(snFile.getFileName());
        snFileDTO.setFileExt(snFile.getFileExt());
        snFileDTO.setOrigin(snFile.getOrigin());
        snFileDTO.setInput(snFile.isIsInput());
        snFileDTO.setAudio(snFile.isIsAudio());
        snFileDTO.setUploadedTime(snFile.getUploadedTime());
        snFileDTO.setActualTimeFrame(snFile.getActualTimeFrame());
        snFileDTO.setAdjustedTimeFrame(snFile.getAdjustedTimeFrame());
        snFileDTO.setFinalTimeFrame(snFile.getFinalTimeFrame());
        snFileDTO.setWsActualLineCount(snFile.getWsActualLineCount());
        snFileDTO.setWsAdjustedLineCount(snFile.getWsAdjustedLineCount());
        snFileDTO.setWsFinalLineCount(snFile.getWsFinalLineCount());
        snFileDTO.setWosActualLineCount(snFile.getWosActualLineCount());
        snFileDTO.setWosAdjustedLineCount(snFile.getWosAdjustedLineCount());
        snFileDTO.setWsFinalLineCount(snFile.getWsFinalLineCount());
        snFileDTO.setChosenFactor(snFile.getChosenFactor());
        snFileDTO.setPeckOrder(snFile.getPeckOrder());
        snFileDTO.setPatients(snFile.getPatients());
        snFileDTO.setTasks(snFile.getTasks());
        snFileDTO.setUploader(snFile.getUploader());
        snFileDTO.setSnFileBlob(snFileBlobService.toDto(snFile.getSnFileBlob()));
        return snFileDTO;
    }

    public SnFile fromDto(SnFileDTO snFileDto) {
        SnFile snFile = new SnFile();
        snFile.setId(snFileDto.getId());
        snFile.setFilePath(snFileDto.getFilePath());
        snFile.setFileName(snFileDto.getFileName());
        snFile.setFileExt(snFileDto.getFileExt());
        snFile.setOrigin(snFileDto.getOrigin());
        snFile.setIsInput(snFileDto.getInput());
        snFile.setIsAudio(snFileDto.getAudio());
        snFile.setUploadedTime(snFileDto.getUploadedTime());
        snFile.setActualTimeFrame(snFileDto.getActualTimeFrame());
        snFile.setAdjustedTimeFrame(snFileDto.getAdjustedTimeFrame());
        snFile.setFinalTimeFrame(snFileDto.getFinalTimeFrame());
        snFile.setWsActualLineCount(snFileDto.getWsActualLineCount());
        snFile.setWsAdjustedLineCount(snFileDto.getWsAdjustedLineCount());
        snFile.setWsFinalLineCount(snFileDto.getWsFinalLineCount());
        snFile.setWosActualLineCount(snFileDto.getWosActualLineCount());
        snFile.setWosAdjustedLineCount(snFileDto.getWosAdjustedLineCount());
        snFile.setWsFinalLineCount(snFileDto.getWsFinalLineCount());
        snFile.setChosenFactor(snFileDto.getChosenFactor());
        snFile.setPeckOrder(snFileDto.getPeckOrder());
        snFile.setPatients(snFileDto.getPatients());
        snFile.setTasks(snFileDto.getTasks());
        snFile.setUploader(snFileDto.getUploader());
        snFile.setSnFileBlob(snFileBlobService.fromDto(snFileDto.getSnFileBlob()));
        return snFile;
    }
}
