package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.parameter.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterMenuParameter implements ParameterKey {
	CLASSES("classes"),
	CLASS_DESCRIPTION("charClassDescription");

	private final String key;
}
