package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.guronas.telegram.bot.elements.parameter.InlineKeyboardButtonParameters;
import com.github.nnbros.rtp.storyteller.BotTestUtils;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.ClassService;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterService;
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
import static com.github.nnbros.rtp.storyteller.action.mainmenu.CharacterMenuAction.CLASS;
import static com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuTelegramClientTest.TEST_DETAILED_CHARACTER_VIEW;
import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.confirmationEmoji;
import static com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient.CALLBACK_DATA_TEMPLATE;
import static org.mockito.Mockito.*;

public class CharacterMenuTelegramClientTest extends StorytellerTest {

	@Mock
	private TelegramElementRegistry elementRegistry;
	@Mock
	private TelegramClient telegramClient;
	@Mock
	private ClassService classService;
	@Mock
	private CharacterService characterService;
	@Mock
	private ParameterService parameterService;
	@Spy
	private Localization localization = createTestLocalization();

	@InjectMocks
	private CharacterMenuTelegramClient characterMenuTelegramClient;

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = TEST_CLASS_1)
	public void sendCharacterClassMenu(String data) throws TelegramApiException, ElementNotFoundException {
		ActionContext testActionContext = BotTestUtils.createTestActionContext(data);
		when(characterService.getDetailedCharacterViewByUserId(TEST_USER_ID)).thenReturn(createTestDetailedCharacterView());
		List<ClassDictionary> classes = List.of(TEST_CLASS_POJO_1, TEST_CLASS_POJO_2, TEST_CLASS_POJO_3);
		when(classService.getAllClasses()).thenReturn(classes);
		when(elementRegistry.buildElement(confirmationEmoji.getGroupName(), confirmationEmoji.name(), Collections.emptyMap(), String.class))
				.thenReturn(TEST_CONFIRMATION_EMOJI);

		InlineKeyboardButtonParameters dynamicParams = new InlineKeyboardButtonParameters();
		dynamicParams.add(TEST_CONFIRMATION_CLASS_PARAM, CALLBACK_DATA_TEMPLATE.formatted(CLASS.getActionName(), TEST_CLASS_1));
		dynamicParams.add(TEST_CLASS_NAME_2, CALLBACK_DATA_TEMPLATE.formatted(CLASS.getActionName(), TEST_CLASS_2));
		dynamicParams.add(TEST_CLASS_NAME_3, CALLBACK_DATA_TEMPLATE.formatted(CLASS.getActionName(), TEST_CLASS_3));
		Map<String, String> params = createTestCharacterBaseParameters();
		when(parameterService.buildCharacterBaseParameters(eq(testActionContext), eq(TEST_DETAILED_CHARACTER_VIEW))).thenReturn(params);

		SendMessage mockMessage = mock(SendMessage.class);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(MainMenuElement.charClass.getGroupName(),
				MainMenuElement.charClass.name(),
				params,
				Map.of(MainMenuParameter.CLASSES.getKey(), dynamicParams));

		characterMenuTelegramClient.sendCharacterClassMenu(testActionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}
}
