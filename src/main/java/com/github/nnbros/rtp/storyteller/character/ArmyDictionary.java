package com.github.nnbros.rtp.storyteller.character;

public record ArmyDictionary(
		int id,
		String name,
		Archetype type,
		Integer baseQuantity,
		Integer baseHp,
		Integer baseAtk,
		Integer baseDef,
		Float advantageBonus) {
}
