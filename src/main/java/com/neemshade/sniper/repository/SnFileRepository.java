package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.domain.TaskHistory;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the SnFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SnFileRepository extends JpaRepository<SnFile, Long> {
    @Query("select distinct sn_file from SnFile sn_file left join fetch sn_file.patients left join fetch sn_file.tasks")
    List<SnFile> findAllWithEagerRelationships();

    @Query("select sn_file from SnFile sn_file left join fetch sn_file.patients left join fetch sn_file.tasks where sn_file.id =:id")
    SnFile findOneWithEagerRelationships(@Param("id") Long id);

    Optional<SnFile> findFirstByTasksIdOrderByPeckOrderDesc(Long taskId);
    
    List<SnFile> findByTasksIdOrderByIsInputDescPeckOrderAsc(Long taskId);
}
