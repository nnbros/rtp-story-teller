package com.github.nnbros.rtp.storyteller.telegram;

import com.github.nnbros.rtp.storyteller.telegram.ui.TelegramElements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class DefaultTelegramClient extends AbstractTelegramClient {

	public DefaultTelegramClient(TelegramClient telegramClient) {
		super(telegramClient);
	}

	public void sendMessage(long userId, String text) {
		log.debug("Sending message to the user [{}]...", userId);
		log.trace("Message text:\n{}", text);
		SendMessage message = TelegramElements.message(userId, text);
		execute(message);
		log.debug("Message has been sent successfully");
	}
}
