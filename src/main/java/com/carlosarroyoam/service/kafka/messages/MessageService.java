package com.carlosarroyoam.service.kafka.messages;

import com.carlosarroyoam.service.kafka.config.KafkaProducerConfig;
import com.carlosarroyoam.service.kafka.messages.dto.CreateMessageRequestDto;
import com.carlosarroyoam.service.kafka.messages.dto.MessageDto;
import com.carlosarroyoam.service.kafka.messages.dto.MessageDto.MessageDtoMapper;
import com.carlosarroyoam.service.kafka.messages.entity.Message;
import com.carlosarroyoam.service.kafka.messages.event.MessageSentEvent;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final MessageRepository messageRepository;

  public MessageService(KafkaTemplate<String, Object> kafkaTemplate,
      MessageRepository messageRepository) {
    this.kafkaTemplate = kafkaTemplate;
    this.messageRepository = messageRepository;
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
  public MessageDto send(CreateMessageRequestDto request) {
    LocalDateTime now = LocalDateTime.now();
    Message message = MessageDtoMapper.INSTANCE.createRequestToEntity(request);
    message.setCreatedAt(now);
    message.setUpdatedAt(now);
    messageRepository.save(message);

    MessageSentEvent messageSentEvent = MessageSentEvent.builder()
        .id(message.getId())
        .content(message.getContent())
        .build();
    kafkaTemplate.send(KafkaProducerConfig.MESSAGES_TOPIC_NAME, messageSentEvent);

    log.info("Message sent to Kafka topic : {}, Object: {}",
        KafkaProducerConfig.MESSAGES_TOPIC_NAME, request);

    return MessageDtoMapper.INSTANCE.toDto(message);
  }
}
