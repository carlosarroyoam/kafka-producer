package com.carlosarroyoam.service.kafka.messages.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageSentEvent {
  private Long id;
  private String content;
}
