package com.carlosarroyoam.service.kafka.messages;

import com.carlosarroyoam.service.kafka.config.KafkaProducerConfig;
import com.carlosarroyoam.service.kafka.messages.dto.CreateMessageRequestDto;
import com.carlosarroyoam.service.kafka.messages.dto.MessageDto;
import com.carlosarroyoam.service.kafka.messages.dto.MessageDto.MessageDtoMapper;
import com.carlosarroyoam.service.kafka.messages.entity.Message;
import com.carlosarroyoam.service.kafka.messages.event.MessageCreatedEvent;
import com.carlosarroyoam.service.kafka.outbox.OutboxEventRepository;
import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);
  private final MessageRepository messageRepository;
  private final OutboxEventRepository outboxRepository;
  private final ObjectMapper mapper;

  public MessageService(MessageRepository messageRepository, OutboxEventRepository outboxRepository,
      ObjectMapper mapper) {
    this.messageRepository = messageRepository;
    this.outboxRepository = outboxRepository;
    this.mapper = mapper;
  }

  public List<MessageDto> findAll(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Message> messages = messageRepository.findAll(pageable);
    return MessageDtoMapper.INSTANCE.toDtos(messages.getContent());
  }

  public MessageDto findById(Long messageId) {
    Message messageById = messageRepository.findById(messageId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));

    return MessageDtoMapper.INSTANCE.toDto(messageById);
  }

  @Transactional
  public MessageDto send(CreateMessageRequestDto request) throws JsonProcessingException {
    LocalDateTime now = LocalDateTime.now();
    Message message = MessageDtoMapper.INSTANCE.createRequestToEntity(request);
    message.setCreatedAt(now);
    message.setUpdatedAt(now);
    messageRepository.save(message);

    MessageCreatedEvent messageSentEvent = MessageCreatedEvent.builder()
        .id(message.getId())
        .content(message.getContent())
        .build();

    OutboxEvent event = new OutboxEvent();
    event.setAggregateType(Message.class.getSimpleName());
    event.setAggregateId(message.getId().toString());
    event.setEventType(MessageCreatedEvent.class.getSimpleName());
    event.setPayload(mapper.writeValueAsString(messageSentEvent));
    event.setTopic(KafkaProducerConfig.MESSAGES_CREATED_TOPIC_NAME);
    event.setCreatedAt(now);
    outboxRepository.save(event);

    log.info("Message created: {}", message);
    return MessageDtoMapper.INSTANCE.toDto(message);
  }
}
