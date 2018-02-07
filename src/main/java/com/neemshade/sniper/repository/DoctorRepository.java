package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.Doctor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("select distinct doctor from Doctor doctor left join fetch doctor.hospitals")
    List<Doctor> findAllWithEagerRelationships();

    @Query("select doctor from Doctor doctor left join fetch doctor.hospitals where doctor.id =:id")
    Doctor findOneWithEagerRelationships(@Param("id") Long id);

}
