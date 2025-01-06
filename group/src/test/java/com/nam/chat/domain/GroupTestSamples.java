package com.nam.chat.domain;

import java.util.UUID;

public class GroupTestSamples {

    public static Group getGroupSample1() {
        return new Group().id(1L).type("type1").lastMessageId("lastMessageId1").avatar("avatar1");
    }

    public static Group getGroupSample2() {
        return new Group().id(2L).type("type2").lastMessageId("lastMessageId2").avatar("avatar2");
    }

    public static Group getGroupRandomSampleGenerator() {
        return new Group()
            .id(Math.abs(UUID.randomUUID().getMostSignificantBits()))
            .type(UUID.randomUUID().toString())
            .lastMessageId(UUID.randomUUID().toString())
            .avatar(UUID.randomUUID().toString());
    }
}
