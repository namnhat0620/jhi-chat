package com.nam.chat.web.rest;

import static com.nam.chat.domain.UserGroupAsserts.*;
import static com.nam.chat.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nam.chat.IntegrationTest;
import com.nam.chat.domain.Group;
import com.nam.chat.domain.UserGroup;
import com.nam.chat.repository.UserGroupRepository;
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
 * Integration tests for the {@link UserGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserGroupResourceIT {

    private static final String DEFAULT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SEEN = false;
    private static final Boolean UPDATED_IS_SEEN = true;

    private static final Boolean DEFAULT_IS_TURN_ON_NOTI = false;
    private static final Boolean UPDATED_IS_TURN_ON_NOTI = true;

    private static final String ENTITY_API_URL = "/api/user-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{userId}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserGroupMockMvc;

    private UserGroup userGroup;

    private UserGroup insertedUserGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroup createEntity() {
        return new UserGroup()
            .userId(UUID.randomUUID().toString())
            .id(DEFAULT_ID)
            .isSeen(DEFAULT_IS_SEEN)
            .isTurnOnNoti(DEFAULT_IS_TURN_ON_NOTI);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroup createUpdatedEntity() {
        return new UserGroup()
            .userId(UUID.randomUUID().toString())
            .id(UPDATED_ID)
            .isSeen(UPDATED_IS_SEEN)
            .isTurnOnNoti(UPDATED_IS_TURN_ON_NOTI);
    }

    @BeforeEach
    public void initTest() {
        userGroup = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserGroup != null) {
            userGroupRepository.delete(insertedUserGroup);
            insertedUserGroup = null;
        }
    }

    @Test
    @Transactional
    void createUserGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserGroup
        var returnedUserGroup = om.readValue(
            restUserGroupMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userGroup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserGroup.class
        );

        // Validate the UserGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserGroupUpdatableFieldsEquals(returnedUserGroup, getPersistedUserGroup(returnedUserGroup));

        insertedUserGroup = returnedUserGroup;
    }

    @Test
    @Transactional
    void createUserGroupWithExistingId() throws Exception {
        // Create the UserGroup with an existing ID
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserGroupMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userGroup)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserGroups() throws Exception {
        // Initialize the database
        userGroup.setUserId(UUID.randomUUID().toString());
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList
        restUserGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=userId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(userGroup.getUserId())))
            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
            .andExpect(jsonPath("$.[*].isSeen").value(hasItem(DEFAULT_IS_SEEN.booleanValue())))
            .andExpect(jsonPath("$.[*].isTurnOnNoti").value(hasItem(DEFAULT_IS_TURN_ON_NOTI.booleanValue())));
    }

    @Test
    @Transactional
    void getUserGroup() throws Exception {
        // Initialize the database
        userGroup.setUserId(UUID.randomUUID().toString());
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get the userGroup
        restUserGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, userGroup.getUserId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.userId").value(userGroup.getUserId()))
            .andExpect(jsonPath("$.id").value(DEFAULT_ID))
            .andExpect(jsonPath("$.isSeen").value(DEFAULT_IS_SEEN.booleanValue()))
            .andExpect(jsonPath("$.isTurnOnNoti").value(DEFAULT_IS_TURN_ON_NOTI.booleanValue()));
    }

    @Test
    @Transactional
    void getUserGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        String id = userGroup.getUserId();

        defaultUserGroupFiltering("userId.equals=" + id, "userId.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where id equals to
        defaultUserGroupFiltering("id.equals=" + DEFAULT_ID, "id.equals=" + UPDATED_ID);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where id in
        defaultUserGroupFiltering("id.in=" + DEFAULT_ID + "," + UPDATED_ID, "id.in=" + UPDATED_ID);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where id is not null
        defaultUserGroupFiltering("id.specified=true", "id.specified=false");
    }

    @Test
    @Transactional
    void getAllUserGroupsByIdContainsSomething() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where id contains
        defaultUserGroupFiltering("id.contains=" + DEFAULT_ID, "id.contains=" + UPDATED_ID);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where id does not contain
        defaultUserGroupFiltering("id.doesNotContain=" + UPDATED_ID, "id.doesNotContain=" + DEFAULT_ID);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIsSeenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isSeen equals to
        defaultUserGroupFiltering("isSeen.equals=" + DEFAULT_IS_SEEN, "isSeen.equals=" + UPDATED_IS_SEEN);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIsSeenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isSeen in
        defaultUserGroupFiltering("isSeen.in=" + DEFAULT_IS_SEEN + "," + UPDATED_IS_SEEN, "isSeen.in=" + UPDATED_IS_SEEN);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIsSeenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isSeen is not null
        defaultUserGroupFiltering("isSeen.specified=true", "isSeen.specified=false");
    }

    @Test
    @Transactional
    void getAllUserGroupsByIsTurnOnNotiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isTurnOnNoti equals to
        defaultUserGroupFiltering("isTurnOnNoti.equals=" + DEFAULT_IS_TURN_ON_NOTI, "isTurnOnNoti.equals=" + UPDATED_IS_TURN_ON_NOTI);
    }

    @Test
    @Transactional
    void getAllUserGroupsByIsTurnOnNotiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isTurnOnNoti in
        defaultUserGroupFiltering(
            "isTurnOnNoti.in=" + DEFAULT_IS_TURN_ON_NOTI + "," + UPDATED_IS_TURN_ON_NOTI,
            "isTurnOnNoti.in=" + UPDATED_IS_TURN_ON_NOTI
        );
    }

    @Test
    @Transactional
    void getAllUserGroupsByIsTurnOnNotiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroupList where isTurnOnNoti is not null
        defaultUserGroupFiltering("isTurnOnNoti.specified=true", "isTurnOnNoti.specified=false");
    }

    @Test
    @Transactional
    void getAllUserGroupsByGroupIsEqualToSomething() throws Exception {
        Group group;
        if (TestUtil.findAll(em, Group.class).isEmpty()) {
            userGroupRepository.saveAndFlush(userGroup);
            group = GroupResourceIT.createEntity();
        } else {
            group = TestUtil.findAll(em, Group.class).get(0);
        }
        em.persist(group);
        em.flush();
        userGroup.setGroup(group);
        userGroupRepository.saveAndFlush(userGroup);
        String groupId = group.getType();
        // Get all the userGroupList where group equals to groupId
        defaultUserGroupShouldBeFound("groupId.equals=" + groupId);

        // Get all the userGroupList where group equals to "invalid-id"
        defaultUserGroupShouldNotBeFound("groupId.equals=" + "invalid-id");
    }

    private void defaultUserGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserGroupShouldBeFound(shouldBeFound);
        defaultUserGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserGroupShouldBeFound(String filter) throws Exception {
        restUserGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=userId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(userGroup.getUserId())))
            .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_ID)))
            .andExpect(jsonPath("$.[*].isSeen").value(hasItem(DEFAULT_IS_SEEN.booleanValue())))
            .andExpect(jsonPath("$.[*].isTurnOnNoti").value(hasItem(DEFAULT_IS_TURN_ON_NOTI.booleanValue())));

        // Check, that the count call also returns 1
        restUserGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=userId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserGroupShouldNotBeFound(String filter) throws Exception {
        restUserGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=userId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=userId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserGroup() throws Exception {
        // Get the userGroup
        restUserGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserGroup() throws Exception {
        // Initialize the database
        userGroup.setUserId(UUID.randomUUID().toString());
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userGroup
        UserGroup updatedUserGroup = userGroupRepository.findById(userGroup.getUserId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserGroup are not directly saved in db
        em.detach(updatedUserGroup);
        updatedUserGroup.id(UPDATED_ID).isSeen(UPDATED_IS_SEEN).isTurnOnNoti(UPDATED_IS_TURN_ON_NOTI);

        restUserGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserGroup.getUserId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserGroup))
            )
            .andExpect(status().isOk());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserGroupToMatchAllProperties(updatedUserGroup);
    }

    @Test
    @Transactional
    void putNonExistingUserGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userGroup.setUserId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userGroup.getUserId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userGroup.setUserId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userGroup.setUserId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGroupMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserGroupWithPatch() throws Exception {
        // Initialize the database
        userGroup.setUserId(UUID.randomUUID().toString());
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userGroup using partial update
        UserGroup partialUpdatedUserGroup = new UserGroup();
        partialUpdatedUserGroup.setUserId(userGroup.getUserId());

        partialUpdatedUserGroup.isSeen(UPDATED_IS_SEEN);

        restUserGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserGroup.getUserId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserGroup))
            )
            .andExpect(status().isOk());

        // Validate the UserGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserGroup, userGroup),
            getPersistedUserGroup(userGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserGroupWithPatch() throws Exception {
        // Initialize the database
        userGroup.setUserId(UUID.randomUUID().toString());
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userGroup using partial update
        UserGroup partialUpdatedUserGroup = new UserGroup();
        partialUpdatedUserGroup.setUserId(userGroup.getUserId());

        partialUpdatedUserGroup.id(UPDATED_ID).isSeen(UPDATED_IS_SEEN).isTurnOnNoti(UPDATED_IS_TURN_ON_NOTI);

        restUserGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserGroup.getUserId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserGroup))
            )
            .andExpect(status().isOk());

        // Validate the UserGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserGroupUpdatableFieldsEquals(partialUpdatedUserGroup, getPersistedUserGroup(partialUpdatedUserGroup));
    }

    @Test
    @Transactional
    void patchNonExistingUserGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userGroup.setUserId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userGroup.getUserId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userGroup.setUserId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userGroup.setUserId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserGroup() throws Exception {
        // Initialize the database
        userGroup.setUserId(UUID.randomUUID().toString());
        insertedUserGroup = userGroupRepository.saveAndFlush(userGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userGroup
        restUserGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, userGroup.getUserId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userGroupRepository.count();
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

    protected UserGroup getPersistedUserGroup(UserGroup userGroup) {
        return userGroupRepository.findById(userGroup.getUserId()).orElseThrow();
    }

    protected void assertPersistedUserGroupToMatchAllProperties(UserGroup expectedUserGroup) {
        assertUserGroupAllPropertiesEquals(expectedUserGroup, getPersistedUserGroup(expectedUserGroup));
    }

    protected void assertPersistedUserGroupToMatchUpdatableProperties(UserGroup expectedUserGroup) {
        assertUserGroupAllUpdatablePropertiesEquals(expectedUserGroup, getPersistedUserGroup(expectedUserGroup));
    }
}
