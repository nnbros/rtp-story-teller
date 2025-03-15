package com.github.nnbros.rtp.storyteller.character;

public record ArmyDictionary(
		int id,
		String name,
		ArmyType type,
		Integer baseHp,
		Integer baseCount,
		Integer baseAtk,
		Integer baseDef) {
}
