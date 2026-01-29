package com.github.nnbros.rtp.storyteller.api.controller;

import com.github.nnbros.rtp.common.api.dto.error.ErrorResponse;
import com.github.nnbros.rtp.storyteller.action.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${storyteller.api.base-url}${storyteller.api.cache-endpoint-prefix}")
public class CacheController {
	public static final String REGISTRATION_URL_SUFFIX = "/registration";

	private final RegistrationService registrationService;

	@DeleteMapping(REGISTRATION_URL_SUFFIX)
	public ResponseEntity<?> clearCharacterRegistrationCache(@RequestBody Set<Long> userIds) {
		try {
			registrationService.clearCharacterCache(userIds);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			log.error("Failed to clear character registration cache, for users: {}", userIds, e);
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), Instant.now());
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}
}
