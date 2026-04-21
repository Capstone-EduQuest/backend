package com.eduquest.backend.presentation.learning.controller;

import com.eduquest.backend.application.learning.dto.ProblemDto;
import com.eduquest.backend.application.learning.dto.ProblemListDto;
import com.eduquest.backend.application.learning.service.ProblemService;
import com.eduquest.backend.presentation.learning.dto.request.ProblemCreateRequest;
import com.eduquest.backend.presentation.learning.dto.request.ProblemListRequest;
import com.eduquest.backend.presentation.learning.dto.request.ProblemUpdateRequest;
import com.eduquest.backend.presentation.learning.dto.response.HintResponse;
import com.eduquest.backend.presentation.learning.dto.response.ProblemListResponse;
import com.eduquest.backend.presentation.learning.dto.response.ProblemResponse;
import com.eduquest.backend.presentation.learning.mapper.HintMapper;
import com.eduquest.backend.presentation.learning.mapper.ProblemMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProblemController {

	private final ProblemService problemService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/problems")
	public ResponseEntity<String> createProblem(@Valid @RequestBody ProblemCreateRequest request) {

		problemService.createProblem(ProblemMapper.toCommand(request));

		return ResponseEntity.status(201).body("문제 생성 성공");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/problems/{uuid}")
	public ResponseEntity<Void> updateProblem(@PathVariable UUID uuid, @Valid @RequestBody ProblemUpdateRequest request) {

		problemService.updateProblem(uuid, ProblemMapper.toCommand(request));

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/problems/{uuid}")
	public ResponseEntity<Void> deleteProblem(@PathVariable UUID uuid) {

		problemService.deleteProblem(uuid);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/problems/{uuid}")
	public ResponseEntity<ProblemResponse> getProblem(@PathVariable UUID uuid) {

		ProblemDto problem = problemService.getProblem(uuid);

		return ResponseEntity.ok(ProblemMapper.toResponse(problem));
	}

	@GetMapping(value = "/problems", params = "stage_number")
	public ResponseEntity<List<ProblemResponse>> getProblemsByStageNumber(@RequestParam("stage_number") Integer stageNumber) {

		List<ProblemDto> results = problemService.findProblemsByStageNumber(stageNumber);

		return ResponseEntity.ok(ProblemMapper.toResponseList(results));
	}

	@GetMapping("/problems")
	public ResponseEntity<ProblemListResponse> listProblems(@Valid ProblemListRequest request) {

		ProblemListDto problems = problemService.listProblems(request.page(), request.size(), request.sort(), request.isAsc());

		return ResponseEntity.ok(ProblemMapper.toListResponse(problems));
	}

	@GetMapping("/problems/{uuid}/hint")
	public ResponseEntity<HintResponse> findHint(
			@PathVariable UUID uuid,
			@RequestParam("level") Integer level,
			Authentication authentication
	) {

		String userId = authentication.getName();

		return ResponseEntity.ok(HintMapper.toResponse(problemService.findHint(uuid, level, authentication.getName())));
	}

}


