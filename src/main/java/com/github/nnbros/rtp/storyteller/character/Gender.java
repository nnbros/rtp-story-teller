package com.github.nnbros.rtp.storyteller.character;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
	//TODO refactor
	MALE("maleGenderText"),
	FEMALE("femaleGenderText");

	private final String elementName;
}
