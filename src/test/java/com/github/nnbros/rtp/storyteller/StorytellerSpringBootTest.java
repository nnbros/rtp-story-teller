package com.github.nnbros.rtp.storyteller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@SpringBootTest
public abstract class StorytellerSpringBootTest {
	@MockBean
	private TelegramClient telegramClient;
}
