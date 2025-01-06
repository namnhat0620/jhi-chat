package com.nam.chat.web.rest;

import static com.nam.chat.domain.GroupAsserts.*;
import static com.nam.chat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nam.chat.IntegrationTest;
import com.nam.chat.domain.Group;
import com.nam.chat.repository.GroupRepository;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupResourceIT {

    private static final Long DEFAULT_ID = 1L;
    private static final Long UPDATED_ID = 2L;

    private static final String DEFAULT_LAST_MESSAGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MESSAGE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{type}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMockMvc;

    private Group group;

    private Group insertedGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity() {
        return new Group().type(UUID.randomUUID().toString()).id(DEFAULT_ID).lastMessageId(DEFAULT_LAST_MESSAGE_ID).avatar(DEFAULT_AVATAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createUpdatedEntity() {
        return new Group().type(UUID.randomUUID().toString()).id(UPDATED_ID).lastMessageId(UPDATED_LAST_MESSAGE_ID).avatar(UPDATED_AVATAR);
    }

    @BeforeEach
    public void initTest() {
        group = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedGroup != null) {
            groupRepository.delete(insertedGroup);
            insertedGroup = null;
        }
    }

    @Test
    @Transactional
    void createGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Group
        var returnedGroup = om.readValue(
            restGroupMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(group)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Group.class
        );

        // Validate the Group in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGroupUpdatableFieldsEquals(returnedGroup, getPersistedGroup(returnedGroup));

        insertedGroup = returnedGroup;
    }

    @Test
    @Transactional
    void createGroupWithExistingId() throws Exception {
        // Create the Group with an existing ID
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(group)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGroups() throws Exception {
        // Initialize the database
        group.setType(UUID.randomUUID().toString());
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=type,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].type").value(hasItem(group.getType())))
            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
            .andExpect(jsonPath("$.[*].lastMessageId").value(hasItem(DEFAULT_LAST_MESSAGE_ID)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)));
    }

    @Test
    @Transactional
    void getGroup() throws Exception {
        // Initialize the database
        group.setType(UUID.randomUUID().toString());
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, group.getType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.type").value(group.getType()))
            .andExpect(jsonPath("$.id").value(DEFAULT_ID))
            .andExpect(jsonPath("$.lastMessageId").value(DEFAULT_LAST_MESSAGE_ID))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR));
    }

    @Test
    @Transactional
    void getGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        String id = group.getType();

        defaultGroupFiltering("type.equals=" + id, "type.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllGroupsByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where id equals to
        defaultGroupFiltering("id.equals=" + DEFAULT_ID, "id.equals=" + UPDATED_ID);
    }

    @Test
    @Transactional
    void getAllGroupsByIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where id in
        defaultGroupFiltering("id.in=" + DEFAULT_ID + "," + UPDATED_ID, "id.in=" + UPDATED_ID);
    }

    @Test
    @Transactional
    void getAllGroupsByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where id is not null
        defaultGroupFiltering("id.specified=true", "id.specified=false");
    }

    @Test
    @Transactional
    void getAllGroupsByIdContainsSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where id contains
        defaultGroupFiltering("id.contains=" + DEFAULT_ID, "id.contains=" + UPDATED_ID);
    }

    @Test
    @Transactional
    void getAllGroupsByIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where id does not contain
        defaultGroupFiltering("id.doesNotContain=" + UPDATED_ID, "id.doesNotContain=" + DEFAULT_ID);
    }

    @Test
    @Transactional
    void getAllGroupsByLastMessageIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where lastMessageId equals to
        defaultGroupFiltering("lastMessageId.equals=" + DEFAULT_LAST_MESSAGE_ID, "lastMessageId.equals=" + UPDATED_LAST_MESSAGE_ID);
    }

    @Test
    @Transactional
    void getAllGroupsByLastMessageIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where lastMessageId in
        defaultGroupFiltering(
            "lastMessageId.in=" + DEFAULT_LAST_MESSAGE_ID + "," + UPDATED_LAST_MESSAGE_ID,
            "lastMessageId.in=" + UPDATED_LAST_MESSAGE_ID
        );
    }

    @Test
    @Transactional
    void getAllGroupsByLastMessageIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where lastMessageId is not null
        defaultGroupFiltering("lastMessageId.specified=true", "lastMessageId.specified=false");
    }

    @Test
    @Transactional
    void getAllGroupsByLastMessageIdContainsSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where lastMessageId contains
        defaultGroupFiltering("lastMessageId.contains=" + DEFAULT_LAST_MESSAGE_ID, "lastMessageId.contains=" + UPDATED_LAST_MESSAGE_ID);
    }

    @Test
    @Transactional
    void getAllGroupsByLastMessageIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where lastMessageId does not contain
        defaultGroupFiltering(
            "lastMessageId.doesNotContain=" + UPDATED_LAST_MESSAGE_ID,
            "lastMessageId.doesNotContain=" + DEFAULT_LAST_MESSAGE_ID
        );
    }

    @Test
    @Transactional
    void getAllGroupsByAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where avatar equals to
        defaultGroupFiltering("avatar.equals=" + DEFAULT_AVATAR, "avatar.equals=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    void getAllGroupsByAvatarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where avatar in
        defaultGroupFiltering("avatar.in=" + DEFAULT_AVATAR + "," + UPDATED_AVATAR, "avatar.in=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    void getAllGroupsByAvatarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where avatar is not null
        defaultGroupFiltering("avatar.specified=true", "avatar.specified=false");
    }

    @Test
    @Transactional
    void getAllGroupsByAvatarContainsSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where avatar contains
        defaultGroupFiltering("avatar.contains=" + DEFAULT_AVATAR, "avatar.contains=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    void getAllGroupsByAvatarNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGroup = groupRepository.saveAndFlush(group);

        // Get all the groupList where avatar does not contain
        defaultGroupFiltering("avatar.doesNotContain=" + UPDATED_AVATAR, "avatar.doesNotContain=" + DEFAULT_AVATAR);
    }

    private void defaultGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGroupShouldBeFound(shouldBeFound);
        defaultGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGroupShouldBeFound(String filter) throws Exception {
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=type,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].type").value(hasItem(group.getType())))
            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
            .andExpect(jsonPath("$.[*].lastMessageId").value(hasItem(DEFAULT_LAST_MESSAGE_ID)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)));

        // Check, that the count call also returns 1
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=type,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGroupShouldNotBeFound(String filter) throws Exception {
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=type,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=type,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGroup() throws Exception {
        // Initialize the database
        group.setType(UUID.randomUUID().toString());
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup.id(UPDATED_ID).lastMessageId(UPDATED_LAST_MESSAGE_ID).avatar(UPDATED_AVATAR);

        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGroup.getType())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGroupToMatchAllProperties(updatedGroup);
    }

    @Test
    @Transactional
    void putNonExistingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setType(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, group.getType())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setType(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setType(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(group)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        group.setType(UUID.randomUUID().toString());
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setType(group.getType());

        partialUpdatedGroup.id(UPDATED_ID).avatar(UPDATED_AVATAR);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getType())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGroup, group), getPersistedGroup(group));
    }

    @Test
    @Transactional
    void fullUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        group.setType(UUID.randomUUID().toString());
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setType(group.getType());

        partialUpdatedGroup.id(UPDATED_ID).lastMessageId(UPDATED_LAST_MESSAGE_ID).avatar(UPDATED_AVATAR);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getType())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGroupUpdatableFieldsEquals(partialUpdatedGroup, getPersistedGroup(partialUpdatedGroup));
    }

    @Test
    @Transactional
    void patchNonExistingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setType(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, group.getType())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setType(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        group.setType(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(group)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroup() throws Exception {
        // Initialize the database
        group.setType(UUID.randomUUID().toString());
        insertedGroup = groupRepository.saveAndFlush(group);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the group
        restGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, group.getType()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return groupRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Group getPersistedGroup(Group group) {
        return groupRepository.findById(group.getId()).orElseThrow();
    }

    protected void assertPersistedGroupToMatchAllProperties(Group expectedGroup) {
        assertGroupAllPropertiesEquals(expectedGroup, getPersistedGroup(expectedGroup));
    }

    protected void assertPersistedGroupToMatchUpdatableProperties(Group expectedGroup) {
        assertGroupAllUpdatablePropertiesEquals(expectedGroup, getPersistedGroup(expectedGroup));
    }
}
