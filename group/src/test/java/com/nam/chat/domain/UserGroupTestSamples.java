package com.nam.chat.domain;

import java.util.UUID;

public class UserGroupTestSamples {

    public static UserGroup getUserGroupSample1() {
        return new UserGroup().id(1L);
    }

    public static UserGroup getUserGroupSample2() {
        return new UserGroup().id(2L);
    }

    public static UserGroup getUserGroupRandomSampleGenerator() {
        return new UserGroup().id(Math.abs(UUID.randomUUID().getMostSignificantBits()));
    }
}
