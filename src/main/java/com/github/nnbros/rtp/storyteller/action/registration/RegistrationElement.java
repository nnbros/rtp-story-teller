package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.nnbros.rtp.storyteller.telegram.ui.Element;

public enum RegistrationElement implements Element {
	createCharGender,
	createCharName,
	invalidCharName,
	charClassSelection,
	charClassConfirmation,
	confirmationEmoji,
	charRegistration,
	registrationComplete;

	public static final String REGISTRATION_GROUP_NAME = "registration";

	@Override
	public String getGroupName() {
		return REGISTRATION_GROUP_NAME;
	}
}
