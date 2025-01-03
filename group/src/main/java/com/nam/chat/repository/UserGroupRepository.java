package com.nam.chat.repository;

import com.nam.chat.domain.UserGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, String>, JpaSpecificationExecutor<UserGroup> {}
