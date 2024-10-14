package com.github.nnbros.rtp.storyteller.exception;

public class StoryTellerRuntimeException extends RuntimeException {
	public StoryTellerRuntimeException() {
	}

	public StoryTellerRuntimeException(String message, Object... params) {
		super(message.formatted(params));
	}

	public StoryTellerRuntimeException(String message, Throwable cause, Object... params) {
		super(message.formatted(params), cause);
	}

	public StoryTellerRuntimeException(Throwable cause) {
		super(cause);
	}
}
