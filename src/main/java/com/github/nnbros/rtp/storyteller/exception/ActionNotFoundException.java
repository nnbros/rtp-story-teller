package com.github.nnbros.rtp.storyteller.exception;

import org.springframework.http.HttpStatus;

public class ActionNotFoundException extends StoryTellerException {

	public ActionNotFoundException(String action) {
		super("Action with id [%s] was not found", action);
	}


	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
