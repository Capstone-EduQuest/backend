CREATE TABLE bookmark (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  problem_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE community_answer (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  content TEXT NOT NULL,
  is_adopted BOOLEAN NOT NULL,
  user_id BIGINT NOT NULL,
  community_post_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE community_post (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  is_adopted BOOLEAN NOT NULL,
  created_at DATETIME NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE evaluation (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  is_correct BOOLEAN NOT NULL,
  submission_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE file (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  storage_type VARCHAR(255) NOT NULL,
  origin_name VARCHAR(255) NOT NULL,
  stored_name VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE hint (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  updated_at DATETIME NULL,
  created_at DATETIME NOT NULL,
  level INT NOT NULL,
  point BIGINT NOT NULL,
  content TEXT NOT NULL,
  problem_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE member (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  user_id VARCHAR(64) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  birth DATE NOT NULL,
  nickname VARCHAR(64) NOT NULL,
  is_locked BOOLEAN NOT NULL DEFAULT false,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  profile_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE note (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE problem (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  type VARCHAR(64) NOT NULL,
  number INT NOT NULL,
  summary TEXT NOT NULL,
  example TEXT NOT NULL,
  expected_output TEXT NOT NULL,
  block JSON NULL,
  stage_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE reset_password_token (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  user_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE reward_history (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  user_id BIGINT NOT NULL,
  stage_id BIGINT NOT NULL,
  amount BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE role (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  name VARCHAR(64) NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE stage (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  title VARCHAR(255) NOT NULL,
  number INTEGER NOT NULL,
  reward BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE submission (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL,
  answer TEXT NOT NULL,
  user_id BIGINT NOT NULL,
  problem_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_role (
  id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE wallet (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  balance BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE wallet_history (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  amount BIGINT NOT NULL,
  reason TEXT NULL,
  created_at DATETIME NOT NULL,
  wallet_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE wrong_note (
  id BIGINT NOT NULL,
  uuid CHAR(36) NOT NULL,
  wrong_answer TEXT NOT NULL,
  ai_explanation TEXT NULL,
  is_reviewed TINYINT NOT NULL DEFAULT 0,
  next_review_at DATETIME NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE INDEX idx_user_role_user_id ON user_role (user_id);

CREATE INDEX idx_user_role_role_id ON user_role (role_id);

CREATE INDEX idx_member_profile_id ON member (profile_id);

CREATE INDEX idx_reset_password_token_user_id ON reset_password_token (user_id);

CREATE INDEX idx_problem_stage_id ON problem (stage_id);

CREATE INDEX idx_submission_user_id ON submission (user_id);

CREATE INDEX idx_submission_problem_id ON submission (problem_id);

CREATE INDEX idx_wrong_note_user_id ON wrong_note (user_id);

CREATE INDEX idx_note_user_id ON note (user_id);

CREATE INDEX idx_community_post_user_id ON community_post (user_id);

CREATE INDEX idx_wallet_user_id ON wallet (user_id);

CREATE INDEX idx_wallet_history_wallet_id ON wallet_history (wallet_id);

CREATE INDEX idx_hint_problem_id ON hint (problem_id);

CREATE INDEX idx_bookmark_problem_id ON bookmark (problem_id);

CREATE INDEX idx_bookmark_user_id ON bookmark (user_id);

CREATE INDEX idx_evaluation_submission_id ON evaluation (submission_id);

CREATE INDEX idx_reward_history_user_id ON reward_history (user_id);

CREATE INDEX idx_reward_history_stage_id ON reward_history (stage_id);

CREATE INDEX idx_community_answer_user_id ON community_answer (user_id);

CREATE INDEX idx_community_answer_community_post_id ON community_answer (community_post_id);

CREATE INDEX uuid_unique ON member (uuid ASC);

CREATE INDEX user_id_unique ON member (user_id ASC);

CREATE INDEX uuid_unique ON role (uuid ASC);

CREATE INDEX uuid_unique ON file (uuid ASC);

CREATE INDEX uuid_unique ON reset_password_token (uuid ASC);

CREATE INDEX uuid_unique ON stage (uuid ASC);

CREATE INDEX uuid_unique ON problem (uuid ASC);

CREATE INDEX uuid_unique ON submission (uuid ASC);

CREATE INDEX uuid_unique ON wrong_note (uuid ASC);

CREATE INDEX uuid_unique ON note (uuid ASC);

CREATE INDEX uuid_unique ON community_post (uuid ASC);

CREATE INDEX uuid_unique ON wallet (uuid ASC);

CREATE INDEX uuid_unique ON wallet_history (uuid ASC);

CREATE INDEX uuid_unique ON hint (uuid ASC);

CREATE INDEX uuid_unique ON bookmark (uuid ASC);

CREATE INDEX uuid_unique ON evaluation (uuid ASC);

CREATE INDEX uuid_unique ON reward_history (uuid ASC);

CREATE INDEX uuid_unique ON community_answer (uuid ASC);