package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.UserFavoriteJob;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserFavoriteJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFavoriteJobRepository extends JpaRepository<UserFavoriteJob, Long> {

}
