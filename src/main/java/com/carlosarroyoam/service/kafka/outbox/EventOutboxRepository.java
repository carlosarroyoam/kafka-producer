package com.carlosarroyoam.service.kafka.outbox;

import com.carlosarroyoam.service.kafka.outbox.entity.EventOutbox;
import com.carlosarroyoam.service.kafka.outbox.entity.EventOutboxStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventOutboxRepository extends JpaRepository<EventOutbox, Long> {
  List<EventOutbox> findTop10ByPublishedAtNullAndStatusOrderByCreatedAtAsc(
      EventOutboxStatus status);

  List<EventOutbox> findTop10ByPublishedAtNullAndStatusAndRetriesLessThanEqualOrderByCreatedAtAsc(
      EventOutboxStatus status, Integer retries);
}
