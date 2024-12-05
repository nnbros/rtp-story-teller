package com.github.nnbros.rtp.storyteller.telegram;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.nnbros.rtp.storyteller.telegram.ui.Element;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class DefaultTelegramClient extends AbstractTelegramClient {

	public DefaultTelegramClient(TelegramClient telegramClient, TelegramElementRegistry elementRegistry) {
		super(telegramClient, elementRegistry);
	}

	public void sendMessage(long userId, Element element) {
		log.debug("Sending message to the user [{}]...", userId);
		SendMessage message = buildElement(element, SendMessage.class);
		log.trace("Message:\n{}", message);
		execute(message);
		log.debug("Message has been sent successfully");
	}
}
