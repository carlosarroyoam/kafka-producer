package com.carlosarroyoam.service.kafka.messages;

import com.carlosarroyoam.service.kafka.messages.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
