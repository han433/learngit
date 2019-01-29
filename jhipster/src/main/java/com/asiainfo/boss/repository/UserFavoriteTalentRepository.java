package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.UserFavoriteTalent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserFavoriteTalent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFavoriteTalentRepository extends JpaRepository<UserFavoriteTalent, Long> {

}
