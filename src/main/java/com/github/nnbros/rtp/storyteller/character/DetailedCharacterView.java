package com.github.nnbros.rtp.storyteller.character;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedCharacterView extends Character {

	@JsonProperty("activeClass")
	private ActiveCharacterClass activeClass;

	@JsonProperty("activeArmy")
	private ActiveCharacterArmy activeArmy;
}
