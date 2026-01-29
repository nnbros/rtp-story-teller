package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.common.api.dto.character.Archetype;

public record ClassDictionary(
		int id,
		Archetype type,
		String name,
		Integer baseHp,
		Integer baseAtk,
		Integer baseDef,
		Float advantageBonus) {
}
