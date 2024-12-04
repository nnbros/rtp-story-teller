package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.telegram.DefaultTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultElement;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.stream.Stream;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_ACTION_NAME;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static org.mockito.Mockito.verify;

public class ActionErrorProcessorTest extends StorytellerTest {

	@Mock
	private DefaultTelegramClient telegramClient;

	@InjectMocks
	private ActionErrorProcessor actionErrorProcessor;

	@ParameterizedTest
	@MethodSource("provideErrorProcessorTestParams")
	public void processCharacterNotFoundException(Exception e, DefaultElement expectedElement) {
		actionErrorProcessor.process(TEST_ACTION_NAME, TEST_USER_ID, e);
		verify(telegramClient).sendMessage(TEST_USER_ID, expectedElement);
	}

	private static Stream<Arguments> provideErrorProcessorTestParams() {
		return Stream.of(
				Arguments.of(new CharacterNotFoundException(TEST_USER_ID), DefaultElement.characterNotFound),
				Arguments.of(new RuntimeException(), DefaultElement.unknownError)
		);
	}
}
