package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuTelegramClient;
import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
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

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
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
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = buildTestRegistrationParameters(characterRequest);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(createCharName.getGroupName(), createCharName.name(), params);

		registrationTelegramClient.requestName(characterRequest);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassOptions() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		List<ClassDictionary> classes = List.of(TEST_CLASS_POJO_1, TEST_CLASS_POJO_2, TEST_CLASS_POJO_3);
		when(classService.getAllClasses()).thenReturn(classes);
		SendMessage mockMessage = mock(SendMessage.class);
		Parameter classesDescription = Parameter.of(RegistrationParameter.CLASSES_DESCRIPTION, TEST_CLASSES_DESCRIPTION);
		Parameter warriorClassParameter = Parameter.of(RegistrationParameter.WARRIOR_CLASS_TEXT, TEST_CLASS_NAME_1);
		Parameter rogueClassParameter = Parameter.of(RegistrationParameter.ROGUE_CLASS_TEXT, TEST_CLASS_NAME_2);
		Map<String, String> params = buildTestRegistrationParameters(characterRequest, classesDescription, warriorClassParameter, rogueClassParameter);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charClassSelection.getGroupName(), charClassSelection.name(), params);

		registrationTelegramClient.sendClassOptions(characterRequest);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassDescription() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		characterRequest.setClassName(TEST_CLASS_POJO_1.name());
		List<ClassDictionary> classes = List.of(TEST_CLASS_POJO_1, TEST_CLASS_POJO_2, TEST_CLASS_POJO_3);
		when(classService.getAllClasses()).thenReturn(classes);
		when(classService.getClassIdByName(TEST_CLASS_POJO_1.name())).thenReturn(TEST_CLASS_POJO_1.id());

		SkillDictionary testSkill1 = new SkillDictionary("testSkill1");
		SkillDictionary testSkill2 = new SkillDictionary("testSkill2");
		List<SkillDictionary> skills = List.of(testSkill1, testSkill2);
		when(classService.getAllSkillsByClassId(TEST_CLASS_POJO_1.id())).thenReturn(skills);

		SendMessage mockMessage = mock(SendMessage.class);
		Parameter classDescription = Parameter.of(RegistrationParameter.SKILLS_DESCRIPTION, TEST_SKILLS_DESCRIPTION);
		Parameter classConfirmationParam = Parameter.of(RegistrationParameter.WARRIOR_CLASS_TEXT, TEST_CONFIRMATION_CLASS_PARAM);
		Parameter rogueClassParameter = Parameter.of(RegistrationParameter.ROGUE_CLASS_TEXT, TEST_CLASS_NAME_2);
		Map<String, String> params = buildTestRegistrationParameters(characterRequest, classDescription, rogueClassParameter, classConfirmationParam);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charClassConfirmation.getGroupName(), charClassConfirmation.name(), params);
		when(elementRegistry.buildElement(confirmationEmoji.getGroupName(), confirmationEmoji.name(), Collections.emptyMap(), String.class))
				.thenReturn(TEST_CONFIRMATION_EMOJI);

		registrationTelegramClient.sendClassDescription(characterRequest);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendRegistrationRequest() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = buildTestRegistrationParameters(characterRequest);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charRegistration.getGroupName(), charRegistration.name(), params);

		registrationTelegramClient.sendRegistrationRequest(characterRequest);

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
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()),
				Parameter.of(CharacterParameter.CHAR_GENDER, TEST_GENDER)
		);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(invalidCharName.getGroupName(), invalidCharName.name(), params);

		registrationTelegramClient.resendCharacterNameRequest(characterRequest);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	private Map<String, String> buildTestRegistrationParameters(CharacterRequest characterRequest, Parameter... additionalParams) {
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()));
		params.add(Parameter.of(DefaultParameter.MESSAGE_ID, characterRequest.getLastMessageId()));
		params.add(Parameter.of(CharacterParameter.CHAR_GENDER, TEST_GENDER));
		params.add(Parameter.of(CharacterParameter.CHAR_NAME, characterRequest.getName()));
		params.add(Parameter.of(CharacterParameter.CHAR_CLASS, localization.getClasses().get(characterRequest.getClassName()).getName()));
		params.addAll(Arrays.asList(additionalParams));

		return Parameters.buildParameters(params);
	}
}
