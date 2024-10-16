package com.github.nnbros.rtp.storyteller.api.controller;

import com.github.nnbros.rtp.storyteller.action.ActionService;
import com.github.nnbros.rtp.storyteller.api.model.ErrorResponse;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${storyteller.api.base-url}${storyteller.api.actions-endpoint-prefix}")
public class ActionController {
	private final ActionService actionService;

	@PostMapping("/{action}")
	public ResponseEntity<?> processAction(@NotNull @PathVariable String action,
													@Valid @RequestBody Update update,
													@RequestParam(required = false) String data) {
		try {
			actionService.process(action, update, data);
			return ResponseEntity.noContent().build();
		} catch (StoryTellerException e) {
			return createErrorResponse(e, e.getStatus(), action);
		} catch (Exception e) {
			return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, action);
		}
	}

	private ResponseEntity<?> createErrorResponse(Exception e, HttpStatus status, String action) {
		log.error("Failed to process the action [{}]", action, e);
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), status, Instant.now());
		return ResponseEntity.status(status.value()).body(errorResponse);
	}
}
