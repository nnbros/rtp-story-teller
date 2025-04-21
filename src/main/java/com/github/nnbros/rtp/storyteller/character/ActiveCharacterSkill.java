package com.github.nnbros.rtp.storyteller.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActiveCharacterSkill(
		@JsonIgnore Integer characterId,
		@JsonIgnore Integer characterSkillId,
		@JsonIgnore Integer skillDictionaryId,
		@JsonProperty("name") String name,
		@JsonProperty("skillType") SkillType skillType,
		@JsonProperty("effectiveAgainst") Archetype effectiveAgainst) {
}
