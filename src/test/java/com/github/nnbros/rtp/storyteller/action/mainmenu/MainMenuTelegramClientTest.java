package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterParameter;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.mockito.Mockito.*;

public class MainMenuTelegramClientTest extends StorytellerTest {
	public static final Character TEST_CHARACTER = createTestCharacter();

	@Mock
	private TelegramClient telegramClient;

	@Mock
	private TelegramElementRegistry elementRegistry;

	@Mock
	private CharacterService characterService;

	@Mock
	private ParameterService parameterService;

	@InjectMocks
	private MainMenuTelegramClient mainMenuTelegramClient;

	@BeforeEach
	public void init() {
		when(characterService.getByUserId(TEST_USER_ID)).thenReturn(TEST_CHARACTER);
	}

	@Test
	public void sendMainMenu() throws ElementNotFoundException, TelegramApiException {
		ActionContext testActionContext = createTestActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> parameters = buildTestCharacterBaseParameters(testActionContext);
		when(parameterService.buildCharacterBaseParameters(testActionContext)).thenReturn(parameters);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.main.name(), parameters);

		mainMenuTelegramClient.sendMainMenu(testActionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendCharacterMenu() throws ElementNotFoundException, TelegramApiException {
		ActionContext testActionContext = createTestActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> parameters = buildTestCharacterBaseParameters(testActionContext);
		when(parameterService.buildCharacterBaseParameters(testActionContext)).thenReturn(parameters);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.character.name(), parameters);

		mainMenuTelegramClient.sendCharacterMenu(testActionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	private Map<String, String> buildTestCharacterBaseParameters(ActionContext actionContext) {
		return Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()),
				Parameter.of(CharacterParameter.CHAR_NAME, MainMenuTelegramClientTest.TEST_CHARACTER.name()),
				Parameter.of(CharacterParameter.CHAR_CLASS, MainMenuTelegramClientTest.TEST_CHARACTER.activeClassDictionary().name())
		);
	}
}
