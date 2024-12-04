package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.telegram.DefaultTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionErrorProcessor {
	private final DefaultTelegramClient telegramClient;

	void process(String action, long userId, Throwable error) {
		if (error instanceof CharacterNotFoundException) {
			telegramClient.sendMessage(userId, DefaultElement.characterNotFound);
		} else {
			telegramClient.sendMessage(userId, DefaultElement.unknownError);
		}
		log.error("Failed to process action [{}] for the user [{}]", action, userId, error);
	}
}
