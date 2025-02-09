package com.nam.chat;

import com.nam.chat.config.AsyncSyncConfiguration;
import com.nam.chat.config.EmbeddedMongo;
import com.nam.chat.config.JacksonConfiguration;
import com.nam.chat.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MessageApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedMongo
public @interface IntegrationTest {
}
