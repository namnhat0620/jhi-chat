package com.nam.chat.service;

import com.nam.chat.domain.UserGroup;
import com.nam.chat.repository.UserGroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nam.chat.domain.UserGroup}.
 */
@Service
@Transactional
public class UserGroupService {

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupService.class);

    private final UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * Save a userGroup.
     *
     * @param userGroup the entity to save.
     * @return the persisted entity.
     */
    public UserGroup save(UserGroup userGroup) {
        LOG.debug("Request to save UserGroup : {}", userGroup);
        return userGroupRepository.save(userGroup);
    }

    /**
     * Update a userGroup.
     *
     * @param userGroup the entity to save.
     * @return the persisted entity.
     */
    public UserGroup update(UserGroup userGroup) {
        LOG.debug("Request to update UserGroup : {}", userGroup);
        userGroup.setIsPersisted();
        return userGroupRepository.save(userGroup);
    }

    /**
     * Partially update a userGroup.
     *
     * @param userGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserGroup> partialUpdate(UserGroup userGroup) {
        LOG.debug("Request to partially update UserGroup : {}", userGroup);

        return userGroupRepository
            .findById(userGroup.getId())
            .map(existingUserGroup -> {
                if (userGroup.getId() != null) {
                    existingUserGroup.setId(userGroup.getId());
                }
                if (userGroup.getIsSeen() != null) {
                    existingUserGroup.setIsSeen(userGroup.getIsSeen());
                }
                if (userGroup.getIsTurnOnNoti() != null) {
                    existingUserGroup.setIsTurnOnNoti(userGroup.getIsTurnOnNoti());
                }

                return existingUserGroup;
            })
            .map(userGroupRepository::save);
    }

    /**
     * Get one userGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserGroup> findOne(Long id) {
        LOG.debug("Request to get UserGroup : {}", id);
        return userGroupRepository.findById(id);
    }

    /**
     * Delete the userGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserGroup : {}", id);
        userGroupRepository.deleteById(id);
    }
}
