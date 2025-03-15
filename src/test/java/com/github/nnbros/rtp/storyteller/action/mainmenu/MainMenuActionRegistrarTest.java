package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.ActionPipeline;
import com.github.nnbros.rtp.storyteller.action.ActionResult;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.createTestActionContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MainMenuActionRegistrarTest extends StorytellerTest {
	@Mock
	private CharacterService characterService;

	@Mock
	private MainMenuTelegramClient telegramClient;

	@Mock
	private CharacterProcessor characterProcessor;

	@InjectMocks
	private MainMenuActionRegistrar registrar;

	private final HashMap<String, ActionPipeline> actionPipelines = new HashMap<>();

	@BeforeEach
	public void initPipelines() {
		actionPipelines.clear();
		registrar.register(actionPipelines);
	}

	@Test
	public void registerPipelines() {
		Arrays.stream(MainMenuAction.values())
				.map(MainMenuAction::getActionName)
				.forEach(action -> assertNotNull(actionPipelines.get(action)));
	}

	@Test
	public void executeMaineMenuPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(MainMenuAction.MAIN_MENU.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(telegramClient, times(1)).sendMainMenu(testActionContext);
	}

	@Test
	public void executeMonsterHuntPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(MainMenuAction.MONSTER_HUNT.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(telegramClient, times(1)).sendMonsterHuntMenu(testActionContext);
	}

	@Test
	public void executeCharacterPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(MainMenuAction.CHARACTER.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(telegramClient, times(1)).sendCharacterMenu(testActionContext);
	}

	@Test
	public void executeArmyPipelineWhenActiveArmyIsUpdated() {
		ActionPipeline actionPipeline = actionPipelines.get(MainMenuAction.ARMY.getActionName());
		ActionContext testActionContext = createTestActionContext();
		ActionResult<ActionContext> actionResult = new ActionResult<>(testActionContext, true);
		when(characterProcessor.updateActiveArmy(testActionContext)).thenReturn(actionResult);

		actionPipeline.execute(testActionContext);

		verify(characterProcessor, times(1)).updateActiveArmy(testActionContext);
		verify(telegramClient, times(1)).sendArmyMenu(testActionContext);
	}

	@Test
	public void executeArmyPipelineWhenActiveArmyIsNotUpdated() {
		ActionPipeline actionPipeline = actionPipelines.get(MainMenuAction.ARMY.getActionName());
		ActionContext testActionContext = createTestActionContext();
		ActionResult<ActionContext> actionResult = new ActionResult<>(testActionContext, false);
		when(characterProcessor.updateActiveArmy(testActionContext)).thenReturn(actionResult);

		actionPipeline.execute(testActionContext);

		verify(characterProcessor, times(1)).updateActiveArmy(testActionContext);
		verifyNoInteractions(telegramClient);
	}
}
