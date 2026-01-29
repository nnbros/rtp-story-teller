package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.common.api.dto.character.Archetype;

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
