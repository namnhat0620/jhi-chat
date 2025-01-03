package com.nam.chat.service;

import com.nam.chat.domain.Group;
import com.nam.chat.repository.GroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nam.chat.domain.Group}.
 */
@Service
@Transactional
public class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Save a group.
     *
     * @param group the entity to save.
     * @return the persisted entity.
     */
    public Group save(Group group) {
        LOG.debug("Request to save Group : {}", group);
        return groupRepository.save(group);
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
            .findById(group.getType())
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
    public Optional<Group> findOne(String id) {
        LOG.debug("Request to get Group : {}", id);
        return groupRepository.findById(id);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}
