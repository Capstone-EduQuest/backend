CREATE TABLE submission_status (
  id BIGINT NOT NULL,
  submission_id BIGINT NOT NULL,
  status VARCHAR(32) NOT NULL,
  try_count INT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  PRIMARY KEY (id)
);