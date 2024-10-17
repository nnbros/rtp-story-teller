package com.github.nnbros.rtp.storyteller.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Validated
public record ErrorResponse(
		@JsonProperty("error")
		String error,
		@JsonProperty("status")
		HttpStatus status,
		@JsonProperty("timestamp")
		Instant timestamp
) {
}
