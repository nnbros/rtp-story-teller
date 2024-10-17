package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.telegram.DefaultTelegramClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionErrorProcessor {
	private final DefaultTelegramClient telegramClient;
	@Value("${ui.registration_messages.character.not_found}")
	private String characterNotFoundMessage;
	@Value("${ui.general_messages.unknown_error}")
	private String generalErrorMessage;

	void process(String action, long userId, Throwable error) {
		if (error instanceof CharacterNotFoundException) {
			telegramClient.sendMessage(userId, characterNotFoundMessage);
		} else {
			telegramClient.sendMessage(userId, generalErrorMessage);
		}
		log.error("Failed to process action [{}] for the user [{}]", action, userId, error);
	}
}
