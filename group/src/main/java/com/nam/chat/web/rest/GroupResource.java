package com.nam.chat.web.rest;

import com.nam.chat.domain.Group;
import com.nam.chat.repository.GroupRepository;
import com.nam.chat.service.GroupQueryService;
import com.nam.chat.service.GroupService;
import com.nam.chat.service.criteria.GroupCriteria;
import com.nam.chat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nam.chat.domain.Group}.
 */
@RestController
@RequestMapping("/api/groups")
public class GroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "groupGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupService groupService;

    private final GroupRepository groupRepository;

    private final GroupQueryService groupQueryService;

    public GroupResource(GroupService groupService, GroupRepository groupRepository,
            GroupQueryService groupQueryService) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.groupQueryService = groupQueryService;
    }

    /**
     * {@code POST  /groups} : Create a new group.
     *
     * @param group the group to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new group, or with status {@code 400 (Bad Request)} if the
     *         group has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) throws URISyntaxException {
        LOG.debug("REST request to save Group : {}", group);
        group = groupService.save(group);
        return ResponseEntity.created(new URI("/api/groups/" + group.getType()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, group.getType()))
                .body(group);
    }

    /**
     * {@code PUT  /groups/:type} : Updates an existing group.
     *
     * @param type  the id of the group to save.
     * @param group the group to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated group,
     *         or with status {@code 400 (Bad Request)} if the group is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the group
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable(value = "id", required = false) final Long id,
            @RequestBody Group group)
            throws URISyntaxException {
        LOG.debug("REST request to update Group : {}, {}", id, group);
        if (group.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, group.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        group = groupService.update(group);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, group.getId().toString()))
                .body(group);
    }

    /**
     * {@code PATCH  /groups/:type} : Partial updates given fields of an existing
     * group, field will ignore if it is null
     *
     * @param type  the id of the group to save.
     * @param group the group to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated group,
     *         or with status {@code 400 (Bad Request)} if the group is not valid,
     *         or with status {@code 404 (Not Found)} if the group is not found,
     *         or with status {@code 500 (Internal Server Error)} if the group
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Group> partialUpdateGroup(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Group group) throws URISyntaxException {
        LOG.debug("REST request to partial update Group partially : {}, {}", id, group);
        if (group.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, group.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Group> result = groupService.partialUpdate(group);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, group.getId().toString()));
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of groups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Group>> getAllGroups(
            GroupCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get Groups by criteria: {}", criteria);

        Page<Group> page = groupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /groups/count} : count all the groups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGroups(GroupCriteria criteria) {
        LOG.debug("REST request to count Groups by criteria: {}", criteria);
        return ResponseEntity.ok().body(groupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /groups/:id} : get the "id" group.
     *
     * @param id the id of the group to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the group, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Group : {}", id);
        Optional<Group> group = groupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(group);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" group.
     *
     * @param id the id of the group to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Group : {}", id);
        groupService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
