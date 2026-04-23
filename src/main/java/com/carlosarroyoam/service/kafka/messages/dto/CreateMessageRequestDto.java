package com.carlosarroyoam.service.kafka.messages.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateMessageRequestDto {
  private String content;
}
