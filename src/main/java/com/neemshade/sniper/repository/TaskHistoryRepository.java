package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.TaskHistory;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TaskHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

	List<TaskHistory> findByTaskIdOrderByPunchTimeDesc(Long taskId);

}
