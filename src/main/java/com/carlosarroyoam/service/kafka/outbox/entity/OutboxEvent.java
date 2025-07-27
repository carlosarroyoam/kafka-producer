package com.carlosarroyoam.service.kafka.outbox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "aggregate_type", length = 32, nullable = false)
  private String aggregateType;

  @Column(name = "aggregate_id", length = 32)
  private String aggregateId;

  @Column(name = "event_type", length = 32)
  private String eventType;

  @Column(name = "payload", length = 1024, nullable = false)
  private String payload;

  @Column(name = "topic", length = 254, nullable = false)
  private String topic;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Status status = Status.PENDING;

  @Column(name = "error", length = 1024)
  private String error;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "delivered_at")
  private LocalDateTime deliveredAt;

  public enum Status {
    PENDING, PUBLISHED, FAILED
  }
}
