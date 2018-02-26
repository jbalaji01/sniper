package com.neemshade.sniper.repository;


import com.neemshade.sniper.domain.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	Optional<UserInfo> findOneByUserId(Long userId);
	
	List<UserInfo> findAllByOrderByEmpCode();
}
