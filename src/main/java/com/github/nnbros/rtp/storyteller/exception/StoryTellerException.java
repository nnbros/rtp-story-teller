package com.github.nnbros.rtp.storyteller.exception;

import com.github.nnbros.rtp.common.exception.RtpException;
import org.springframework.http.HttpStatus;

public class StoryTellerException extends RtpException {

	public StoryTellerException() {
	}

	public StoryTellerException(String message, Object... params) {
		super(message, params);
	}

	public StoryTellerException(String message, Throwable cause, Object... params) {
		super(message, cause, params);
	}

	public StoryTellerException(Throwable cause) {
		super(cause);
	}

	public HttpStatus getStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
