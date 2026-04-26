package com.eduquest.backend.application.submission.listener;

import com.eduquest.backend.domain.submission.dto.AiFeedBackRequest;
import com.eduquest.backend.domain.submission.event.WrongNoteAiFeedBackEvent;
import com.eduquest.backend.domain.submission.model.WrongNote;
import com.eduquest.backend.domain.submission.service.ChatModelService;
import com.eduquest.backend.domain.submission.service.WrongNoteCommandService;
import com.eduquest.backend.domain.submission.service.WrongNoteQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WrongNoteAiFeedBackListener {

    private final ChatModelService chatModelService;
    private final WrongNoteQueryService wrongNoteQueryService;
    private final WrongNoteCommandService wrongNoteCommandService;

    @EventListener
    public void handleWrongNoteAiFeedBackEvent(WrongNoteAiFeedBackEvent event) {

        AiFeedBackRequest request = AiFeedBackRequest.of(
                event.summary(),
                event.expectedOutput(),
                event.wrongAnswer(),
                Map.of("block", event.block())
        );

        log.debug(event.toString());

        String aiExplanation = chatModelService.generateAiExplanation(request);

        log.debug("ai explanation: {}", aiExplanation);

        WrongNote wrongNote = wrongNoteQueryService.findWrongNoteByUuid(event.wrongNoteUuid());

        wrongNote.uodateAiExplanation(aiExplanation);

        wrongNoteCommandService.updateWrongNote(wrongNote);

    }

}
