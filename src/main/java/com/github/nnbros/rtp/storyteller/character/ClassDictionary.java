package com.github.nnbros.rtp.storyteller.character;

public record ClassDictionary(
		int id,
		String name,
		String description,
		Integer baseHp,
		Integer baseAtk,
		Integer baseDef) {
}
