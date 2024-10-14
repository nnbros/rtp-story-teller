package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.action.ActionPipeline;
import com.github.nnbros.rtp.storyteller.action.ActionRegistrar;
import com.github.nnbros.rtp.storyteller.registration.RegistrationService;
import com.github.nnbros.rtp.storyteller.registration.RegistrationTelegramClient;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.nnbros.rtp.storyteller.action.ActionPipelines.*;
import static com.github.nnbros.rtp.storyteller.character.CharacterAction.*;

@Component
public class CharacterActionRegistrar implements ActionRegistrar {
	private final Map<String, ActionPipeline> actionPipelines;

	public CharacterActionRegistrar(RegistrationService registrationService,
									RegistrationTelegramClient telegramClient) {
		actionPipelines = initPipelines(registrationService, telegramClient);
	}

	private static Map<String, ActionPipeline> initPipelines(RegistrationService registrationService, RegistrationTelegramClient telegramClient) {
		return Map.of(
				CREATE_START.getActionName(), create(registrationService::createCharacter, telegramClient::sendGenderOptions),
				CREATE_GENDER.getActionName(), create(registrationService::addGender, telegramClient::requestName),
				CREATE_NAME.getActionName(), create(registrationService::addName, telegramClient::sendClassOptions, telegramClient::resendCharacterNameRequest),
				CLASS_SELECTION.getActionName(), create(registrationService::addClass, telegramClient::sendClassDescription),
				CLASS_CONFIRMATION.getActionName(), create(registrationService::getCharacterRequest, telegramClient::sendRegistrationRequest),
				REGISTRATION.getActionName(), create(registrationService::register, telegramClient::sendRegistrationConfirmation)
		);
	}

	@Override
	public void register(Map<String, ActionPipeline> actionPipelines) {
		actionPipelines.putAll(this.actionPipelines);
	}
}
