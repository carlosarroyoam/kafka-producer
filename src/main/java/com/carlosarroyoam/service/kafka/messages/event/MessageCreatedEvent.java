package com.carlosarroyoam.service.kafka.messages.event;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageCreatedEvent implements Serializable {
  private static final long serialVersionUID = -7450278099007241586L;
  private Long id;
  private String content;
}
