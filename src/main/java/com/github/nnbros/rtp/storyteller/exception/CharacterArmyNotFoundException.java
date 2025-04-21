package com.github.nnbros.rtp.storyteller.exception;

public class CharacterArmyNotFoundException extends StoryTellerRuntimeException {
    public CharacterArmyNotFoundException() {
    }

    public CharacterArmyNotFoundException(long userId, String armyName) {
        super("Character army for user [%s] and army dictionary id [%s] was not found", userId, armyName);
    }
}
