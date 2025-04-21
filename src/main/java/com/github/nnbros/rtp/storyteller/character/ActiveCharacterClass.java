package com.github.nnbros.rtp.storyteller.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActiveCharacterClass(
		@JsonIgnore Integer characterId,
		@JsonIgnore Integer characterClassId,
		@JsonIgnore Integer classDictionaryId,
		@JsonProperty("type") Archetype type,
		@JsonProperty("name") String name,
		@JsonProperty("baseHp") Integer baseHp,
		@JsonProperty("baseAtk") Integer baseAtk,
		@JsonProperty("baseDef") Integer baseDef,
		@JsonProperty("advantageBonus") Float advantageBonus,
		@JsonProperty("experience") Long experience) {
}
