package com.github.nnbros.rtp.storyteller.registration;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.ActionResult;
import com.github.nnbros.rtp.storyteller.character.CharacterAction;
import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.Gender;
import com.github.nnbros.rtp.storyteller.configuration.StoryTellerProperties;
import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterService;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassService;
import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.stream.Stream;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest extends StorytellerTest {
	public static final String CHARACTER_NAME_PATTERN = "^[A-Za-zА-Яа-я0-9_]{4,32}$";
	public static final String TEST_CLASS = "testClass";

	@Mock
	private CharacterService characterService;
	@Mock
	private StoryTellerProperties properties;
	@Mock
	private ClassService classService;

	@InjectMocks
	private RegistrationService registrationService;

	@Test
	public void createCharacter() {
		ActionContext testActionContext = createTestActionContext();
		when(characterService.containsByUser(TEST_USER_ID)).thenReturn(false);

		registrationService.createCharacter(testActionContext);

		CharacterRequest characterRequest = registrationService.getCharacterRequest(testActionContext);
		assertNotNull(characterRequest);
		assertEquals(TEST_USER_ID, characterRequest.getUserId());
	}

	@Test
	public void createCharacterIfItAlreadyExists() {
		ActionContext testActionContext = createTestActionContext();
		when(characterService.containsByUser(TEST_USER_ID)).thenReturn(true);

		assertThrows(StoryTellerRuntimeException.class, () -> registrationService.createCharacter(testActionContext));
	}

	@Test
	public void getNotExistingCharacter() {
		ActionContext testActionContext = createTestActionContext();
		assertThrows(CharacterNotFoundException.class, () -> registrationService.getCharacterRequest(testActionContext));
	}

	@ParameterizedTest
	@MethodSource("provideGenders")
	public void addGender(String gender, Gender expectedGender) {
		ActionContext testActionContext = new ActionContext(CharacterAction.CREATE_GENDER.getActionName(),
				TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, gender);

		registrationService.createCharacter(testActionContext);
		registrationService.addGender(testActionContext);

		CharacterRequest characterRequest = registrationService.getCharacterRequest(testActionContext);
		assertNotNull(characterRequest);
		assertEquals(TEST_USER_ID, characterRequest.getUserId());
		assertEquals(expectedGender, characterRequest.getGender());
	}

	@Test
	public void addEmptyGender() {
		ActionContext testActionContext = new ActionContext(CharacterAction.CREATE_GENDER.getActionName(),
				TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);

		registrationService.createCharacter(testActionContext);
		assertThrows(StoryTellerRuntimeException.class, () -> registrationService.addGender(testActionContext));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"test",
			"testName",
			"VeryLongName12345678901234567890",
			"test_name_123_456",
			"1234567890",
			"кирилица_1123_ййй"
	})
	public void addValidName(String name) {
		Update update = createTestMessageUpdate(createTestMessage(name));
		ActionContext testActionContext = new ActionContext(CharacterAction.CREATE_NAME.getActionName(),
				TEST_USER_ID, UpdateType.MESSAGE, update, TEST_MESSAGE_ID, null);
		when(properties.getCharacterNamePattern()).thenReturn(CHARACTER_NAME_PATTERN);

		registrationService.createCharacter(testActionContext);
		ActionResult<CharacterRequest> actionResult = registrationService.addName(testActionContext);

		assertNotNull(actionResult);
		assertTrue(actionResult.isSuccessful());
		CharacterRequest characterRequest = actionResult.value();
		assertNotNull(characterRequest);
		assertEquals(TEST_USER_ID, characterRequest.getUserId());
		assertEquals(name, characterRequest.getName());
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"tes",
			"12",
			"VeryLongName123456789012345678901",
			"!@#$%^&*(),./",
			"",
			"   ",
			"test_name!"
	})
	public void addIllegalName(String name) {
		Update update = createTestMessageUpdate(createTestMessage(name));
		ActionContext testActionContext = new ActionContext(CharacterAction.CREATE_NAME.getActionName(),
				TEST_USER_ID, UpdateType.MESSAGE, update, TEST_MESSAGE_ID, null);
		when(properties.getCharacterNamePattern()).thenReturn(CHARACTER_NAME_PATTERN);

		registrationService.createCharacter(testActionContext);
		ActionResult<CharacterRequest> actionResult = registrationService.addName(testActionContext);

		assertNotNull(actionResult);
		assertFalse(actionResult.isSuccessful());
		CharacterRequest characterRequest = actionResult.value();
		assertNotNull(characterRequest);
		assertEquals(TEST_USER_ID, characterRequest.getUserId());
	}

	@ParameterizedTest
	@EnumSource(value = UpdateType.class, names = {"MESSAGE", "EDITED_MESSAGE"}, mode = EnumSource.Mode.EXCLUDE)
	public void addNameWithWrongUpdateType(UpdateType updateType) {
		ActionContext testActionContext = new ActionContext(CharacterAction.CREATE_NAME.getActionName(),
				TEST_USER_ID, updateType, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);

		registrationService.createCharacter(testActionContext);
		assertThrows(StoryTellerRuntimeException.class, () -> registrationService.addName(testActionContext));
	}

	@Test
	public void addClass() {
		ActionContext testActionContext = new ActionContext(CharacterAction.CLASS_SELECTION.getActionName(),
				TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, TEST_CLASS);
		when(classService.containsByName(TEST_CLASS)).thenReturn(true);

		registrationService.createCharacter(testActionContext);
		registrationService.addClass(testActionContext);

		CharacterRequest characterRequest = registrationService.getCharacterRequest(testActionContext);
		assertNotNull(characterRequest);
		assertEquals(TEST_USER_ID, characterRequest.getUserId());
		assertEquals(TEST_CLASS, characterRequest.getClassName());
	}

	@Test
	public void addNotExistingClass() {
		ActionContext testActionContext = new ActionContext(CharacterAction.CLASS_SELECTION.getActionName(),
				TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, TEST_CLASS);
		when(classService.containsByName(TEST_CLASS)).thenReturn(false);

		registrationService.createCharacter(testActionContext);
		assertThrows(StoryTellerRuntimeException.class, () -> registrationService.addClass(testActionContext));
	}

	@Test
	public void addEmptyClass() {
		ActionContext testActionContext = new ActionContext(CharacterAction.CLASS_SELECTION.getActionName(),
				TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);

		registrationService.createCharacter(testActionContext);
		assertThrows(StoryTellerRuntimeException.class, () -> registrationService.addClass(testActionContext));
	}

	@Test
	public void register() {
		ActionContext testActionContext = new ActionContext(CharacterAction.REGISTRATION.getActionName(),
				TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);

		registrationService.createCharacter(testActionContext);
		registrationService.register(testActionContext);

		CharacterRequest characterRequest = registrationService.getCharacterRequest(testActionContext);
		assertNotNull(characterRequest);
		verify(characterService, times(1)).register(characterRequest);
	}

	@Test
	public void clearCache() {
		ActionContext testActionContext1 = new ActionContext(CharacterAction.CREATE_START.getActionName(),
				1L, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);
		ActionContext testActionContext2 = new ActionContext(CharacterAction.CREATE_START.getActionName(),
				2L, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);
		ActionContext testActionContext3 = new ActionContext(CharacterAction.CREATE_START.getActionName(),
				3L, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, null);

		registrationService.createCharacter(testActionContext1);
		registrationService.createCharacter(testActionContext2);
		registrationService.createCharacter(testActionContext3);

		assertNotNull(registrationService.getCharacterRequest(testActionContext1));
		assertNotNull(registrationService.getCharacterRequest(testActionContext2));
		assertNotNull(registrationService.getCharacterRequest(testActionContext3));

		registrationService.clearCharacterCache(Set.of(1L, 2L, 3L));

		assertThrows(CharacterNotFoundException.class, () -> registrationService.getCharacterRequest(testActionContext1));
		assertThrows(CharacterNotFoundException.class, () -> registrationService.getCharacterRequest(testActionContext2));
		assertThrows(CharacterNotFoundException.class, () -> registrationService.getCharacterRequest(testActionContext3));
	}

	private static Stream<Arguments> provideGenders() {
		return Stream.of(
				Arguments.of("male", Gender.MALE),
				Arguments.of("female", Gender.FEMALE)
		);
	}
}
