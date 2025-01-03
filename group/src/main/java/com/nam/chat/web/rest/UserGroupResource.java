package com.nam.chat.web.rest;

import com.nam.chat.domain.UserGroup;
import com.nam.chat.repository.UserGroupRepository;
import com.nam.chat.service.UserGroupQueryService;
import com.nam.chat.service.UserGroupService;
import com.nam.chat.service.criteria.UserGroupCriteria;
import com.nam.chat.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nam.chat.domain.UserGroup}.
 */
@RestController
@RequestMapping("/api/user-groups")
public class UserGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupResource.class);

    private static final String ENTITY_NAME = "groupUserGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserGroupService userGroupService;

    private final UserGroupRepository userGroupRepository;

    private final UserGroupQueryService userGroupQueryService;

    public UserGroupResource(
        UserGroupService userGroupService,
        UserGroupRepository userGroupRepository,
        UserGroupQueryService userGroupQueryService
    ) {
        this.userGroupService = userGroupService;
        this.userGroupRepository = userGroupRepository;
        this.userGroupQueryService = userGroupQueryService;
    }

    /**
     * {@code POST  /user-groups} : Create a new userGroup.
     *
     * @param userGroup the userGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userGroup, or with status {@code 400 (Bad Request)} if the userGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserGroup> createUserGroup(@Valid @RequestBody UserGroup userGroup) throws URISyntaxException {
        LOG.debug("REST request to save UserGroup : {}", userGroup);
        if (userGroupRepository.existsById(userGroup.getUserId())) {
            throw new BadRequestAlertException("userGroup already exists", ENTITY_NAME, "idexists");
        }
        userGroup = userGroupService.save(userGroup);
        return ResponseEntity.created(new URI("/api/user-groups/" + userGroup.getUserId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userGroup.getUserId()))
            .body(userGroup);
    }

    /**
     * {@code PUT  /user-groups/:userId} : Updates an existing userGroup.
     *
     * @param userId the id of the userGroup to save.
     * @param userGroup the userGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGroup,
     * or with status {@code 400 (Bad Request)} if the userGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserGroup> updateUserGroup(
        @PathVariable(value = "userId", required = false) final String userId,
        @Valid @RequestBody UserGroup userGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserGroup : {}, {}", userId, userGroup);
        if (userGroup.getUserId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(userId, userGroup.getUserId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userGroupRepository.existsById(userId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userGroup = userGroupService.update(userGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userGroup.getUserId()))
            .body(userGroup);
    }

    /**
     * {@code PATCH  /user-groups/:userId} : Partial updates given fields of an existing userGroup, field will ignore if it is null
     *
     * @param userId the id of the userGroup to save.
     * @param userGroup the userGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGroup,
     * or with status {@code 400 (Bad Request)} if the userGroup is not valid,
     * or with status {@code 404 (Not Found)} if the userGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the userGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{userId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserGroup> partialUpdateUserGroup(
        @PathVariable(value = "userId", required = false) final String userId,
        @NotNull @RequestBody UserGroup userGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserGroup partially : {}, {}", userId, userGroup);
        if (userGroup.getUserId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(userId, userGroup.getUserId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userGroupRepository.existsById(userId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserGroup> result = userGroupService.partialUpdate(userGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userGroup.getUserId())
        );
    }

    /**
     * {@code GET  /user-groups} : get all the userGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserGroup>> getAllUserGroups(UserGroupCriteria criteria) {
        LOG.debug("REST request to get UserGroups by criteria: {}", criteria);

        List<UserGroup> entityList = userGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /user-groups/count} : count all the userGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserGroups(UserGroupCriteria criteria) {
        LOG.debug("REST request to count UserGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(userGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-groups/:id} : get the "id" userGroup.
     *
     * @param id the id of the userGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGroup> getUserGroup(@PathVariable("id") String id) {
        LOG.debug("REST request to get UserGroup : {}", id);
        Optional<UserGroup> userGroup = userGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userGroup);
    }

    /**
     * {@code DELETE  /user-groups/:id} : delete the "id" userGroup.
     *
     * @param id the id of the userGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable("id") String id) {
        LOG.debug("REST request to delete UserGroup : {}", id);
        userGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
