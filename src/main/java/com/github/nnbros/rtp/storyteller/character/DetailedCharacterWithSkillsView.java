package com.github.nnbros.rtp.storyteller.character;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedCharacterWithSkillsView extends DetailedCharacterView {

	@JsonProperty("activeSkills")
	private final List<ActiveCharacterSkill> activeSkills;
}
