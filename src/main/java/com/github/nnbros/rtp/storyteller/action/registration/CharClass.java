package com.github.nnbros.rtp.storyteller.action.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//TODO remove after dynamic button generation is implemented and class description is moved to configs
@Getter
@RequiredArgsConstructor
public enum CharClass {
	WARRIOR("Воин"),
	ROGUE("Плут");

	private final String className;
}
