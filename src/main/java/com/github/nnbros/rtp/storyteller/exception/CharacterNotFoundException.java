package com.github.nnbros.rtp.storyteller.exception;

public class CharacterNotFoundException extends StoryTellerRuntimeException {
	public CharacterNotFoundException() {
	}

	public CharacterNotFoundException(long userId) {
		super("Character for the user [%s] was not found", userId);
	}

	public CharacterNotFoundException(int characterId) {
		super("Character with id [%s] was not found", characterId);
	}
}
