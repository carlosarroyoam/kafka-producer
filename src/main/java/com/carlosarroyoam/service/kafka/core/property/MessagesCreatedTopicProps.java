package com.carlosarroyoam.service.kafka.core.property;

import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app.kafka.topics.message-created")
@Validated
@Getter
@Setter
public class MessagesCreatedTopicProps {
  @NotEmpty(message = "app.kafka.topics.message-created.name must not be empty")
  private String name;

  @NotEmpty(message = "app.kafka.topics.message-created.partitions must not be empty")
  private int partitions;

  @NotEmpty(message = "app.kafka.topics.message-created.replicas must not be empty")
  private short replicas;

  @NotEmpty(message = "app.kafka.topics.message-created.retention must not be empty")
  private Duration retention;
}
