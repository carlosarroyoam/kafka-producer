package com.carlosarroyoam.service.kafka.outbox;

import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEvent;
import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEvent.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
  List<OutboxEvent> findTop10ByDeliveredAtNullAndStatusOrderByCreatedAtAsc(Status status);
}
