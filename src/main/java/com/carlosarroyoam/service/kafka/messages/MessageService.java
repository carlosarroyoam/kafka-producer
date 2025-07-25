package com.carlosarroyoam.service.kafka.messages;

import com.carlosarroyoam.service.kafka.config.KafkaProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private static final Logger log = LoggerFactory.getLogger(MessageService.class);
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public MessageService(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(Message message) {
    kafkaTemplate.send(KafkaProducerConfig.MESSAGES_TOPIC_NAME, message);
    log.info("Message send to Kafka topic : {}, Object: {}",
        KafkaProducerConfig.MESSAGES_TOPIC_NAME, message);
  }
}
