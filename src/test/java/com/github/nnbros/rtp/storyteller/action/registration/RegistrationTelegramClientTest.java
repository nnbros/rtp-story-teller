package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.guronas.telegram.bot.elements.parameter.InlineKeyboardButtonParameters;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.common.action.ActionContext;
import com.github.nnbros.rtp.common.action.ActionResult;
import com.github.nnbros.rtp.common.api.dto.character.Archetype;
import com.github.nnbros.rtp.common.api.dto.character.Gender;
import com.github.nnbros.rtp.common.api.dto.character.SkillType;
import com.github.nnbros.rtp.common.telegram.ui.CharacterParameter;
import com.github.nnbros.rtp.common.telegram.ui.DefaultParameter;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuTelegramClient;
import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

import static com.github.nnbros.rtp.common.telegram.AbstractTelegramClient.CALLBACK_DATA_TEMPLATE;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static com.github.nnbros.rtp.storyteller.action.registration.CharacterAction.CLASS_SELECTION;
import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.*;
import static org.mockito.Mockito.*;

public class RegistrationTelegramClientTest extends StorytellerTest {
	public static final String TEST_GENDER = "testGender";
	public static final String TEST_CLASSES_DESCRIPTION = "testClassName1: testDescription1\ntestClassName2: testDescription2\ntestClassName3: testDescription3\n";
	public static final String TEST_SKILLS_DESCRIPTION = "testSkillName1: testDescription4\ntestSkillName2: testDescription5\n";

	@Mock
	private TelegramElementRegistry elementRegistry;
	@Mock
	private ClassService classService;
	@Mock
	private SkillService skillService;
	@Mock
	private TelegramClient telegramClient;
	@Mock
	private MainMenuTelegramClient mainMenuTelegramClient;
	@Spy
	private Localization localization = createTestLocalization();

	@InjectMocks
	private RegistrationTelegramClient registrationTelegramClient;

	@BeforeEach
	public void beforeTest() throws ElementNotFoundException {
		when(elementRegistry.buildElement(REGISTRATION_GROUP_NAME, Gender.MALE.getElementName(), Collections.emptyMap(), String.class))
				.thenReturn(TEST_GENDER);
	}

