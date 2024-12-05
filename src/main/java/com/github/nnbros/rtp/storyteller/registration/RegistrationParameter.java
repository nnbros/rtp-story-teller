package com.github.nnbros.rtp.storyteller.registration;

import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationParameter implements ParameterKey {
	CHAR_GENDER("charGender"),
	CHAR_NAME("charName"),
	CHAR_CLASS("charClass"),
	CLASSES_DESCRIPTION("classesDescription"),
	SKILLS_DESCRIPTION("skillsDescription"),
	CONFIRMATION_WARRIOR("warriorClassText"),
	CONFIRMATION_ROUGE("rogueClassText");

	private final String key;
}
