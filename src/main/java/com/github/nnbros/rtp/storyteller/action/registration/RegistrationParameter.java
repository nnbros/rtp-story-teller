package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.guronas.telegram.bot.elements.parameter.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationParameter implements ParameterKey {
	CLASSES_DESCRIPTION("classesDescription"),
	SKILLS_DESCRIPTION("skillsDescription"),
	CLASSES("classes");

	private final String key;
}
