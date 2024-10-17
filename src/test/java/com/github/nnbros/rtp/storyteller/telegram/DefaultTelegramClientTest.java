package com.github.nnbros.rtp.storyteller.telegram;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.telegram.ui.TelegramElements;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_TEXT;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DefaultTelegramClientTest extends StorytellerTest {
	@Mock
	private TelegramClient telegramClient;

	@InjectMocks
	private DefaultTelegramClient defaultTelegramClient;

	@Test
	public void sendMessage() throws TelegramApiException {
		SendMessage message = TelegramElements.message(TEST_USER_ID, TEST_TEXT);

		defaultTelegramClient.sendMessage(TEST_USER_ID, TEST_TEXT);

		verify(telegramClient, times(1)).execute(message);
	}

	@Test
	public void catchTelegramException() throws TelegramApiException {
		SendMessage message = TelegramElements.message(TEST_USER_ID, TEST_TEXT);
		when(telegramClient.execute(message)).thenThrow(new TelegramApiException("Test telegram exception message"));

		StoryTellerRuntimeException e = assertThrows(StoryTellerRuntimeException.class, () -> defaultTelegramClient.sendMessage(TEST_USER_ID, TEST_TEXT));
		assertEquals(TelegramApiException.class, e.getCause().getClass());
	}
}
