package com.carlosarroyoam.service.kafka.core.config;

import com.carlosarroyoam.service.kafka.core.property.MessagesCreatedTopicProps;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
  @Bean
  NewTopic messagesTopic(MessagesCreatedTopicProps messageCreatedTopicProps) {
    return TopicBuilder.name(messageCreatedTopicProps.getName())
        .partitions(messageCreatedTopicProps.getPartitions())
        .replicas(messageCreatedTopicProps.getReplicas())
        .config(
            TopicConfig.RETENTION_MS_CONFIG,
            String.valueOf(messageCreatedTopicProps.getRetention().toMillis()))
        .build();
  }
}
