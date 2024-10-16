package com.github.nnbros.rtp.storyteller.registration;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassService;
import com.github.nnbros.rtp.storyteller.telegram.ui.RegistrationTelegramElementFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.mockito.Mockito.*;

public class RegistrationTelegramClientTest extends StorytellerTest {
	public static final ClassDictionary TEST_CLASS_1 = new ClassDictionary(1, "testClass1", "testDescription1", 1, 1, 1);
	public static final ClassDictionary TEST_CLASS_2 = new ClassDictionary(2, "testClass2", "testDescription2", 2, 2, 2);
	public static final ClassDictionary TEST_CLASS_3 = new ClassDictionary(3, "testClass3", "testDescription3", 3, 3, 3);

	@Mock
	private RegistrationTelegramElementFactory elementFactory;
	@Mock
	private ClassService classService;
	@Mock
	private TelegramClient telegramClient;

	@InjectMocks
	private RegistrationTelegramClient registrationTelegramClient;

	@Test
	public void sendGenderOptions() throws TelegramApiException {
		ActionContext testActionContext = createTestActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		when(elementFactory.characterGenderMessage(testActionContext.userId())).thenReturn(mockMessage);

		registrationTelegramClient.sendGenderOptions(testActionContext);

		verify(elementFactory, times(1)).characterGenderMessage(testActionContext.userId());
		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void requestName() throws TelegramApiException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		SendMessage mockMessage = mock(SendMessage.class);
		when(elementFactory.characterNameMessage(characterRequest)).thenReturn(mockMessage);

		registrationTelegramClient.requestName(characterRequest);

		verify(elementFactory, times(1)).characterNameMessage(characterRequest);
		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassOptions() throws TelegramApiException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		List<ClassDictionary> classes = List.of(TEST_CLASS_1, TEST_CLASS_2, TEST_CLASS_3);
		when(classService.getAllClasses()).thenReturn(classes);
		SendMessage mockMessage = mock(SendMessage.class);
		when(elementFactory.classSelectionMessage(characterRequest, classes)).thenReturn(mockMessage);

		registrationTelegramClient.sendClassOptions(characterRequest);

		verify(elementFactory, times(1)).classSelectionMessage(characterRequest, classes);
		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendClassDescription() throws TelegramApiException {
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
		when(elementFactory.classConfirmationMessage(characterRequest, skills, classes)).thenReturn(mockMessage);

		registrationTelegramClient.sendClassDescription(characterRequest);

		verify(elementFactory, times(1)).classConfirmationMessage(characterRequest, skills, classes);
		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendRegistrationRequest() throws TelegramApiException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		SendMessage mockMessage = mock(SendMessage.class);
		when(elementFactory.registrationConfirmationMessage(characterRequest)).thenReturn(mockMessage);

		registrationTelegramClient.sendRegistrationRequest(characterRequest);

		verify(elementFactory, times(1)).registrationConfirmationMessage(characterRequest);
		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void sendRegistrationConfirmation() throws TelegramApiException {
		ActionContext actionContext = createTestActionContext();
		SendMessage mockMessage = mock(SendMessage.class);
		when(elementFactory.successFullRegistrationMessage(TEST_USER_ID)).thenReturn(mockMessage);

		registrationTelegramClient.sendRegistrationConfirmation(actionContext);

		verify(elementFactory, times(1)).successFullRegistrationMessage(TEST_USER_ID);
		verify(telegramClient, times(1)).execute(mockMessage);
	}

	@Test
	public void resendCharacterNameRequest() throws TelegramApiException {
		CharacterRequest characterRequest = createTestCharacterRequest();
		SendMessage mockMessage = mock(SendMessage.class);
		when(elementFactory.illegalCharacterNameMessage(characterRequest)).thenReturn(mockMessage);

		registrationTelegramClient.resendCharacterNameRequest(characterRequest);

		verify(elementFactory, times(1)).illegalCharacterNameMessage(characterRequest);
		verify(telegramClient, times(1)).execute(mockMessage);
	}
}
