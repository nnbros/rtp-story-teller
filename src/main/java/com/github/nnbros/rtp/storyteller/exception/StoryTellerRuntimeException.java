package com.github.nnbros.rtp.storyteller.exception;

import com.github.nnbros.rtp.common.exception.RtpRuntimeException;

public class StoryTellerRuntimeException extends RtpRuntimeException {
	public StoryTellerRuntimeException() {
	}

	public StoryTellerRuntimeException(String message, Object... params) {
		super(message, params);
	}

	public StoryTellerRuntimeException(String message, Throwable cause, Object... params) {
		super(message, cause, params);
	}

	public StoryTellerRuntimeException(Throwable cause) {
		super(cause);
	}
}
