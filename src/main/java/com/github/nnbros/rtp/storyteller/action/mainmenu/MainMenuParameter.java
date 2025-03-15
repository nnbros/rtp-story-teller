package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.parameter.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainMenuParameter implements ParameterKey {
	CLASSES("classes"),
	ARMIES("armies"),
	ARMY_DESCRIPTION("armyDescription");

	private final String key;
}
