package com.neemshade.sniper.repository;

import com.neemshade.sniper.domain.SnFileBlob;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SnFileBlob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SnFileBlobRepository extends JpaRepository<SnFileBlob, Long> {

}
