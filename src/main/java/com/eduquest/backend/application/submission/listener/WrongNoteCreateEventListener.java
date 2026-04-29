package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.submission.event.WrongNoteCreateRequestedEvent;
import com.eduquest.backend.domain.submission.service.WrongNoteCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WrongNoteCreateEventListener {

	private final WrongNoteCommandService wrongNoteCommandService;

	@Async("evaluationTaskExecutor")
	@TransactionalEventListener
	public void handleWrongNoteCreateRequested(WrongNoteCreateRequestedEvent event) {
		wrongNoteCommandService.createWrongNote(event.wrongAnswer(), event.memberId(), event.problemId());
	}
}


