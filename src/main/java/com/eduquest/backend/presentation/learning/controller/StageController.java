package com.eduquest.backend.presentation.learning.controller;

import com.eduquest.backend.application.learning.service.StageService;
import com.eduquest.backend.presentation.learning.dto.request.StageCreateRequest;
import com.eduquest.backend.presentation.learning.dto.request.StageListRequest;
import com.eduquest.backend.presentation.learning.dto.request.StageUpdateRequest;
import com.eduquest.backend.presentation.learning.dto.response.StageListResponse;
import com.eduquest.backend.presentation.learning.dto.response.StageResponse;
import com.eduquest.backend.presentation.learning.mapper.StageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StageController {

	private final StageService stageService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/stages")
	public ResponseEntity<String> createStage(@Valid @RequestBody StageCreateRequest request) {

		stageService.createStage(request.title(), request.number(), request.reward());

		return ResponseEntity.status(201).body("스테이지 생성 성공");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/stages/{uuid}")
	public ResponseEntity<Void> updateStage(@PathVariable UUID uuid, @Valid @RequestBody StageUpdateRequest request) {

		stageService.updateStage(uuid, request.title(), request.number(), request.reward());

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/stages/{uuid}")
	public ResponseEntity<Void> deleteStage(@PathVariable UUID uuid) {

		stageService.deleteStage(uuid);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/stages/{uuid}")
	public ResponseEntity<StageResponse> getStage(@PathVariable UUID uuid) {

		return ResponseEntity.ok(StageMapper.toResponse(stageService.getStage(uuid)));
	}

	@GetMapping("/stages")
	public ResponseEntity<StageListResponse> listStages(@Valid StageListRequest request) {

		return ResponseEntity.ok(StageMapper.toListResponse(stageService.listStages(request.page(), request.size(), request.sort(), request.isAsc())));
	}

}


