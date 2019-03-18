package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.TaskGroup;
import org.springframework.stereotype.Repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TaskGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {

	Page<TaskGroup> findAllByCreatedTimeBetweenOrderByCreatedTimeDesc(Instant fromDate, Instant toDate, Pageable pageable);

	TaskGroup findFirstByTasksId(Long taskId);
}
