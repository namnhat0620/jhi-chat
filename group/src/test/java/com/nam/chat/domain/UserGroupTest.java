package com.nam.chat.domain;

import static com.nam.chat.domain.GroupTestSamples.*;
import static com.nam.chat.domain.UserGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nam.chat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserGroup.class);
        UserGroup userGroup1 = getUserGroupSample1();
        UserGroup userGroup2 = new UserGroup();
        assertThat(userGroup1).isNotEqualTo(userGroup2);

        userGroup2.setUserId(userGroup1.getUserId());
        assertThat(userGroup1).isEqualTo(userGroup2);

        userGroup2 = getUserGroupSample2();
        assertThat(userGroup1).isNotEqualTo(userGroup2);
    }

    @Test
    void groupTest() {
        UserGroup userGroup = getUserGroupRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        userGroup.setGroup(groupBack);
        assertThat(userGroup.getGroup()).isEqualTo(groupBack);

        userGroup.group(null);
        assertThat(userGroup.getGroup()).isNull();
    }
}
