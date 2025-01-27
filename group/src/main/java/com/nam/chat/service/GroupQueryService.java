package com.nam.chat.service;

import com.nam.chat.domain.*; // for static metamodels
import com.nam.chat.repository.GroupRepository;
import com.nam.chat.service.criteria.GroupCriteria;
import jakarta.persistence.criteria.JoinType;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Group} entities in the
 * database.
 * The main input is a {@link GroupCriteria} which gets converted to
 * {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Group} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GroupQueryService extends QueryService<Group> {

    private static final Logger LOG = LoggerFactory.getLogger(GroupQueryService.class);

    private final GroupRepository groupRepository;

    public GroupQueryService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Return a {@link Page} of {@link Group} which matches the criteria from the
     * database.
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Group> findByCriteria(GroupCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Group> specification = createSpecification(criteria);
        Page<Group> result = groupRepository.findAll(specification, page);
        result.getContent().stream().forEach(group -> {
            Set<UserGroup> publicUserGroups = group.getUserGroups().stream()
                    .map(userGroup -> new UserGroup().login(userGroup.getLogin())).collect(Collectors.toSet());
            group.setUserGroups(publicUserGroups);
        });
        return groupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GroupCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Group> specification = createSpecification(criteria);
        return groupRepository.count(specification);
    }

    /**
     * Function to convert {@link GroupCriteria} to a {@link Specification}
     * 
     * @param criteria The object which holds all the filters, which the entities
     *                 should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Group> createSpecification(GroupCriteria criteria) {
        Specification<Group> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Group_.type));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Group_.id));
            }
            if (criteria.getLastMessageId() != null) {
                specification = specification
                        .and(buildStringSpecification(criteria.getLastMessageId(), Group_.lastMessageId));
            }
            if (criteria.getAvatar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvatar(), Group_.avatar));
            }
            if (criteria.getUserGroupId() != null) {
                specification = specification.and(
                        buildSpecification(criteria.getUserGroupId(),
                                root -> root.join(Group_.userGroups, JoinType.LEFT).get(UserGroup_.id)));
            }
        }
        return specification;
    }
}
