package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.HotListUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HotListUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotListUserRepository extends JpaRepository<HotListUser, Long> {

}
