package com.nam.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.nam.chat.domain.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{ 'groupId': ?0 }")
    Page<Message> findByGroupId(Long groupId, Pageable pageable);
}
