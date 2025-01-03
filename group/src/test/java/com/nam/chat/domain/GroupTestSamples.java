package com.nam.chat.domain;

import java.util.UUID;

public class GroupTestSamples {

    public static Group getGroupSample1() {
        return new Group().id("id1").type("type1").lastMessageId("lastMessageId1").avatar("avatar1");
    }

    public static Group getGroupSample2() {
        return new Group().id("id2").type("type2").lastMessageId("lastMessageId2").avatar("avatar2");
    }

    public static Group getGroupRandomSampleGenerator() {
        return new Group()
            .id(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .lastMessageId(UUID.randomUUID().toString())
            .avatar(UUID.randomUUID().toString());
    }
}
