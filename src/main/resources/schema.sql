CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(528) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE events_outbox (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  aggregate_type VARCHAR(32) NOT NULL,
  aggregate_id VARCHAR(32),
  event_type VARCHAR(32) NOT NULL,
  payload VARCHAR(1024) NOT NULL,
  topic VARCHAR(254) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  error VARCHAR(1014),
  retries INT NOT NULL DEFAULT '0',
  created_at TIMESTAMP NOT NULL,
  published_at TIMESTAMP
);