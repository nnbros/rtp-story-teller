package com.github.nnbros.rtp.storyteller.character;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Character {

	@JsonProperty("characterName")
	private String name;
}
