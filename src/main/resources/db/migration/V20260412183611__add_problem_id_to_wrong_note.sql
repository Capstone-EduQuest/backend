
ALTER TABLE wrong_note ADD COLUMN problem_id BIGINT NOT NULL;

CREATE INDEX idx_wrong_note_problem_id ON wrong_note (problem_id ASC);