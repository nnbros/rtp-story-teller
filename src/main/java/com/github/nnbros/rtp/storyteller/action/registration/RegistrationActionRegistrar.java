package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.nnbros.rtp.storyteller.action.ActionPipeline;
import com.github.nnbros.rtp.storyteller.action.ActionRegistrar;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.nnbros.rtp.storyteller.action.ActionPipelines.*;
import static com.github.nnbros.rtp.storyteller.action.registration.CharacterAction.*;
import static com.github.nnbros.rtp.storyteller.util.StorytellerUtils.emptyConsumer;

@Component
public class RegistrationActionRegistrar implements ActionRegistrar {
	private final Map<String, ActionPipeline> actionPipelines;

	public RegistrationActionRegistrar(RegistrationService registrationService,
									   RegistrationTelegramClient telegramClient) {
		actionPipelines = initPipelines(registrationService, telegramClient);
	}

	private static Map<String, ActionPipeline> initPipelines(RegistrationService registrationService, RegistrationTelegramClient telegramClient) {
		return Map.of(
				CREATE_START.getActionName(), create(registrationService::createCharacter, telegramClient::sendGenderOptions),
				CREATE_GENDER.getActionName(), create(registrationService::addGender, telegramClient::requestName),
				CREATE_NAME.getActionName(), create(registrationService::addName, telegramClient::sendClassOptions, telegramClient::resendCharacterNameRequest),
				CLASS_SELECTION.getActionName(), create(registrationService::addClass, telegramClient::sendClassDescription, emptyConsumer()),
				CLASS_CONFIRMATION.getActionName(), create(registrationService::getCharacterRequest, telegramClient::sendRegistrationRequest),
				REGISTRATION.getActionName(), create(registrationService::register, telegramClient::sendRegistrationConfirmation)
		);
	}

	@Override
	public void register(Map<String, ActionPipeline> actionPipelines) {
		actionPipelines.putAll(this.actionPipelines);
	}
}
