package com.nam.chat.domain;

import java.util.UUID;

public class UserGroupTestSamples {

    public static UserGroup getUserGroupSample1() {
        return new UserGroup().id("id1").userId("userId1");
    }

    public static UserGroup getUserGroupSample2() {
        return new UserGroup().id("id2").userId("userId2");
    }

    public static UserGroup getUserGroupRandomSampleGenerator() {
        return new UserGroup().id(UUID.randomUUID().toString()).userId(UUID.randomUUID().toString());
    }
}
