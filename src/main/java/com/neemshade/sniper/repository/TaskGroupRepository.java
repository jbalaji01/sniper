package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.TaskGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TaskGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {

}
