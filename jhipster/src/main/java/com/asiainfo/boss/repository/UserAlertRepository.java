package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.UserAlert;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserAlert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {

}
