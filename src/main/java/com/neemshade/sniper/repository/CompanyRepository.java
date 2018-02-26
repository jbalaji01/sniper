package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.Company;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	List<Company> findAllByOrderByCompanyName();
}
