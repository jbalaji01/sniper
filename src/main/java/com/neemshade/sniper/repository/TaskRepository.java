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
import org.springframework.data.repository.query.Param;


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
	
	@Query("select task from Task task "
			+ " where task.owner.user.login = ?#{principal.username} "
			+ " and task.isActive = true "
			+ " and task.createdTime between :fromDate and :toDate ")
	Page<Task> findActiveTasksOfUser(@Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);

	
	@Query("select task from Task task "
			+ " where ("
			+ " task.owner.user.login = ?#{principal.username} "
			+ " or task.transcript.user.login = ?#{principal.username} "
			+ " or task.editor.user.login = ?#{principal.username} "
			+ " or task.manager.user.login = ?#{principal.username} )"
			+ " and task.createdTime between :fromDate and :toDate ")
	Page<Task> findAllTasksOfUser(@Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate, Pageable pageable);
}
