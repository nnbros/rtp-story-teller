package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.nnbros.rtp.common.action.ActionContext;
import com.github.nnbros.rtp.common.action.ActionPipeline;
import com.github.nnbros.rtp.common.action.ActionResult;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RegistrationActionRegistrarTest extends StorytellerTest {
	@Mock
	private RegistrationService registrationService;
	@Mock
	private RegistrationTelegramClient telegramClient;

	@InjectMocks
	private RegistrationActionRegistrar registrar;

	private final HashMap<String, ActionPipeline> actionPipelines = new HashMap<>();

	@BeforeEach
	public void initPipelines() {
		actionPipelines.clear();
		registrar.register(actionPipelines);
	}

	@Test
	public void registerPipelines() {
		Arrays.stream(CharacterAction.values())
				.map(CharacterAction::getActionName)
				.forEach(action -> assertNotNull(actionPipelines.get(action)));
	}

	@Test
	public void executeCreateCharPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CREATE_START.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).createCharacter(testActionContext);
		verify(telegramClient, times(1)).sendGenderOptions(testActionContext);
	}

	@Test
	public void executeCharGenderPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CREATE_GENDER.getActionName());
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext testActionContext = testActionResult.getActionContext();
		when(registrationService.addGender(testActionContext)).thenReturn(testActionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addGender(testActionContext);
		verify(telegramClient, times(1)).requestName(testActionResult);
	}

	@Test
	public void executeValidCharNamePipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CREATE_NAME.getActionName());
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext testActionContext = testActionResult.getActionContext();
		when(registrationService.addName(testActionContext)).thenReturn(testActionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addName(testActionContext);
		verify(telegramClient, times(1)).sendClassOptions(testActionResult);
	}

	@Test
	public void executeIllegalCharNamePipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CREATE_NAME.getActionName());
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest, false);
		ActionContext testActionContext = testActionResult.getActionContext();
		when(registrationService.addName(testActionContext)).thenReturn(testActionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addName(testActionContext);
		verify(telegramClient, times(1)).resendCharacterNameRequest(testActionResult);
	}

	@Test
	public void executeClassSelectionPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CLASS_SELECTION.getActionName());
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext testActionContext = testActionResult.getActionContext();
		when(registrationService.addClass(testActionContext)).thenReturn(testActionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addClass(testActionContext);
		verify(telegramClient, times(1)).sendClassDescription(testActionResult);
	}

	@Test
	public void executeClassSelectionPipelineAlternativeAction() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CLASS_SELECTION.getActionName());
		ActionContext testActionContext = createTestActionContext();
		when(registrationService.addClass(testActionContext)).thenReturn(new ActionResult<>(null, false));

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addClass(testActionContext);
		verifyNoInteractions(telegramClient);
	}

	@Test
	public void executeClassConfirmationPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CLASS_CONFIRMATION.getActionName());
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> testActionResult = createTestActionResultWithValue(characterRequest);
		ActionContext testActionContext = testActionResult.getActionContext();
		when(registrationService.getCharacterRequest(testActionContext)).thenReturn(testActionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).getCharacterRequest(testActionContext);
		verify(telegramClient, times(1)).sendRegistrationRequest(testActionResult);
	}

	@Test
	public void executeRegistrationConfirmationPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.REGISTRATION.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).register(testActionContext);
		verify(telegramClient, times(1)).sendRegistrationConfirmation(testActionContext);
	}
}