	@Test
	public void sendGenderOptions() throws TelegramApiException, ElementNotFoundException {
		ActionContext testActionContext = createTestActionContext();
		BotApiMethod<?> mockMessage = mock(BotApiMethod.class);
		Map<String, String> params = Map.of(DefaultParameter.CHAT_ID.getKey(), testActionContext.userId().toString());
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(createCharGender.getGroupName(), createCharGender.name(), params);

		registrationTelegramClient.sendGenderOptions(testActionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void requestName() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext actionContext = testActionResult.getActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = buildTestRegistrationParameters(actionContext.messageId(), characterRequest);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(createCharName.getGroupName(), createCharName.name(), params);

		registrationTelegramClient.requestName(testActionResult);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassOptions() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext actionContext = testActionResult.getActionContext();
		List<ClassDictionary> classes = List.of(TEST_CLASS_POJO_1, TEST_CLASS_POJO_2, TEST_CLASS_POJO_3);
		when(classService.getAllClasses()).thenReturn(classes);
		SendMessage mockMessage = mock(SendMessage.class);
		Parameter classesDescription = Parameter.of(RegistrationParameter.CLASSES_DESCRIPTION, TEST_CLASSES_DESCRIPTION);
		Map<String, String> params = buildTestRegistrationParameters(actionContext.messageId(), characterRequest, classesDescription);

		InlineKeyboardButtonParameters dynamicParams = new InlineKeyboardButtonParameters();
		dynamicParams.add(TEST_CLASS_NAME_1, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), TEST_CLASS_1));
		dynamicParams.add(TEST_CLASS_NAME_2, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), TEST_CLASS_2));
		dynamicParams.add(TEST_CLASS_NAME_3, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), TEST_CLASS_3));

		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charClassSelection.getGroupName(), charClassSelection.name(), params,
				Map.of(RegistrationParameter.CLASSES.getKey(), dynamicParams));

		registrationTelegramClient.sendClassOptions(testActionResult);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassDescription() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		characterRequest.setClassName(TEST_ACTIVE_CLASS_POJO_1.name());
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext actionContext = testActionResult.getActionContext();
		List<ClassDictionary> classes = List.of(TEST_CLASS_POJO_1, TEST_CLASS_POJO_2, TEST_CLASS_POJO_3);
		when(classService.getAllClasses()).thenReturn(classes);
		when(classService.getClassIdByName(TEST_ACTIVE_CLASS_POJO_1.name())).thenReturn(TEST_CLASS_POJO_1.id());

		SkillDictionary testSkill1 = new SkillDictionary("testSkill1", SkillType.BASIC_CHARACTER, Archetype.SWORDSMAN);
		SkillDictionary testSkill2 = new SkillDictionary("testSkill2", SkillType.BASIC_CHARACTER, Archetype.SWORDSMAN);
		List<SkillDictionary> skills = List.of(testSkill1, testSkill2);
		when(skillService.getAllSkillsByClassName(TEST_ACTIVE_CLASS_POJO_1.name())).thenReturn(skills);

		SendMessage mockMessage = mock(SendMessage.class);
		Parameter classDescription = Parameter.of(RegistrationParameter.SKILLS_DESCRIPTION, TEST_SKILLS_DESCRIPTION);
		Map<String, String> params = buildTestRegistrationParameters(actionContext.messageId(), characterRequest, classDescription);

		InlineKeyboardButtonParameters dynamicParams = new InlineKeyboardButtonParameters();
		dynamicParams.add(TEST_CONFIRMATION_CLASS_PARAM, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), TEST_CLASS_1));
		dynamicParams.add(TEST_CLASS_NAME_2, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), TEST_CLASS_2));
		dynamicParams.add(TEST_CLASS_NAME_3, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), TEST_CLASS_3));

		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charClassConfirmation.getGroupName(), charClassConfirmation.name(), params,
				Map.of(RegistrationParameter.CLASSES.getKey(), dynamicParams));
		when(elementRegistry.buildElement(confirmationEmoji.getGroupName(), confirmationEmoji.name(), Collections.emptyMap(), String.class))
				.thenReturn(TEST_CONFIRMATION_EMOJI);

		registrationTelegramClient.sendClassDescription(testActionResult);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendRegistrationRequest() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext actionContext = testActionResult.getActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = buildTestRegistrationParameters(actionContext.messageId(), characterRequest);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charRegistration.getGroupName(), charRegistration.name(), params);

		registrationTelegramClient.sendRegistrationRequest(testActionResult);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendRegistrationConfirmation() throws TelegramApiException, ElementNotFoundException {
		ActionContext actionContext = createTestActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()),
				Parameter.of(DefaultParameter.MESSAGE_ID, actionContext.messageId())
		);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(registrationComplete.getGroupName(), registrationComplete.name(), params);

		registrationTelegramClient.sendRegistrationConfirmation(actionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
		verify(mainMenuTelegramClient, times(1)).sendMainMenu(actionContext);
	}

	@Test
	public void resendCharacterNameRequest() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()),
				Parameter.of(CharacterParameter.CHAR_GENDER, TEST_GENDER)
		);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(invalidCharName.getGroupName(), invalidCharName.name(), params);

		registrationTelegramClient.resendCharacterNameRequest(testActionResult);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	private Map<String, String> buildTestRegistrationParameters(int lastMessageId, CharacterRequest characterRequest, Parameter... additionalParams) {
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()));
		params.add(Parameter.of(DefaultParameter.MESSAGE_ID, lastMessageId));
		params.add(Parameter.of(CharacterParameter.CHAR_GENDER, TEST_GENDER));
		params.add(Parameter.of(CharacterParameter.CHAR_NAME, characterRequest.getName()));
		params.add(Parameter.of(CharacterParameter.CHAR_CLASS, localization.getClasses().get(characterRequest.getClassName()).getName()));
		params.addAll(Arrays.asList(additionalParams));

		return Parameters.buildParameters(params);
	}
}
