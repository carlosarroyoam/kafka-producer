package com.carlosarroyoam.service.kafka.messages;

import com.carlosarroyoam.service.kafka.messages.dto.CreateMessageRequestDto;
import com.carlosarroyoam.service.kafka.messages.dto.MessageDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping(produces = "application/json")
  public ResponseEntity<List<MessageDto>> findAll(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "25") Integer size) {
    List<MessageDto> messages = messageService.findAll(page, size);
    return ResponseEntity.ok(messages);
  }

  @GetMapping(value = "/{messageId}", produces = "application/json")
  public ResponseEntity<MessageDto> findById(@PathVariable Long messageId) {
    MessageDto messageById = messageService.findById(messageId);
    return ResponseEntity.ok(messageById);
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<Void> create(@RequestBody CreateMessageRequestDto request,
      UriComponentsBuilder builder) {
    MessageDto messageDto = messageService.send(request);
    UriComponents uriComponents = builder.path("/messages/{messageId}")
        .buildAndExpand(messageDto.getId());
    return ResponseEntity.created(uriComponents.toUri()).build();
  }
}
