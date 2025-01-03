package com.nam.chat.domain;

import static com.nam.chat.domain.GroupTestSamples.*;
import static com.nam.chat.domain.UserGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nam.chat.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Group.class);
        Group group1 = getGroupSample1();
        Group group2 = new Group();
        assertThat(group1).isNotEqualTo(group2);

        group2.setType(group1.getType());
        assertThat(group1).isEqualTo(group2);

        group2 = getGroupSample2();
        assertThat(group1).isNotEqualTo(group2);
    }

    @Test
    void userGroupTest() {
        Group group = getGroupRandomSampleGenerator();
        UserGroup userGroupBack = getUserGroupRandomSampleGenerator();

        group.addUserGroup(userGroupBack);
        assertThat(group.getUserGroups()).containsOnly(userGroupBack);
        assertThat(userGroupBack.getGroup()).isEqualTo(group);

        group.removeUserGroup(userGroupBack);
        assertThat(group.getUserGroups()).doesNotContain(userGroupBack);
        assertThat(userGroupBack.getGroup()).isNull();

        group.userGroups(new HashSet<>(Set.of(userGroupBack)));
        assertThat(group.getUserGroups()).containsOnly(userGroupBack);
        assertThat(userGroupBack.getGroup()).isEqualTo(group);

        group.setUserGroups(new HashSet<>());
        assertThat(group.getUserGroups()).doesNotContain(userGroupBack);
        assertThat(userGroupBack.getGroup()).isNull();
    }
}
