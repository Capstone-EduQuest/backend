package com.eduquest.backend.domain.learning.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemQuery {

    public record Hint(Integer level, Long point, String content) {
        public static Hint of(Integer level, Long point, String content) {
            return new Hint(level, point, content);
        }
    }

    public record HintDetail(Long id, Long problemId, Integer level, Long point, String content) {
        public static HintDetail of(Long id, Long problemId, Integer level, Long point, String content) {
            return new HintDetail(id, problemId, level, point, content);
        }
    }

    public record Detail(
            Long id,
            UUID uuid,
            UUID stageUuid,
            String stageTitle,
            Integer stageNumber,
            String type,
            Integer number,
            String summary,
            String example,
            String expectedOutput,
            String block,
            List<Hint> hints
    ) {
        public static Detail of(
                Long id,
                UUID uuid,
                UUID stageUuid,
                String stageTitle,
                Integer stageNumber,
                String type,
                Integer number,
                String summary,
                String example,
                String expectedOutput,
                String block,
                List<Hint> hints
        ) {
            return new Detail(id, uuid, stageUuid, stageTitle, stageNumber, type, number, summary, example, expectedOutput, block, hints);
        }
    }

    public record Summary(UUID uuid, Integer number, String summary) {
        public static Summary of(UUID uuid, Integer number, String summary) {
            return new Summary(uuid, number, summary);
        }
    }

}

