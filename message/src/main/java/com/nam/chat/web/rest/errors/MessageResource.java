package com.nam.chat.web.rest.errors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nam.chat.domain.Message;
import com.nam.chat.service.MessageService;

import lombok.RequiredArgsConstructor;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageResource {
    private static final Logger LOG = LoggerFactory.getLogger(MessageResource.class);
    private static final String ENTITY_NAME = "message";
    private final MessageService messageService;

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of groups in body.
     */
    // @GetMapping("")
    // public ResponseEntity<List<Message>> getAllGroups(
    // MessageCri criteria,
    // @org.springdoc.core.annotations.ParameterObject Pageable pageable
    // ) {
    // LOG.debug("REST request to get Groups by criteria: {}", criteria);

    // Page<Group> page = groupQueryService.findByCriteria(criteria, pageable);
    // HttpHeaders headers = PaginationUtil
    // .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
    // page);
    // return ResponseEntity.ok().headers(headers).body(page.getContent());
    // }

    @PostMapping("")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        LOG.debug("REST request to save Message : {}", message);
        Message result = messageService.save(message);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri()).body(result);
    }

    @GetMapping("")
    public ResponseEntity<List<Message>> getAllMessages(@RequestParam Long groupId, Pageable pageable) {
        LOG.debug("REST request to get message with criteria: {}", groupId);
        Page<Message> page = messageService.findAll(groupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
