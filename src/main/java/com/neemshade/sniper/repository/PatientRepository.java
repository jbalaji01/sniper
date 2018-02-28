package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	List<Patient> findAllByOrderByPatientName();

}
