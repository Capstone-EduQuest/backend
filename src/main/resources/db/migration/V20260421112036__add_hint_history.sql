CREATE TABLE hint_history
(
  id         BIGINT   NOT NULL,
  created_at DATETIME NOT NULL,
  member_id  BIGINT   NOT NULL,
  hint_id    BIGINT   NOT NULL,
  PRIMARY KEY (id)
);

CREATE INDEX idx_hint_history_member_id ON hint_history (member_id);

CREATE INDEX idx_hint_history_hint_id ON hint_history (hint_id);