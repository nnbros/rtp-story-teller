package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.nnbros.rtp.common.action.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterAction implements Action {
	CREATE_START("storyteller_create_char_start"),
	CREATE_GENDER("storyteller_create_char_gender"),
	CREATE_NAME("storyteller_create_char_name"),
	CLASS_SELECTION("storyteller_create_char_class_selection"),
	CLASS_CONFIRMATION("storyteller_create_char_class_confirmation"),
	REGISTRATION("storyteller_create_char_registration");

	private final String actionName;
}
