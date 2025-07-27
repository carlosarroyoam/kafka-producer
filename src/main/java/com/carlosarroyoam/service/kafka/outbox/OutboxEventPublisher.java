package com.carlosarroyoam.service.kafka.outbox;

import com.carlosarroyoam.service.kafka.messages.event.MessageCreatedEvent;
import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEvent;
import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEvent.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventPublisher {
  private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisher.class);
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final OutboxEventRepository outboxRepository;
  private final ObjectMapper mapper;

  public OutboxEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
      OutboxEventRepository outboxRepository, ObjectMapper mapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.outboxRepository = outboxRepository;
    this.mapper = mapper;
  }

  @Scheduled(fixedRate = 3000)
  public void publishEvents() {
    List<OutboxEvent> events = outboxRepository
        .findTop10ByDeliveredAtNullAndStatusOrderByCreatedAtAsc(Status.PENDING);

    for (OutboxEvent event : events) {
      try {
        kafkaTemplate.send(event.getTopic(), event.getAggregateId(), payloadToObject(event));
        event.setDeliveredAt(LocalDateTime.now());
        event.setStatus(Status.PUBLISHED);
        log.info("Event published: {}, topic: {}, payload: {}", event.getId(), event.getTopic(),
            event.getPayload());
      } catch (RuntimeException | JsonProcessingException ex) {
        event.setStatus(Status.FAILED);
        event.setError(ex.getMessage());
        log.error("Failed to publish event: {}, error: {}", event.getId(), ex.getMessage());
      }

      outboxRepository.save(event);
    }
  }

  private Object payloadToObject(OutboxEvent event) throws JsonProcessingException {
    return switch (event.getEventType()) {
    case "MessageCreatedEvent" -> mapper.readValue(event.getPayload(), MessageCreatedEvent.class);
    default -> throw new IllegalArgumentException(
        "Not supported event type: " + event.getEventType());
    };
  }
}
