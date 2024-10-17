package com.github.nnbros.rtp.storyteller.exception;

import org.springframework.http.HttpStatus;

public class StoryTellerException extends Exception {

	public StoryTellerException() {
	}

	public StoryTellerException(String message, Object... params) {
		super(message.formatted(params));
	}

	public StoryTellerException(String message, Throwable cause, Object... params) {
		super(message.formatted(params), cause);
	}

	public StoryTellerException(Throwable cause) {
		super(cause);
	}

	public HttpStatus getStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
