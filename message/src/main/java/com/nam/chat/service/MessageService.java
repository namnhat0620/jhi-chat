package com.nam.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nam.chat.domain.Message;
import com.nam.chat.repository.MessageRepository;
import com.nam.chat.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private static final String ENTITY_NAME = "MessageService";
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;

    public Message save(Message message) {
        LOG.debug("Request to save Message : {}", message);
        if (!this.checkIfUserInGroup(message.getSentToLogin(), message.getGroupId())) {
            throw new IllegalArgumentException("User is not in group");
        }
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        message.setSentFromLogin(SecurityUtils.getCurrentUserLogin().orElse(""));

        message = messageRepository.save(message);
        notificationService.sendNotification(message.getSentToLogin(), message);
        return message;
    }

    public Page<Message> findAll(Long groupId, Pageable pageable) {
        LOG.debug("Request to get all Messages");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("timestamp").descending());

        if (groupId != null) {
            return messageRepository.findByGroupId(groupId, sortedPageable);
        }
        return messageRepository.findAll(sortedPageable);
    }

    private boolean checkIfUserInGroup(String login, Long groupId) {
        // TODO: implement groupFeignClient and check
        // return messageRepository.checkIfUserInGroup(login, groupId);

        return true;
    }
}
