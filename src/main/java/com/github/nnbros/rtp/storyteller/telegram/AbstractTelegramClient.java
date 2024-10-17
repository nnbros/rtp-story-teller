package com.github.nnbros.rtp.storyteller.telegram;

import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

@RequiredArgsConstructor
public abstract class AbstractTelegramClient {
	private final TelegramClient telegramClient;

	protected <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method request) {
		try {
			telegramClient.execute(request);
		} catch (TelegramApiException e) {
			//TODO retry
			throw new StoryTellerRuntimeException(e);
		}
	}
}
