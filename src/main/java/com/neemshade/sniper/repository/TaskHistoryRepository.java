package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.TaskHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TaskHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

}
