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

import java.util.HashMap;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.createTestActionContext;
import static org.mockito.Mockito.*;

public class CharacterMenuActionRegistrarTest extends StorytellerTest {
	@Mock
	private CharacterService characterService;

	@Mock
	CharacterMenuService characterMenuService;

	@Mock
	private CharacterMenuTelegramClient telegramClient;

	@InjectMocks
	private CharacterMenuActionRegistrar registrar;

	private final HashMap<String, ActionPipeline> actionPipelines = new HashMap<>();

	@BeforeEach
	public void initPipelines() {
		actionPipelines.clear();
		registrar.register(actionPipelines);
	}

	@Test
	public void executeCharacterDetailsPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterMenuAction.CHARACTER_DETAILS.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(telegramClient, times(1)).sendCharacterDetails(testActionContext);
	}

	@Test
	public void executeCharacterDeckBuilderPipeline() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterMenuAction.CHARACTER_DECK_BUILDER.getActionName());
		ActionContext testActionContext = createTestActionContext();

		actionPipeline.execute(testActionContext);

		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(telegramClient, times(1)).sendCharacterDeckBuilder(testActionContext);
	}

	@Test
	public void executeCharacterClassPipelineWhenActiveClassIsUpdated() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterMenuAction.CLASS.getActionName());
		ActionContext testActionContext = createTestActionContext();
		ActionResult<ActionContext> actionResult = new ActionResult<>(testActionContext, true);
		when(characterMenuService.updateActiveClass(testActionContext)).thenReturn(actionResult);

		actionPipeline.execute(testActionContext);

		verify(characterMenuService, times(1)).updateActiveClass(testActionContext);
		verify(telegramClient, times(1)).sendCharacterClassMenu(testActionContext);
	}

	@Test
	public void executeCharacterClassPipelineWhenActiveClassIsNotUpdated() {
		ActionPipeline actionPipeline = actionPipelines.get(CharacterMenuAction.CLASS.getActionName());
		ActionContext testActionContext = createTestActionContext();
		ActionResult<ActionContext> actionResult = new ActionResult<>(testActionContext, false);
		when(characterMenuService.updateActiveClass(testActionContext)).thenReturn(actionResult);

		actionPipeline.execute(testActionContext);

		verify(characterMenuService, times(1)).updateActiveClass(testActionContext);
		verifyNoInteractions(telegramClient);
	}
}
