package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.ActionPipeline;
import com.github.nnbros.rtp.storyteller.action.ActionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.createTestActionContext;
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
		ActionContext testActionContext = createTestActionContext();
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		when(registrationService.addGender(testActionContext)).thenReturn(characterRequest);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addGender(testActionContext);
		verify(telegramClient, times(1)).requestName(characterRequest);
	}

	@Test
	public void executeValidCharNamePipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CREATE_NAME.getActionName());
		ActionContext testActionContext = createTestActionContext();
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> actionResult = new ActionResult<>(characterRequest, true);
		when(registrationService.addName(testActionContext)).thenReturn(actionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addName(testActionContext);
		verify(telegramClient, times(1)).sendClassOptions(characterRequest);
	}

	@Test
	public void executeIllegalCharNamePipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CREATE_NAME.getActionName());
		ActionContext testActionContext = createTestActionContext();
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		ActionResult<CharacterRequest> actionResult = new ActionResult<>(characterRequest, false);
		when(registrationService.addName(testActionContext)).thenReturn(actionResult);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addName(testActionContext);
		verify(telegramClient, times(1)).resendCharacterNameRequest(characterRequest);
	}

	@Test
	public void executeClassSelectionPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterAction.CLASS_SELECTION.getActionName());
		ActionContext testActionContext = createTestActionContext();
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		when(registrationService.addClass(testActionContext)).thenReturn(new ActionResult<>(characterRequest, true));

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).addClass(testActionContext);
		verify(telegramClient, times(1)).sendClassDescription(characterRequest);
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
		ActionContext testActionContext = createTestActionContext();
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		when(registrationService.getCharacterRequest(testActionContext)).thenReturn(characterRequest);

		actionPipeline.execute(testActionContext);

		verify(registrationService, times(1)).getCharacterRequest(testActionContext);
		verify(telegramClient, times(1)).sendRegistrationRequest(characterRequest);
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
