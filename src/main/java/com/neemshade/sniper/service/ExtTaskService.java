package com.neemshade.sniper.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neemshade.sniper.domain.TaskGroup;
import com.neemshade.sniper.repository.TaskGroupRepository;

@Service
@Transactional
public class ExtTaskService {
	private final Logger log = LoggerFactory.getLogger(TaskGroupService.class);

    private final TaskGroupRepository taskGroupRepository;

    public ExtTaskService(TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

	public Page<TaskGroup> findTaskGroupByDates(Instant fromDate, Instant toDate, Pageable pageable) {
		
		return taskGroupRepository.findAllByCreatedTimeBetweenOrderByCreatedTimeDesc(fromDate, toDate, pageable);
	}
}
