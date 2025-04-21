package com.github.nnbros.rtp.storyteller.character;

public record ClassDictionary(
		int id,
		Archetype type,
		String name,
		Integer baseHp,
		Integer baseAtk,
		Integer baseDef,
		Float advantageBonus) {
}
