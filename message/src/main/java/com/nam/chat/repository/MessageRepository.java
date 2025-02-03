package com.nam.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nam.chat.domain.Message;

public interface MessageRepository extends MongoRepository<Message, String> {

}
