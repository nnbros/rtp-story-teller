package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.guronas.telegram.bot.elements.parameter.InlineKeyboardButtonParameters;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.BotTestUtils;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuAction.ARMY;
import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.confirmationEmoji;
import static com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient.CALLBACK_DATA_TEMPLATE;
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
	@Mock
	private ArmyService armyService;
	@Spy
	private Localization localization = createTestLocalization();

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
		when(parameterService.buildCharacterBaseParameters(testActionContext, TEST_CHARACTER)).thenReturn(parameters);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.main.name(), parameters);

		mainMenuTelegramClient.sendMainMenu(testActionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendCharacterMenu() throws ElementNotFoundException, TelegramApiException {
		ActionContext testActionContext = createTestActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> parameters = buildTestCharacterBaseParameters(testActionContext);
		when(parameterService.buildCharacterBaseParameters(testActionContext, TEST_CHARACTER)).thenReturn(parameters);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.character.name(), parameters);

		mainMenuTelegramClient.sendCharacterMenu(testActionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = TEST_ARMY_1)
	public void sendCharacterClassMenu(String data) throws TelegramApiException, ElementNotFoundException {
		ActionContext testActionContext = BotTestUtils.createTestActionContext(data);
		when(characterService.getByUserId(TEST_USER_ID)).thenReturn(createTestCharacter());
		List<ArmyDictionary> armies = List.of(TEST_ARMY_POJO_1, TEST_ARMY_POJO_2, TEST_ARMY_POJO_3);
		when(armyService.getAllArmies()).thenReturn(armies);
		when(elementRegistry.buildElement(confirmationEmoji.getGroupName(), confirmationEmoji.name(), Collections.emptyMap(), String.class))
				.thenReturn(TEST_CONFIRMATION_EMOJI);

		InlineKeyboardButtonParameters dynamicParams = new InlineKeyboardButtonParameters();
		dynamicParams.add(TEST_CONFIRMATION_ARMY_PARAM, CALLBACK_DATA_TEMPLATE.formatted(ARMY.getActionName(), TEST_ARMY_1));
		dynamicParams.add(TEST_ARMY_NAME_2, CALLBACK_DATA_TEMPLATE.formatted(ARMY.getActionName(), TEST_ARMY_2));
		dynamicParams.add(TEST_ARMY_NAME_3, CALLBACK_DATA_TEMPLATE.formatted(ARMY.getActionName(), TEST_ARMY_3));
		Map<String, String> params = createTestCharacterBaseParameters();
		when(parameterService.buildCharacterBaseParameters(eq(testActionContext), eq(TEST_CHARACTER))).thenReturn(params);

		SendMessage mockMessage = mock(SendMessage.class);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(MainMenuElement.army.getGroupName(),
				MainMenuElement.army.name(),
				params,
				Map.of(MainMenuParameter.ARMIES.getKey(), dynamicParams));

		mainMenuTelegramClient.sendArmyMenu(testActionContext);

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
