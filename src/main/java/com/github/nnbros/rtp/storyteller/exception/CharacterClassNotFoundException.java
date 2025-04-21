package com.github.nnbros.rtp.storyteller.exception;

public class CharacterClassNotFoundException extends StoryTellerRuntimeException {
	public CharacterClassNotFoundException() {
	}

	public CharacterClassNotFoundException(long userId, String className) {
		super("Character class for user [%s] and class dictionary id [%s] was not found", userId, className);
	}
}
