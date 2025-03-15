package com.github.nnbros.rtp.storyteller.character;

public record Character(String name,
						ClassDictionary activeClassDictionary,
						ArmyDictionary activeArmyDictionary) {
}
