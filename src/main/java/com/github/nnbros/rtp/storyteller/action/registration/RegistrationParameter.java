package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.guronas.telegram.bot.elements.parameter.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationParameter implements ParameterKey {
	CLASSES_DESCRIPTION("classesDescription"),
	SKILLS_DESCRIPTION("skillsDescription"),
	WARRIOR_CLASS_TEXT("warriorClassText"),
	ROGUE_CLASS_TEXT("rogueClassText"),

	//TODO remove after dynamic params are implemented
	WARRIOR("warrior"),
	ROGUE("rogue");

	private final String key;
}
