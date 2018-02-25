package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskGroup;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	// find the max peckOrder for a given taskGroup
	Optional<Task> findFirstByTaskGroupIdOrderByPeckOrderDesc(Long taskGroupId);
	
	Page<Task> findByTaskGroup_IdAndCreatedTimeBetween(Long taskGroupId, Instant fromDate, Instant toDate, Pageable pageable);

	List<Task> findByTaskGroupIdOrderByPeckOrder(Long taskGroupId);

}
