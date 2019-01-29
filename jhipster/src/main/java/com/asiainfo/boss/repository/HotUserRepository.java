package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.HotUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HotUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotUserRepository extends JpaRepository<HotUser, Long> {

}
