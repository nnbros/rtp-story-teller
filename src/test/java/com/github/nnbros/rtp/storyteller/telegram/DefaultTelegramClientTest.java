package com.github.nnbros.rtp.storyteller.telegram;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultElement;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collections;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DefaultTelegramClientTest extends StorytellerTest {
	@Mock
	private TelegramClient telegramClient;
	@Mock
	TelegramElementRegistry elementRegistry;

	@InjectMocks
	private DefaultTelegramClient defaultTelegramClient;

	@Test
	public void sendMessage() throws TelegramApiException, ElementNotFoundException {
		SendMessage message = mock(SendMessage.class);
		when(elementRegistry.buildElement(DefaultElement.unknownError.getGroupName(), DefaultElement.unknownError.name(), Collections.emptyMap(), SendMessage.class))
				.thenReturn(message);

		defaultTelegramClient.sendMessage(TEST_USER_ID, DefaultElement.unknownError);

		verify(telegramClient, times(1)).execute(message);
	}

	@Test
	public void catchTelegramException() throws TelegramApiException, ElementNotFoundException {
		SendMessage message = mock(SendMessage.class);
		when(elementRegistry.buildElement(DefaultElement.unknownError.getGroupName(), DefaultElement.unknownError.name(), Collections.emptyMap(), SendMessage.class))
				.thenReturn(message);
		when(telegramClient.execute(message)).thenThrow(new TelegramApiException("Test telegram exception message"));

		StoryTellerRuntimeException e = assertThrows(StoryTellerRuntimeException.class, () -> defaultTelegramClient.sendMessage(TEST_USER_ID, DefaultElement.unknownError));
		assertEquals(TelegramApiException.class, e.getCause().getClass());
	}
}
