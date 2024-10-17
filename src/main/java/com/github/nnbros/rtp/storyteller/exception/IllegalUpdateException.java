package com.github.nnbros.rtp.storyteller.exception;

import org.springframework.http.HttpStatus;

public class IllegalUpdateException extends StoryTellerException {

	public IllegalUpdateException() {
	}

	public IllegalUpdateException(String message, Object... params) {
		super(message, params);
	}

	public IllegalUpdateException(String message, Throwable cause, Object... params) {
		super(message, cause, params);
	}

	public IllegalUpdateException(Throwable cause) {
		super(cause);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.BAD_REQUEST;
	}
}
