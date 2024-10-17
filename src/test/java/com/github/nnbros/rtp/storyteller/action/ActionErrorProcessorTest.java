package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.telegram.DefaultTelegramClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_ACTION_NAME;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static org.mockito.Mockito.verify;

public class ActionErrorProcessorTest extends StorytellerTest {
	private static final String CHARACTER_NOT_FOUND_MESSAGE = "testCharacterNotFoundMessage";
	private static final String GENERAL_ERROR_MESSAGE = "testGeneralErrorMessage";

	@Mock
	private DefaultTelegramClient telegramClient;

	@InjectMocks
	private ActionErrorProcessor actionErrorProcessor;

	@BeforeEach
	public void setUp() {
		ReflectionTestUtils.setField(actionErrorProcessor, "characterNotFoundMessage", CHARACTER_NOT_FOUND_MESSAGE);
		ReflectionTestUtils.setField(actionErrorProcessor, "generalErrorMessage", GENERAL_ERROR_MESSAGE);
	}

	@ParameterizedTest
	@MethodSource("provideErrorProcessorTestParams")
	public void processCharacterNotFoundException(Exception e, String expectedMssage) throws TelegramApiException {
		actionErrorProcessor.process(TEST_ACTION_NAME, TEST_USER_ID, e);
		verify(telegramClient).sendMessage(TEST_USER_ID, expectedMssage);
	}

	private static Stream<Arguments> provideErrorProcessorTestParams() {
		return Stream.of(
				Arguments.of(new CharacterNotFoundException(TEST_USER_ID), CHARACTER_NOT_FOUND_MESSAGE),
				Arguments.of(new RuntimeException(), GENERAL_ERROR_MESSAGE)
		);
	}
}
