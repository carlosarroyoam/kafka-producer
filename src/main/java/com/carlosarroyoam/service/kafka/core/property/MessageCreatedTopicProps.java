package com.carlosarroyoam.service.kafka.core.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app.kafka.topics.message-created")
@Validated
@Getter
@Setter
public class MessageCreatedTopicProps {
  @NotBlank(message = "app.kafka.topics.message-created.name must not be empty")
  private String name;

  @Positive(message = "app.kafka.topics.message-created.partitions must be greater than zero")
  private int partitions;

  @Positive(message = "app.kafka.topics.message-created.replicas must be greater than zero")
  private short replicas;

  @NotNull(message = "app.kafka.topics.message-created.retention must not be null")
  @DurationMin(
      millis = 1,
      message = "app.kafka.topics.message-created.retention must be greater than zero")
  private Duration retention;
}
