package com.github.nnbros.rtp.storyteller.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActiveCharacterArmy(
		@JsonIgnore Integer characterId,
		@JsonIgnore Integer characterArmyId,
		@JsonIgnore Integer armyDictionaryId,
		@JsonProperty("name") String name,
		@JsonProperty("type") Archetype type,
		@JsonProperty("baseQuantity") Integer baseQuantity,
		@JsonProperty("baseHp") Integer baseHp,
		@JsonProperty("baseAtk") Integer baseAtk,
		@JsonProperty("baseDef") Integer baseDef,
		@JsonProperty("advantageBonus") Float advantageBonus,
		@JsonProperty("level") Integer level,
		@JsonProperty("tier") Integer tier) {
}
