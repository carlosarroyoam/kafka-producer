package com.carlosarroyoam.service.kafka.messages.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMessageRequestDto {
  private String content;
}
