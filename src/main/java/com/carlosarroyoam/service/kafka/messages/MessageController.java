package com.carlosarroyoam.service.kafka.messages;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/messages")
public class MessageController {
  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<Void> create(@RequestBody Message message, UriComponentsBuilder builder) {
    messageService.send(message);
    UriComponents uriComponents = builder.path("/messages/{messageId}")
        .buildAndExpand(message.getId());
    return ResponseEntity.created(uriComponents.toUri()).build();
  }
}
