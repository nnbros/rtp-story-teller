package com.github.nnbros.rtp.storyteller.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfiguration {

	@Bean
	public TelegramClient telegramClient(StoryTellerProperties properties) {
		return new OkHttpTelegramClient(properties.getRtpBot().getToken());
	}
}
