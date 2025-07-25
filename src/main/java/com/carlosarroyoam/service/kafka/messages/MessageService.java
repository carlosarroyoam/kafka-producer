package com.carlosarroyoam.service.kafka.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);
  private static final String MESSAGES_TOPIC_NAME = "com.carlosarroyoam.kafka.messages";
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public MessageService(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(Message message) {
    kafkaTemplate.send(MESSAGES_TOPIC_NAME, message);
    log.info("Message send to Kafka topic : {}, Object: {}", MESSAGES_TOPIC_NAME, message);
  }
}
