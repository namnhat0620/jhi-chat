package com.nam.chat.service;

import com.nam.chat.domain.*; // for static metamodels
import com.nam.chat.domain.UserGroup;
import com.nam.chat.repository.UserGroupRepository;
import com.nam.chat.service.criteria.UserGroupCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserGroup} entities in the database.
 * The main input is a {@link UserGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserGroupQueryService extends QueryService<UserGroup> {

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupQueryService.class);

    private final UserGroupRepository userGroupRepository;

    public UserGroupQueryService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * Return a {@link List} of {@link UserGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserGroup> findByCriteria(UserGroupCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<UserGroup> specification = createSpecification(criteria);
        return userGroupRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserGroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserGroup> specification = createSpecification(criteria);
        return userGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link UserGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserGroup> createSpecification(UserGroupCriteria criteria) {
        Specification<UserGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), UserGroup_.login));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserGroup_.id));
            }
            if (criteria.getIsSeen() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSeen(), UserGroup_.isSeen));
            }
            if (criteria.getIsTurnOnNoti() != null) {
                specification = specification.and(buildSpecification(criteria.getIsTurnOnNoti(), UserGroup_.isTurnOnNoti));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGroupId(), root -> root.join(UserGroup_.group, JoinType.LEFT).get(Group_.type))
                );
            }
        }
        return specification;
    }
}
