package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.Event_user;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Event_user entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Event_userRepository extends JpaRepository<Event_user, Long> {

}
