package com.eduquest.backend.domain.submission.service;

import com.eduquest.backend.domain.submission.model.WrongNote;

public interface WrongNoteCommandService {

    /**
     * 존재하면 업데이트, 없으면 생성. 반환값은 WrongNote의 id
     */
    Long saveOrUpdateWrongNote(WrongNote wrongNote);

    /**
     * 편의를 위한 생성 헬퍼: 잘못된 답안을 기반으로 WrongNote 생성
     *
     * @param wrongAnswer 오답 텍스트
     * @param userId       회원 id
     * @param problemId    문제 id
     */
    Long createWrongNote(String wrongAnswer, Long userId, Long problemId);

    /**
     * 특정 유저-문제 조합의 wrongnote 삭제
     */
    void deleteByUserIdAndProblemId(Long userId, Long problemId);

    /**
     * isReviewed 상태 업데이트
     */
    void markReviewed(Long userId, Long problemId, boolean isReviewed);

}

