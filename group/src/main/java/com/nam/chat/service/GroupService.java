package com.nam.chat.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nam.chat.domain.Group;
import com.nam.chat.domain.UserGroup;
import com.nam.chat.repository.GroupRepository;
import com.nam.chat.repository.UserGroupRepository;
import com.nam.chat.security.SecurityUtils;

/**
 * Service Implementation for managing {@link com.nam.chat.domain.Group}.
 */
@Service
@Transactional
public class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    public GroupService(GroupRepository groupRepository, UserGroupRepository userGroupRepository) {
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * Save a group.
     *
     * @param group the entity to save.
     * @return the persisted entity.
     */
    public Group save(Group group) {
        LOG.debug("Request to save Group : {}", group);
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElse(StringUtils.EMPTY);
        String newFriendLogin = group.getUserGroups().stream().findFirst().map(UserGroup::getLogin)
                .orElse(StringUtils.EMPTY);

        // Check if existed, do not create new
        Optional<Group> exitedGroupOptional = groupRepository.findGroupsWithSpecificLogins(
                newFriendLogin,
                currentUserLogin);
        if (exitedGroupOptional.isPresent()) {
            return exitedGroupOptional.get();
        }

        // Add current user login to group
        group.getUserGroups().add(new UserGroup().login(currentUserLogin));

        // Create new group
        Group savedGroup = groupRepository.save(group);
        Optional.ofNullable(group.getUserGroups()).orElse(Collections.emptySet()).stream().forEach(userGroup -> {
            userGroup.setGroup(savedGroup);
            userGroupRepository.save(userGroup);
        });

        // Update temporary group name
        savedGroup.setName(newFriendLogin);
        return savedGroup;
    }

    /**
     * Update a group.
     *
     * @param group the entity to save.
     * @return the persisted entity.
     */
    public Group update(Group group) {
        LOG.debug("Request to update Group : {}", group);
        group.setIsPersisted();
        return groupRepository.save(group);
    }

    /**
     * Partially update a group.
     *
     * @param group the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Group> partialUpdate(Group group) {
        LOG.debug("Request to partially update Group : {}", group);

        return groupRepository
                .findById(group.getId())
                .map(existingGroup -> {
                    if (group.getId() != null) {
                        existingGroup.setId(group.getId());
                    }
                    if (group.getLastMessageId() != null) {
                        existingGroup.setLastMessageId(group.getLastMessageId());
                    }
                    if (group.getAvatar() != null) {
                        existingGroup.setAvatar(group.getAvatar());
                    }

                    return existingGroup;
                })
                .map(groupRepository::save);
    }

    /**
     * Get one group by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Group> findOne(Long id) {
        LOG.debug("Request to get Group : {}", id);
        return groupRepository.findById(id);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }

    private Set<UserGroup> addCurrentUserToGroup(Group group) {
        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        return group.getUserGroups();
    }
}
