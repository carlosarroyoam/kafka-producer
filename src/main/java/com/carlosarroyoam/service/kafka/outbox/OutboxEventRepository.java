package com.carlosarroyoam.service.kafka.outbox;

import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEvent;
import com.carlosarroyoam.service.kafka.outbox.entity.OutboxEventStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
  List<OutboxEvent> findTop10ByPublishedAtNullAndStatusOrderByCreatedAtAsc(
      OutboxEventStatus status);

  List<OutboxEvent> findTop10ByPublishedAtNullAndStatusAndRetriesLessThanEqualOrderByCreatedAtAsc(
      OutboxEventStatus status, Integer retries);
}
