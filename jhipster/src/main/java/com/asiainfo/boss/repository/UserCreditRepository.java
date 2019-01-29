package com.asiainfo.boss.repository;

import com.asiainfo.boss.domain.UserCredit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserCredit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCreditRepository extends JpaRepository<UserCredit, Long> {

}
