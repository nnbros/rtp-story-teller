package com.github.nnbros.rtp.storyteller.registration;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.Gender;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassService;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import com.github.nnbros.rtp.storyteller.telegram.ui.Parameter;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParametersBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static com.github.nnbros.rtp.storyteller.registration.RegistrationElement.*;
import static org.mockito.Mockito.*;

public class RegistrationTelegramClientTest extends StorytellerTest {
	public static final ClassDictionary TEST_CLASS_1 = new ClassDictionary(1, "testClass1", "testDescription1", 1, 1, 1);
	public static final ClassDictionary TEST_CLASS_2 = new ClassDictionary(2, "testClass2", "testDescription2", 2, 2, 2);
	public static final ClassDictionary TEST_CLASS_3 = new ClassDictionary(3, "testClass3", "testDescription3", 3, 3, 3);
	public static final String TEST_GENDER = "testGender";
	public static final String TEST_CLASSES_DESCRIPTION = "testClass1: testDescription1\ntestClass2: testDescription2\ntestClass3: testDescription3\n";
	public static final String TEST_SKILLS_DESCRIPTION = "testSkill1: testDescription1\ntestSkill2: testDescription2\n";
	public static final String CONFIRMATION_EMOJI = ":)";
	public static final String CONFIRMATION_CLASS_PARAM = CONFIRMATION_EMOJI + "testClass1";

	@Mock
	private TelegramElementRegistry elementRegistry;
	@Mock
	private ClassService classService;
	@Mock
	private TelegramClient telegramClient;
	private final ParametersBuilder parametersBuilder = new ParametersBuilder();

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
		List<ClassDictionary> classes = List.of(TEST_CLASS_1, TEST_CLASS_2, TEST_CLASS_3);
		when(classService.getAllClasses()).thenReturn(classes);
		SendMessage mockMessage = mock(SendMessage.class);
		Parameter classesDescription = Parameter.of(RegistrationParameter.CLASSES_DESCRIPTION, TEST_CLASSES_DESCRIPTION);
		Map<String, String> params = buildTestRegistrationParameters(characterRequest, classesDescription);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charClassSelection.getGroupName(), charClassSelection.name(), params);

		registrationTelegramClient.sendClassOptions(characterRequest);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassDescription() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		characterRequest.setClassName(TEST_CLASS_1.name());
		List<ClassDictionary> classes = List.of(TEST_CLASS_1, TEST_CLASS_2, TEST_CLASS_3);
		when(classService.getAllClasses()).thenReturn(classes);
		when(classService.getClassIdByName(TEST_CLASS_1.name())).thenReturn(TEST_CLASS_1.id());

		SkillDictionary testSkill1 = new SkillDictionary("testSkill1", "testDescription1");
		SkillDictionary testSkill2 = new SkillDictionary("testSkill2", "testDescription2");
		List<SkillDictionary> skills = List.of(testSkill1, testSkill2);
		when(classService.getAllSkillsByClassId(TEST_CLASS_1.id())).thenReturn(skills);

		SendMessage mockMessage = mock(SendMessage.class);
		Parameter classDescription = Parameter.of(RegistrationParameter.SKILLS_DESCRIPTION, TEST_SKILLS_DESCRIPTION);
		Parameter classConfirmationParam = Parameter.of(RegistrationParameter.CONFIRMATION_ROUGE, CONFIRMATION_CLASS_PARAM);
		Map<String, String> params = buildTestRegistrationParameters(characterRequest, classDescription, classConfirmationParam);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(charClassConfirmation.getGroupName(), charClassConfirmation.name(), params);
		when(elementRegistry.buildElement(confirmationEmoji.getGroupName(), confirmationEmoji.name(), Collections.emptyMap(), String.class))
				.thenReturn(CONFIRMATION_EMOJI);

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
		Map<String, String> params = parametersBuilder.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()),
				Parameter.of(DefaultParameter.MESSAGE_ID, actionContext.messageId())
		);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(registrationComplete.getGroupName(), registrationComplete.name(), params);

		registrationTelegramClient.sendRegistrationConfirmation(actionContext);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void resendCharacterNameRequest() throws TelegramApiException, ElementNotFoundException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		SendMessage mockMessage = mock(SendMessage.class);
		Map<String, String> params = parametersBuilder.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()),
				Parameter.of(RegistrationParameter.CHAR_GENDER, TEST_GENDER)
		);
		doReturn(mockMessage).when(elementRegistry).buildBotApiMethod(invalidCharName.getGroupName(), invalidCharName.name(), params);

		registrationTelegramClient.resendCharacterNameRequest(characterRequest);

		verify(telegramClient, times(1)).execute(mockMessage);
	}

	private Map<String, String> buildTestRegistrationParameters(CharacterRequest characterRequest, Parameter... additionalParams) {
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()));
		params.add(Parameter.of(DefaultParameter.MESSAGE_ID, characterRequest.getLastMessageId()));
		params.add(Parameter.of(RegistrationParameter.CHAR_GENDER, TEST_GENDER));
		params.add(Parameter.of(RegistrationParameter.CHAR_NAME, characterRequest.getName()));
		params.add(Parameter.of(RegistrationParameter.CHAR_CLASS, characterRequest.getClassName()));
		params.addAll(Arrays.asList(additionalParams));

		return parametersBuilder.buildParameters(params);
	}
}
