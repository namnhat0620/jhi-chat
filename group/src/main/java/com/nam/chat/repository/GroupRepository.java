package com.nam.chat.repository;

import com.nam.chat.domain.Group;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    @Query(value = "SELECT jg.id, jg.type, jg.avatar, jg.last_message_id, " +
            "COALESCE(jg.name, :login1) AS name " +
            "FROM jhi_group jg " +
            "JOIN jhi_user_group jug ON jg.id = jug.group_id " +
            "WHERE jg.type = 'PRIVATE' " +
            "GROUP BY jg.id, jg.type, jg.last_message_id, jg.\"type\" " +
            "HAVING ARRAY_AGG(login) @> ARRAY[:login1, :login2]::VARCHAR[] " + // Match the logins using the set
            "AND ARRAY[:login1, :login2]::VARCHAR[] @> ARRAY_AGG(login) " +
            "LIMIT 1", nativeQuery = true)
    Optional<Group> findGroupsWithSpecificLogins(@Param("login1") String login1, @Param("login2") String login2);
}
