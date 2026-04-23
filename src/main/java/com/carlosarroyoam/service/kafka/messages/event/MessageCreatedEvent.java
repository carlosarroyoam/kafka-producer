package com.carlosarroyoam.service.kafka.messages.event;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageCreatedEvent implements Serializable {
  private static final long serialVersionUID = -7450278099007241586L;
  private Long id;
  private String content;
}
